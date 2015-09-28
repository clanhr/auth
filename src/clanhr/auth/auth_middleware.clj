(ns clanhr.auth.auth-middleware
  "Check if user is authenticated"
  (require [clanhr.auth.user-validator :as validator]
           [clanhr.reply.core :as reply]
           [ring.util.response :refer [get-header]]
           [result.core :as result]))

(defn valid?
  [token]
  (validator/run token))

(defn extract-token-from
  [context]
  (or
    (get-in context [:request :params :token])
    (get-header context "auth-token")
    (get-header context "x-clanhr-auth-token")))

(defn add-principal
  "Adds principal info to the request"
  [context result]
  (let [email (get-in result [:claims :iss :email])
        account (get-in result [:claims :iss :account])
        account-id (get-in result [:claims :iss :account-id])
        user-id (get-in result [:claims :iss :user-id])]
    (assoc context :principal {:email email
                               :account account
                               :account-id account-id
                               :user-id user-id})))

(defn run
  "Performs validation"
  [handler]
  (fn [context]
    (let [token (extract-token-from context)
          result (valid? token)]
      (if (result/succeeded? result)
        (handler (-> context
                     (add-principal result)))
        (reply/unauthorized)))))
