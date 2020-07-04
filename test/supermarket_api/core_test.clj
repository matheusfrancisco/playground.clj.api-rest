(ns supermarket-api.core-test (:require [clojure.test :refer :all] [com.stuartsierra.component :as component] [clj-http.client :as client] [supermarket-api.components.config :as config]
            [supermarket-api.components.routes :as routes]
            [supermarket-api.service :as service]
            [supermarket-api.components.storage :as storage]
            [supermarket-api.components.webserver :as webserver]
            [supermarket-api.server :as s]))


(def system (atom nil))

(def config-map-test
  {; #TODO :db-uri (or (System/getenv "DB_URI") "datomic:free://localhost:4334/supermarket-api?password=my-pwd") :http-port 8080 :http-host "localhost"}) (defn- build-system-map-test [] (component/system-map :config (config/new-config config-map-test) :storage (storage/new-in-memory)
    :routes  (routes/new-routes #'service/routes)
    :http-server (component/using (webserver/new-webserver) [:config :routes :storage])))


(defn start-test []
  (build-system-map-test))

(defn with-system
  [f]
  (let [current-sys (component/start (start-test))]
    (f)
    (component/stop current-sys)))

(use-fixtures :each with-system)

(deftest testing-integration-routes
 (testing "GET"
   (let [resp (client/get "http://localhost:8080/")
         status (:status resp)]
     (is (= status 200)))))