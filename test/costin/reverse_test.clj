(ns costin.reverse-test
  (:require [costin.reverse :refer :all]
            [clojure.test :refer :all]))

(deftest parsing-test
  (testing "Parsing test")
  (is (= (form-expr "1 2 + 34 -") [1 2 \+ 34 \-])))

(deftest single-number
  (testing "Result of a single number")
  (is (= (rpol "1") 1)))

(deftest single-operation
  (testing "A single operation")
  (is (= (rpol "1 2 +  3"))))

; the example in the doc was wrong, 12 / 8 isn't 2
(deftest multiple-operation
  (testing "Multiple operations")
  (is (= (rpol "4 2 * 6 2 + + 8 /") 2)))
