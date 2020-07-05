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


(comment (defn make-response-user
  [bools field acc]
  (map (fn [vv]
         (prn "oi 2" vv)
         (if vv
           (assoc-in acc [:invalid-field]
                     field)
           nil))
       bools))

(defn has-invalid?
  [fields-checked]
  (prn "oi")
  (reduce (fn [acc v]
            (let [bools (-> v first second :is-valid?)
                  field (-> v ffirst)]))
          {} fields-checked)))

; #TODO move to adapters user
(defn is-valid?
  [validators field]
  (reduce (fn [acc validator]
            (assoc acc :is-valid? (conj
                                    (:is-valid? acc) (validator field))))
          {:is-valid? []}
          validators))


; #TODO move to adapters user
(defn validate-fields
  [payload schema]
  (map (fn [v]
         (assoc {} :field v)
         (assoc {} v (is-valid?
                       (get schema (first v))
                       (second v))))
       payload))


(defn validate-user-body
  [bp]
  (pprint bp)
  (if-not (nil? bp)
    (let [validated-fields (validate-fields bp u/user-schema)]
      ; #TODO validate fields and return to user
      ; #TODO remove true from if and add validate reutrn
      (if true
        true
        false))
    {:type :body-empty :cause :body-empyt}))


(defn handler-create-user
  [{:keys [components json-params]}]
  (prn json-params)
  (prn (-> components
           :db
           :conn))
  (let [_conn (-> components
                  :db
                  :conn)
        tx (users/create-user! _conn (u/create-user json-params))]
    ;#TODO add validate here if user body was invalid
    (pprint "===== transaction ========")
    (pprint tx)
    (pprint "====== transaction =======")
    ;(validate-user-body json-params)
    (ring-resp/response {:message "sucess" :status 201})))


(def common-interceptors
  [(body-params/body-params) http/json-body])

(def routes
  #{["/" :get (conj common-interceptors `home-page)]
    ["/users" :get (conj common-interceptors `get-all-users)]
    ["/users" :post (conj common-interceptors `handler-create-user)]})