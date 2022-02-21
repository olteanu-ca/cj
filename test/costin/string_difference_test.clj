(ns costin.string-difference-test
  (:require [costin.string-difference :refer :all]
            [clojure.test :refer :all]))


(deftest blank-diff
  (testing "Encoding random text")
  ; uses the :post condition of decode
  (is (= (diff "" "j") \j)))

(deftest diffs
  (testing "Differences")
  (is (and
       (= (diff "abc" "xcab") \x)
       (= (diff "xxyyzz" "xzyfyxz") \f)
       (= (diff "cccvv" "cvvcvc") \v))))
