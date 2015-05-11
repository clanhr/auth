(ns clanhr.auth.core
  "Handle auth tokens"
  (:require [environ.core :refer [env]]
            [clj-jwt.core  :refer :all]
            [clj-time.core :refer [now plus days]]))

(defn secret
  "Returns a secret based on environment variables"
  ([]
   (secret env))
  ([env]
   (or
     (:clanhr_auth_secret env)
     (:secret env)
     (throw (Exception. "Can't resolve auth token")))))

(defn build-claim
  "Build a claim"
  [user]
  {:iss user
   :exp (plus (now) (days 10))
   :iat (now)})

(defn token-for
  "Creates a token for a user"
  [args]
  (-> (build-claim (args :user))
      jwt
      (sign :HS256 (secret))
      to-str))

(defn parse
  "Parse token"
  [token]
  (-> token str->jwt))

(defn valid?
  "Check if token is valid"
  [token]
  (if token
    (verify token (secret))
    false))

(defn principal
  "Get the principal from the given token"
  [token]
  (if token
    (get-in token [:claims, :iss])
    false))
