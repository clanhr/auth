(ns clanhr.auth.auth-middleware-test
  (require [clanhr.auth.auth-middleware :as auth-middleware]
           [clanhr.auth.core :as auth])
  (use clojure.test
        ring.mock.request))

(deftest auth-test

  (let [response-hash {:status 200 :headers {} :body "Foo"}
        data {:user "bob_the_builder"
              :password "spoon"}
        token (auth/token-for data)]
    (testing "should pass"
      (letfn [(handler [request]
                response-hash)]
        (let [req (request :get "/")
              response ((auth-middleware/run handler)
                        (header req "x-clanhr-auth-token" token))]
          (is (= 200
                 (:status response))))))

    (testing "should fail"
      (letfn [(handler [request]
                response-hash)]
        (let [response ((auth-middleware/run handler) (request :get "/"))]
          (is (= 401
                 (:status response))))))))

(deftest extract-data-test
  (let [data {:user "bob_the_builder"
              :password "spoon"
              :email "bubu@mail.com"
              :account "account"
              :user-id "user_id"}
        token (auth/token-for data)
        result-valid (auth-middleware/valid? token)
        result (auth-middleware/add-principal {} result-valid)]
    (is (get-in result [:principal :user-id]))))
