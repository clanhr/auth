(ns clanhr.auth.authorized
  (:require [clanhr.auth.core :as auth]
            [environ.core :refer [env]]
            [clanhr.auth.authorization-rules :as authorization-rules]
            [clojure.core.async :refer [<! go]]
            [clanhr-api.core :as clanhr-api]
            [result.core :as result]))

(defn- directory-api-endpoint
  "Gets the current directory api endpoint"
  []
  (:clanhr-directory-api env))

(defn- mock-response
  "The mocked response, is available"
  [context]
  (:get-user-roles-result context))

(defn- token-present
  "Success if the token is present on the context"
  [context]
  (result/presence (or (:token context)
                       (mock-response context))
                   "no-token-on-context"))

(defn get-roles
  [context]
  (if-let [result (mock-response context)]
    (go result)
    (if-let [directory-api (directory-api-endpoint)]
      (clanhr-api/http-get {:service :directory-api
                            :path (str "/user/" (:user-id context) "/roles")
                            :token (:token context)})
      (go (result/success)))))

(defn get-token
  "Gets the token for a user"
  [context user]
  (or (:token context)
      (auth/token-for (get-in user [:system :email])
                      (get-in user [:_id])
                      (get-in user [:systen :account]))))

(defmulti authorized?
  (fn [context]
    (cond
      (:user context) :by-user)))

(defmethod authorized? :by-user [context]
  (go
    (let [roles (get-in context [:user :system :roles])
          context (assoc context :user-id (get-in context [:user :_id])
                                 :token (get-token context (get-in context [:user])))]
      (result/enforce-let [token? (token-present context)
                           auth-result (authorization-rules/run (:action context) roles)
                           subscription-enabled? (<! (get-roles context))]
        auth-result))))

(defmethod authorized? :default [context]
  (go
    (result/enforce-let [token? (token-present context)
                         result (<! (get-roles context))]
      (authorization-rules/run (:action context)
                               (:roles result)))))
