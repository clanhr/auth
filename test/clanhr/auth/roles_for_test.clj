(ns clanhr.auth.roles_for_test
  (:use clojure.test)
  (:require [clanhr.auth.roles-for :as roles-for]
            [result.core :as result]))

(deftest approver-test
  (testing "from manager-ids"
    (let [user {:_id "1"}
          other-user {:company-data {:manager-ids ["1"]}}]
      (is (= "approver" (roles-for/approver user other-user)))
      (is (= nil (roles-for/approver other-user user)))))
  (testing "from approver-id"
    (let [user {:_id "1"}
          other-user {:company-data {:approver-id "1"}}]
      (is (= "approver" (roles-for/approver user other-user)))
      (is (= nil (roles-for/approver other-user user))))))

(deftest self-approver
  (testing "should be self approver"
    (let [user {:_id "1" :company-data {:manager-ids ["1"]}}
          other-user {:_id "1"}]
      (is (= "self-approver" (roles-for/self-approver user other-user)))))
  (testing "should not be self approver"
    (let [user {:_id "1" :company-data {:manager-ids ["2"]}}
          other-user {:_id "2"}]
      (is (= nil (roles-for/self-approver user other-user))))))

(deftest run-test
  (let [user {:_id "1"}
        other-user {:company-data {:manager-ids ["1"]}}
        result (roles-for/run {:user user :other-user other-user})]
    (is (result/succeeded? result))
    (is (= ["approver"] (:data result)))))

