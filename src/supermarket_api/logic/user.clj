(ns supermarket-api.logic.user)


(defn create-user
  [bp]
  {:user/id (java.util.UUID/randomUUID)
   :user/name (:user/name bp)
   :user/email (:user/email bp)
   :user/password (:user/password bp)
   :user/type (:user/type bp)})

(def user-schema
  {:user/name [clojure.string/blank?]
   :user/email [clojure.string/blank?]
   :user/password [clojure.string/blank?]
   :user/type [clojure.string/blank?]})

;(reduce (fn [acc v]
;          (let [bools (-> v first second :is-valid?)
;                field (-> v ffirst)]
;            (prn (map (fn [vv]
;                        (if vv
;                          (prn "t")
;                          (prn "f" field)))
;                      bools))
;            ))
;        ;(prn (->> v
;        ;          (first)
;        ;          (second)
;        ;          (:is-valid?)
;        ;          #(contains? % false))
;
;        {} ccc)
