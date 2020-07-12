(ns supermarket-api.logic.user)


(defn create-user
  [bp]
  {:user/id (java.util.UUID/randomUUID)
   :user/name (:user/name bp)
   :user/email (:user/email bp)
   :user/password (:user/password bp)
   :user/type (:user/type bp)})




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
