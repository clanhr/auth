(ns clanhr.auth.auth-middleware
  "Check if request is authenticated"
  (:require [clanhr.auth.user-validator :as validator]
            [clanhr.reply.core :as reply]
            [ring.util.response :refer [get-header]]
            [result.core :as result]))

(defn valid?
  [token]
  (validator/run token))

(defn extract-token-from
  "Extracts token from the request. Usually the token is present in one of the
   following headers `auth-token`, `x-clanhr-auth-token` or in the url given by
   `token` or `api_key`.
   A usecase where we might use the token in the url is when the user download
   excel files."
  [context]
  (or
    (get-header context "auth-token")
    (get-header context "x-clanhr-auth-token")
    (get-in context [:query-params "token"])
    (get-in context [:query-params "api_key"])))

(defn add-principal
  "Adds principal info to the request"
  [context result token]
  (let [email (get-in result [:claims :iss :email])
        account (get-in result [:claims :iss :account])
        account-id (get-in result [:claims :iss :account-id])
        user-id (get-in result [:claims :iss :user-id])
        system? (boolean (get-in result [:claims :iss :system]))]
    (assoc context :principal {:email email
                               :account account
                               :account-id account-id
                               :system system?
                               :user-id user-id}
                   :token token)))

(defn run
  "Performs validation"
  [handler]
  (fn [context]
    (let [token (extract-token-from context)
          result (valid? token)]
      (if (result/succeeded? result)
        (handler (-> context
                     (add-principal result token)))
        (reply/unauthorized)))))
