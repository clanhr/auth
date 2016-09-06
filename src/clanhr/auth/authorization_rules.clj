(ns clanhr.auth.authorization-rules
  (:require [result.core :as result]))

(def ^:const profile
  "Template role profiles for easy access"
  {:full-access ["admin" "hrmanager" "manager" "user" "" "staff"]
   :board-member-manager ["admin" "hrmanager" "manager" "staff"]
   :board-member ["admin" "hrmanager" "staff"]})

(def ^:const rules
  "Maps specific actions or zones to allowed roles"
  {:directory-access (:full-access profile)
   :notifications-access (:full-access profile)
   :reports-access (:board-member-manager profile)
   :can-manage-absences (:board-member profile)
   :can-manage-roles (:board-member profile)
   :can-manage-settings (:board-member profile)
   :can-manage-alerts (:board-member profile)
   :can-manage-holidays (:board-member profile)
   :change-absence-state (conj (:board-member profile) "approver")
   :settings-access (:board-member profile)
   :can-see-full-user-info (:board-member profile)
   :delete-user (:board-member profile)})

(defn run
  "Check if role can perform action"
  [action roles]
  (let [roles (if (or (nil? roles) (empty? roles))
                [""]
                roles)]
    (if (some (set roles) (action rules))
      (result/success {:roles roles})
      (result/forbidden))))
