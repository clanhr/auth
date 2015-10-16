(ns clanhr.auth.user-has-access
  (:require [clanhr.auth.core :as auth]
            [clanhr.auth.authorization-rules :as authorization-rules]
            [clanhr-api.core :as clanhr-api]
            [result.core :as result]))

(defn get-roles
  [context]
  (<!! (clanhr-api/http-get {:service :directory-api
                             :path (str "/user/" (:user-id context) "/roles")
                             :token (:token context)})))

(defn run
  "Check if user is authorized to do some action"
  [context]
  (result/enforce-let [roles (get-roles context)]
    (authorization-rules/run (:action context)
                             roles)))
