(ns clanhr.auth.user-validator-test
  (:use clojure.test)
  (:require [clanhr.auth.user-validator :as validate-token]
            [result.core :as result]
            [clanhr.auth.core :as auth]))

(deftest validate-user-token-test
  (let [data {:user "bob the builder"
              :email "bob@mail.com"
              :password "spoon"}
        token (auth/token-for data)]
    (testing "should succeeded"
      (let [validated-result (validate-token/run token)]
        (is (result/succeeded? validated-result))
    (testing "should failed"
      (let [another-token "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
            validate-result (validate-token/run another-token)]
        (is (result/failed? validate-result))))
    (testing "invalid data"
      (let [another-token "�"
            result (validate-token/run another-token)]
        (is (result/failed? result))
        (is (result/exception? result))))))))
