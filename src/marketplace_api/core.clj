(ns marketplace-api.core
  (:gen-class)
  (:require [com.stuartsierra.component :as component]
            [marketplace-api.components.config :as config]
            [marketplace-api.components.routes :as routes]
            ; #TODO[marketplace-api.components.storage-datomic :as storage]
            [marketplace-api.components.webserver :as webserver]
            [marketplace-api.server :as server]
            [marketplace-api.service :as service]
            [io.pedestal.service-tools.dev :as dev]))

(def system (atom nil))

(defn- build-system-map []
  (component/system-map
    :config (config/new-config config/config-map)
    ; #TODO implement
    ;:storage (component/using (storage/new-storage-datomic) [:config])
    :routes  (routes/new-routes #'marketplace-api.service/routes)
    :http-server (component/using (webserver/new-webserver) [:config :routes])))

(defn -main
  "The entry-point for 'lein run-dev'"
  [& args]
  (-> (build-system-map)
      (server/start-system! system)))

(defn run-dev []
  (dev/watch) ;; auto-reload namespaces only in run-dev / repl-start
  (-main))
