(ns clanhr.auth.authorization-rules-test
  (:require [result.core :as result]
            [clanhr.auth.authorization-rules :as authorization-rules])
  (:use clojure.test
        ring.mock.request))

(deftest authorization-rules-test
  (testing "has access"
    (is (result/succeeded?
          (authorization-rules/run :notifications-access ["admin"])))
    (is (result/succeeded?
          (authorization-rules/run :notifications-access ["hrmanager"])))
    (is (result/succeeded?
          (authorization-rules/run :notifications-access ["manager"])))
    (is (result/succeeded?
          (authorization-rules/run :notifications-access ["hrmanager" "manager"])))
    (is (result/succeeded?
          (authorization-rules/run :notifications-access ["admin" "manager"])))
    (is (result/succeeded?
          (authorization-rules/run :notifications-access ["admin" "hrmanager"]))))

  (testing "do not have access"
    (is (result/forbidden?
          (authorization-rules/run :notifications-access ["bubu-role"])))))
