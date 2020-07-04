(ns supermarket-api.components.config
  (:require [com.stuartsierra.component :as component]))

(defrecord Config [config]
  component/Lifecycle
  (start [this] this)
  (stop  [this] this))

(def config-map
  {; #TODO :db-uri (or (System/getenv "DB_URI") "datomic:free://localhost:4334/marketplace-api?password=my-pwd")
   :http-port (Integer/parseInt (or (System/getenv "HTTP_PORT") "3000"))
   :http-host (or (System/getenv "HTTP_HOST") "localhost")})

(defn new-config [input-map] (map->Config {:config (or input-map config-map)}))
