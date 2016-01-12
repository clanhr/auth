(ns clanhr.auth.authorization-rules
  (:require [result.core :as result]))

(def rules
  {:notifications-access ["admin" "hrmanager" "manager" "user" "" "staff"]
   :reports-access ["admin" "hrmanager" "manager" "staff"]
   :can-manage-absences ["admin" "hrmanager" "staff"]
   :delete-user ["admin" "hrmanager" "staff"]})

(defn run
  "Check if role can perform action"
  [action roles]
  (let [roles (if (or (nil? roles) (empty? roles))
                [""]
                roles)]
    (if (some (set roles) (action rules))
      (result/success {:roles roles})
      (result/forbidden))))
