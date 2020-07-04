(ns supermarket-api.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [supermarket-api.protocols.storage-client :as storage-client]))

(defn ok [response]
  (ring-resp/response response))

(defn home-page
  [request]
  (ring-resp/response {:message "Hello World!!"}))

(defn get-all-users! [storage]
  (storage-client/read-all storage))

(defn get-all-users
  [{{storage :storage} :components}]
  (ring-resp/response {:message (get-all-users! storage)}))

(defn handler-create-user
  [{{conn :storage} :components}]
  ())


(def common-interceptors
  [(body-params/body-params) http/json-body])

(def routes
  #{["/" :get (conj common-interceptors `home-page)]
    ["/users" :get (conj common-interceptors `get-all-users)
     "/users" :post (conj common-interceptors `handler-create-user)]})
