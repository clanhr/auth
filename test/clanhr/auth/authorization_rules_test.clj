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
          (authorization-rules/run :notifications-access nil)))
    (is (result/succeeded?
          (authorization-rules/run :notifications-access "")))
    (is (result/succeeded?
          (authorization-rules/run :notifications-access ["admin" "hrmanager"])))
    (is (result/succeeded?
          (authorization-rules/run :deactivate-user ["admin" "hrmanager"])))
    (is (result/succeeded?
          (authorization-rules/run :change-expense-state ["expensesManager"])))
    (is (result/succeeded?
          (authorization-rules/run :can-auto-approve-expenses ["expensesManager"])))
    (is (result/succeeded?
          (authorization-rules/run :reports-access ["absencesManager"])))
    (is (result/succeeded?
          (authorization-rules/run :settings-access ["absencesManager"])))
    (is (result/succeeded?
          (authorization-rules/run :can-manage-absences ["absencesManager"])))
    (is (result/succeeded?
          (authorization-rules/run :change-absence-state ["absencesManager"]))))

  (testing "do not have access"
    (is (result/forbidden?
          (authorization-rules/run :notifications-access ["bubu-role"])))
    (is (result/forbidden?
          (authorization-rules/run :reports-access nil)))
    (is (result/forbidden?
          (authorization-rules/run :reports-access "")))
    (is (result/forbidden?
          (authorization-rules/run :deactivate-user ["manager" "" nil])))
    (is (result/forbidden?
          (authorization-rules/run :billing-actions-access ["absencesManager" "expensesManager"])))))
