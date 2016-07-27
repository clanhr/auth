(ns ^{:added "1.13.0" :author "Pedro Pereira Santos"}
  clanhr.auth.roles-for
  "Logic that build specific roles for relations between two users"
  (:require [result.core :as result]))

(defn approver
  "Returns the approver role if the user is an approver of other-user"
  [user other-user]
  (if-let [user-id (:_id user)]
    (if (or (= user-id (get-in other-user [:company-data :approver-id]))
            (some #{user-id} (get-in other-user [:company-data :manager-ids])))
      "approver")))

(defn run
  "Returns specific roles that represent the relation between user and other-user"
  [{:keys [user other-user]}]
  (->> [(approver user other-user)]
       (filter identity)
       (result/success)))
