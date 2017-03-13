(ns clanhr.auth.user-validator
  "This namespace is used in the `clanhr.auth.auth-middleware` and does only
   one thing, validates the given token"
  (:require [clanhr.auth.core :as auth]
            [result.core :as result]))

(defn email
  "Get the email from the token"
  [token]
  (let [principal (auth/principal token)]
    (:email principal)))

(defn run
  "Validate json web token"
  [token]
  (try
    (if token
      (let [parsed-token (auth/parse token)
            is-valid? (auth/valid? parsed-token)]
        (if is-valid?
          (result/success parsed-token)
          (result/failure)))
      (result/failure))
    (catch Exception e
      (result/exception e))))
