(ns supermarket-api.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [datomic.api :as d]
            [supermarket-api.logic.user :as u]
            [supermarket-api.models.users :as users]
            [clojure.pprint :refer [pprint]]
            [supermarket-api.protocols.storage-client :as storage-client]))

(defn ok [response]
  (ring-resp/response response))

(defn home-page
  [request]
  (ring-resp/response {:message "Hello World!!"}))

(defn get-all-users! [storage]
  (storage-client/read-all storage))

(defn find-users [db]
  (d/q '[:find [(pull ?user [:user/type
                             :user/email
                             :user/name
                             :user/id])
                ...]
         :where [?user :user/name]]
       db))

(defn get-all-users-datomic! [conn]
  (find-users conn))

(defn get-all-users
  [{{db :db} :components}]
  (ring-resp/response {:message (get-all-users-datomic! (d/db (:conn db)))}))



(defn handler-create-user
  [{:keys [components json-params] :as req}]
  (let [_conn (-> components
                  :db
                  :conn)
        errors (users/validate-user json-params)]

    (if (nil? errors)
      (let [tx (users/create-user! _conn (u/create-user json-params))]
        (ring-resp/response {:message "sucess" :status 201}))
      (ring-resp/response {:errors errors}))))


(def common-interceptors
  [(body-params/body-params) http/json-body])

(def routes
  #{["/" :get (conj common-interceptors `home-page)]
    ["/users" :get (conj common-interceptors `get-all-users)]
    ["/users" :post (conj common-interceptors `handler-create-user)]})