(ns clanhr.auth.user-validator
  (require [clanhr.auth.core :as auth]
           [clanhr.result.core :as result]))

(defn email
  "Get the email from the token"
  [token]
  (let [principal (auth/principal token)]
    (:email principal)))

(defn run
  "Validate json web token"
  [token]
  (if token
    (let [parsed-token (auth/parse token)
          is-valid? (auth/valid? parsed-token)]
      (if is-valid?
        (result/success parsed-token)
        (result/failure)))
    (result/failure)))
