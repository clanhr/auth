(ns clanhr.auth.authorized
  (:require [clanhr.auth.core :as auth]
            [clanhr.auth.authorization-rules :as authorization-rules]
            [clojure.core.async :refer [<! go]]
            [clanhr-api.core :as clanhr-api]
            [result.core :as result]))

(defn- mock-response
  "The mocked response, is available"
  [context]
  (:get-user-roles-result context))

(defn get-roles
  [context]
  (if-let [result (mock-response context)]
    (go result)
    (clanhr-api/http-get {:service :directory-api
                          :path (str "/user/" (:user-id context) "/roles")
                          :token (:token context)})))

(defmulti authorized?
  (fn [context]
    (cond
      (:user context) :by-user)))

(defmethod authorized? :by-user [context]
  (go
    (let [roles (get-in context [:user :system :roles])]
      (authorization-rules/run (:action context) roles))))

(defn- token-present
  "Success if the token is present on the context"
  [context]
  (result/presence (or (:token context)
                       (mock-response context))
                   "no-token-on-context"))

(defmethod authorized? :default [context]
  (go
    (result/enforce-let [token? (token-present context)
                         result (<! (get-roles context))]
      (authorization-rules/run (:action context)
                               (:roles result)))))

