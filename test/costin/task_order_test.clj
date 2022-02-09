(ns costin.task-order-test
  (:require [costin.task-order :refer :all]
            [clojure.spec.gen.alpha :as genspec]
            [clojure.test :refer :all]))

(deftest transitive-closure-test
  (testing "Transitive closure")
  (is (= (find-transitive-closure #{"abc" "def" "ghi"} {"abc" #{"def"} "def" #{"ghi"}}) [#{"ghi"} #{"def"} #{"abc"}])))
