(ns clanhr.auth.authorized-test
  (:require [result.core :as result]
            [clojure.core.async :refer [<!!]]
            [clanhr.auth.authorized :as auth])
  (:use clojure.test
        ring.mock.request))

(deftest user-has-access-test
  (testing "has access"
    (let [context {:get-user-roles-result (result/success {:roles [:hrmanager]})
                   :action :notifications-access}
          result (<!! (auth/authorized? context))]
      (is (result/succeeded? result))))

  (testing "do not have access"
    (let [context {:get-user-roles-result (result/success {:roles [:bubu-role]})
                   :action :notifications-access}
          result (<!! (auth/authorized? context))]
      (is (result/unauthorised? result)))))
