(ns supermarket-api.service-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [cheshire.core :as json]
            [supermarket-api.service :as service]
            [io.pedestal.test :refer :all :as pt]
            [supermarket-api.components.config :as config]
            [supermarket-api.components.routes :as routes]
            [clj-http.client :as client]
            [supermarket-api.components.storage :as storage]
            [supermarket-api.components.datomic :as dt]
            [supermarket-api.components.webserver :as webserver]
            [supermarket-api.server :as s]
            [environ.core :refer [env]]))

(def system (atom nil))


(def config-map-test
  {; #TODO :db-uri (or (System/getenv "DB_URI") "datomic:free://localhost:4334/marketplace-api?password=my-pwd")
   :http-port 8080
   :http-host "localhost"})

(defn- build-system-map-test []
  (component/system-map
    :config (config/new-config config-map-test)
    :db (dt/new-datomic (env :db-connection-uri))
    :storage (storage/new-in-memory)
    :routes  (routes/new-routes #'service/routes)
    :http-server (component/using (webserver/new-webserver) [:config :routes :storage :db])))

(defn get-service-fn [system]
  (get-in @system [:http-server :service :io.pedestal.http/service-fn]))

(defn do-request [service verb route body]
  (prn service verb route body)
  (pt/response-for
    service verb route :headers {"Content-Type" "application/json"} :body body))

(defn parsed-response-body [response-body]
  (prn response-body)
  (json/decode (:body response-body) true))

(def request-parsed
  (comp parsed-response-body do-request))

(defn start-test! [system]
  (->> (build-system-map-test)
       component/start
       (reset! system)))


(defn stop-system-test! [system]
  (swap! system #(component/stop %)))

(defn with-system
  [f]
  (do
    (start-test! system)
    (f)
    (stop-system-test! system)))

(use-fixtures :each with-system)

(deftest testing-integration-routes
 (testing "Add new user"
   (let [service (get-service-fn system)
         user "{\"user/name\": \"Matheus\",
               \"user/email\": \"xico@hotmail.com\",
               \"user/password\": \"123123\",
               \"user/type\": \"user\"}"]
      (let [resp (request-parsed service :post "/users" user)]
        (is (= (:status resp) 201)))))

 (testing "Should return an error if email is blank"
   (let [service (get-service-fn system)]
     (let  [user "{\"user/name\": \"Matheus\",
               \"user/email\": \"\",
               \"user/password\": \"123123\",
               \"user/type\": \"user\"}"
            resp (request-parsed service :post "/users" user)]
       (is (= (:errors resp) [{:user/email "invalid"}]))))))


