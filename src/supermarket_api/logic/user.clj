(ns supermarket-api.logic.user)


(defn create-user
  [bp]
  {:user/id (java.util.UUID/randomUUID)
   :user/name (:user/name bp)
   :user/email (:user/email bp)
   :user/password (:user/password bp)
   :user/type (:user/type bp)})