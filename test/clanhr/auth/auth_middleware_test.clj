(ns clanhr.auth.auth-middleware-test
  (:require [clanhr.auth.auth-middleware :as auth-middleware]
            [clanhr.auth.core :as auth])
  (:use clojure.test
        ring.mock.request))

(deftest auth-test

  (let [response-hash {:status 200 :headers {} :body "Foo"}
        data {:user "bob_the_builder"
              :password "spoon"}
        token (auth/token-for data)]
    (testing "should pass on x-clanhr-auth-token header"
      (letfn [(handler [request]
                response-hash)]
        (let [req (request :get "/")
              response ((auth-middleware/run handler)
                        (header req "x-clanhr-auth-token" token))]
          (is (= 200
                 (:status response))))))

    (testing "should pass on auth-token header"
      (letfn [(handler [request]
                response-hash)]
        (let [req (request :get "/")
              response ((auth-middleware/run handler)
                        (header req "auth-token" token))]
          (is (= 200
                 (:status response))))))

    (testing "should pass on token query param"
      (letfn [(handler [request]
                response-hash)]
        (let [req (assoc (request :get "/") :query-params {"token" token})
              response ((auth-middleware/run handler) req)]
          (is (= 200
                 (:status response))))))

    (testing "should pass an api_key as query param"
      (letfn [(handler [request]
                response-hash)]
        (let [req (assoc (request :get "/") :query-params {"api_key" token})
              response ((auth-middleware/run handler) req)]
          (is (= 200
                 (:status response))))))

    (testing "should pass system tokens"
      (letfn [(handler [request]
                (is (true? (get-in request [:principal :system])))
                response-hash)]
        (let [token (auth/system-token)
              req (assoc (request :get "/") :query-params {"token" token})
              response ((auth-middleware/run handler) req)]
          (is (= 200 (:status response))))))

    (testing "should fail"
      (letfn [(handler [request]
                response-hash)]
        (let [response ((auth-middleware/run handler) (request :get "/"))]
          (is (= 401
                 (:status response))))))))

(deftest extract-data-test
  (let [data {:user {:name "bob_the_builder"
                     :email "bubu@mail.com"
                     :account-id "account_id"
                     :account "account"
                     :user-id "user_id"}
              :password "spoon"}
        token (auth/token-for data)
        result-valid (auth-middleware/valid? token)
        result (auth-middleware/add-principal {} result-valid "some-token")]
    (is (= (get-in data [:user :user-id]) (get-in result [:principal :user-id])))
    (is (= (get-in data [:user :account]) (get-in result [:principal :account])))
    (is (= (get-in data [:user :account-id]) (get-in result [:principal :account-id])))
    (is (= (get-in data [:user :email]) (get-in result [:principal :email])))))
