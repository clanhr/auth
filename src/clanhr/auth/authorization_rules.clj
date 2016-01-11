(ns clanhr.auth.authorization-rules
  (:require [result.core :as result]))

(def rules
  {:notifications-access ["admin" "hrmanager" "manager" "user" ""]
   :reports-access ["admin" "hrmanager" "manager"]
   :can-manage-absences ["admin" "hrmanager"]
   :delete-user ["admin" "hrmanager"]})

(defn run
  "Check if role can perform action"
  [action roles]
  (let [roles (if (or (nil? roles) (empty? roles))
                [""]
                roles)]
    (if (some (set roles) (action rules))
      (result/success {:roles roles})
      (result/forbidden))))
