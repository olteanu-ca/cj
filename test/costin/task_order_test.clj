(ns costin.task-order-test
  (:require [costin.task-order :refer :all]
            [clojure.spec.gen.alpha :as genspec]
            [clojure.test :refer :all]))

(deftest task-order-test
  (testing "Basic task order test")
  (is (= (order-tasks #{"abc" "def" "ghi"} {"abc" #{"def"} "def" #{"ghi"}}) [#{"ghi"} #{"def"} #{"abc"}])))
