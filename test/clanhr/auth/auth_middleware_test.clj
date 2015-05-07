(ns clanhr.auth.auth-middleware-test
  (:require [clanhr.auth.auth-middleware :as auth-middleware
            [clanhr.auth.core :as auth]])
  (:use clojure.test
        ring.mock.request))

(deftest auth-test

  (let [data {:user "bob the builder"
              :email "bob@mail.com"
              :password "spoon"}]
    (letfn [(handler [request]
              {:status 200
               :headers {"x-clanhr-auth-token": (auth/token-for data secret)}
               :body "Foo"})]
      (let [response ((auth-middleware/run handler) (request :get "/"))]
        (println "RESPONSE: " response)))))
