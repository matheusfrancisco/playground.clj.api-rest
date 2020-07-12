(ns supermarket-api.models.users
  (:require [clojure.pprint :refer [pprint]]
            [datomic.api :as d]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]))



(def user-schema {:user/name (s/and string? (complement str/blank?))
             :user/email (s/and string? (complement str/blank?))
             :user/password (s/and string? (complement str/blank?))
             :user/type (s/and string? (complement str/blank?))})


(defn validate-user
  [bp]
  (if-not (nil? bp)
    ; #TODO extract reduce to function and write unit test
    ; #TODO extract fields to funciont and write unit test
    ; #TODO improve symbols names
    (let [valid-fields (reduce (fn [acc e]
                                 (let [f (first e)
                                       v (second e)]
                                   (assoc acc f (s/valid? ((keyword f) user-schema) v)))) {} bp)
          errors (filter (fn [element]
                           (when-not (nil? element)
                             element))
                         (map (fn [object]
                                (let [field (first object)
                                      value (second object)
                                      error {}]
                                  (if value
                                    nil
                                    (assoc error field "invalid")))) valid-fields))]
      errors)))


(defn create-user!
  "Receive an conn datomic and user
  and return a transaction"
  [_conn user]
  (let [tx (d/transact-async _conn [user])]
    tx))
