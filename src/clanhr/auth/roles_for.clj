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

(defn self-approver
  "If the user is the same as other-user and the user is approver of himself,
   returns the self-approver role"
  [user other-user]
  (if-let [user-id (:_id user)]
    (when (and (= user-id (:_id other-user))
             (some #{user-id} (get-in user [:company-data :manager-ids])))
      "self-approver")))

(defn get-roles
  [user other-user]
  (conj [(approver user other-user)]
         (self-approver user other-user)))

(defn run
  "Returns specific roles that represent the relation between user and other-user"
  [{:keys [user other-user]}]
  (->> (get-roles user other-user)
       (filter identity)
       (result/success)))
