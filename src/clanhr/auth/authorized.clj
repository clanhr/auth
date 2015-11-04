(ns clanhr.auth.authorized
  (:require [clanhr.auth.core :as auth]
            [clanhr.auth.authorization-rules :as authorization-rules]
            [clojure.core.async :refer [<! go]]
            [clanhr-api.core :as clanhr-api]
            [result.core :as result]))

(defn get-roles
  [context]
  (if-let [result (:get-user-roles-result context)]
    (go result)
    (clanhr-api/http-get {:service :directory-api
                          :path (str "/user/" (:user-id context) "/roles")
                          :token (:token context)})))

(defmulti authorized?
  (fn [context]
    (cond
      (:user context) :by-user)))

(defmethod authorized? :default [context]
  (go
    (result/enforce-let [result (<! (get-roles context))]
      (authorization-rules/run (:action context)
                               (:roles result)))))

