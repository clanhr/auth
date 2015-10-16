(ns clanhr.auth.authorization-rules
  (:require [result.core :as result]))

(def rules
  {:notifications-access [:admin :hrmanager :manager]})

(defn run
  "Check if role can perform action"
  [action roles]
  (result/presence (some (set roles) (action rules))))
