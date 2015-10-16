(ns clanhr.auth.user-has-access-test
  (:require [result.core :as result]
            [clojure.core.async :refer [<!!]]
            [clanhr.auth.user-has-access :as user-has-access])
  (:use clojure.test
        ring.mock.request))

(deftest user-has-access-test
  (testing "has access"
    (let [context {:get-user-roles-result (result/success {:roles [:hrmanager]})
                   :action :notifications-access}
          result (<!! (user-has-access/run context))]
      (is (result/succeeded? result))))

  (testing "do not have access"
    (let [context {:get-user-roles-result (result/success {:roles [:bubu-role]})
                   :action :notifications-access}
          result (<!! (user-has-access/run context))]
      (is (result/failed? result)))))
