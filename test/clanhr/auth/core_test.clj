(ns clanhr.auth.core_test
  (:use clojure.test)
  (:require [clanhr.auth.core :as auth]))

(deftest generate-token-test
  (testing "generation"
    (let [data {:user {:name "bob_the_builder" :account "wejwq"} :password "spoon"}
          token (auth/token-for data)]
      (is token)
      (testing "verify")
        (let [result (auth/parse token)]
          (is result)
          (is (auth/valid? result))
          (is (= (:user data) (auth/principal result)))))))

(deftest build-claim-test
  (let [user {:user "bob_the_builder"}]
    (is (= true
      (contains? (auth/build-claim user) :iss)))
    (is (= true
      (contains? (auth/build-claim user) :exp)))
    (is (= true
      (contains? (auth/build-claim user) :iat)))))

(deftest request-username-for-unexistent-token-test
  (let [fake-token "qwjkekwqbebdqwjebdqwjb"
        principal (auth/principal fake-token)]
    (is (not principal))))

(deftest throw-if-no-secret
  (is (thrown-with-msg? Exception #"Can't resolve auth token" (auth/secret {}))))

