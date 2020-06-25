(ns marketplace-api.components.storage
  (:require [com.stuartsierra.component :as component]
            [marketplace-api.protocols.storage-client :as storage-client]))

(defrecord InMemoryStorage [storage]
  component/Lifecycle
  (start [component]
    component)
  (stop  [component]
    (reset! storage {})
    component)

  storage-client/StorageClient
  (read-all [_this] @storage)
  (put! [_this update-fn] (update-fn swap! storage))
  (clear-all! [_this] (reset! storage [])))

(defn new-in-memory []
  (->InMemoryStorage (atom [{:user/id 1
                             :user/name "Chico"
                             :user/email "xico@xico.com.br"}])))
