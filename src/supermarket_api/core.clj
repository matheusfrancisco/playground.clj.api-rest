(ns supermarket-api.core
  (:gen-class)
  (:require [com.stuartsierra.component :as component]
            [supermarket-api.components.config :as config]
            [supermarket-api.components.routes :as routes]
            [supermarket-api.components.storage :as storage]
            [supermarket-api.components.webserver :as webserver]
            [supermarket-api.server :as server]
            [supermarket-api.service :as service]
            [io.pedestal.service-tools.dev :as dev]))

(def system (atom nil))

(defn- build-system-map []
  (component/system-map
    :config (config/new-config config/config-map)
    :storage (storage/new-in-memory)
    :routes  (routes/new-routes #'supermarket-api.service/routes)
    :http-server (component/using (webserver/new-webserver) [:config :routes :storage])))

(defn -main
  "The entry-point for 'lein run-dev'"
  [& args]
  (-> (build-system-map)
      (server/start-system! system)))

(defn run-dev []
  (dev/watch) ;; auto-reload namespaces only in run-dev / repl-start
  (-main))
