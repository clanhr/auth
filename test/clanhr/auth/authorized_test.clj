(ns clanhr.auth.authorized-test
  (:require [result.core :as result]
            [clojure.core.async :refer [<!!]]
            [clanhr.auth.authorized :as auth])
  (:use clojure.test
        ring.mock.request))

(deftest user-has-access-test
  (testing "has access"
    (let [context {:get-user-roles-result (result/success {:roles ["hrmanager"]})
                   :action :notifications-access}
          result (<!! (auth/authorized? context))]
      (is (result/succeeded? result))
      (is (= ["hrmanager"] (:roles result)))))

  (testing "payment required"
    (let [context {:get-user-roles-result (result/payment-required)
                   :action :notifications-access}
          result (<!! (auth/authorized? context))]
      (is (result/failed? result))
      (is (result/payment-required? result))))

  (testing "do not have access"
    (let [context {:get-user-roles-result (result/success {:roles ["bubu-role"]})
                   :action :notifications-access}
          result (<!! (auth/authorized? context))]
      (is (result/forbidden? result)))))

(deftest specific-user-has-access-test
  (testing "has access"
    (let [context {:user {:system {:roles ["hrmanager"]}}
                   :action :notifications-access}
          result (<!! (auth/authorized? context))]
      (is (result/succeeded? result))
      (is (= ["hrmanager"] (:roles result)))))

  (testing "payment required"
    (let [context {:user {:system {:roles ["hrmanager"]}}
                   :get-user-roles-result (result/payment-required)
                   :action :notifications-access}
          result (<!! (auth/authorized? context))]
      (is (result/failed? result))
      (is (result/payment-required? result))))

  (testing "do not have access"
    (let [context {:user {:system {:roles ["waza"]}}
                   :action :notifications-access}
          result (<!! (auth/authorized? context))]
      (is (result/forbidden? result)))))

(deftest get-url-test
  (is (= (auth/get-url {:user-id "1"})
         "/user/1/roles"))
  (is (= (auth/get-url {:user-id "1" :other-user-id "2"})
         "/user/1/roles?other=2")))
