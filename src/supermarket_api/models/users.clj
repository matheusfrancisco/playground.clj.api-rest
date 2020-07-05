(ns supermarket-api.models.users
  (:require [clojure.pprint :refer [pprint]]
            [datomic.api :as d]))

(defn create-user!
  "Receive an conn datomic and user
  and return a transaction"
  [_conn user]
  (pprint "=====user=====")
  (pprint user)
  (let [tx (d/transact-async _conn [user])]
    tx))
