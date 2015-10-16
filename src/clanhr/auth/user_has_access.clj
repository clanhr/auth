(ns clanhr.auth.user-has-access
  (:require [clanhr.auth.core :as auth]
            [clanhr.auth.authorization-rules :as authorization-rules]
            [clojure.core.async :refer [<! go]]
            [clanhr-api.core :as clanhr-api]
            [result.core :as result]))

(defn get-roles
  [context]
  (if-let [result (:get-user-roles-result context)]
    result
    (clanhr-api/http-get {:service :directory-api
                          :path (str "/user/" (:user-id context) "/roles")
                          :token (:token context)})))

(defn run
  "Check if user is authorized to do some action"
  [context]
  (result/async-enforce-let [result (get-roles context)]
    (authorization-rules/run (:action context)
                             (:roles result))))
