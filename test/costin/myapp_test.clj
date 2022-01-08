(ns costin.myapp-test
  (:require [clojure.test :refer :all]
            [costin.myapp :refer :all]))

(deftest form-line-t
  (let [x (forms-line (seq [:o :o :o]))]
    (is (= x :o))))

(deftest form-line-diagonal
  (let [board
        [[:x :o :x]
         [:o :x :o]
         [:x :o :o]]
        line (map (fn [idx] (nth (nth board idx) (- 2 idx))) (range 3))]
    (is (= :x (forms-line line)))))

(deftest diagonal-test
  (testing "diagonal")
  (let [board [[:x :o :x]
               [:o :x :o]
               [:o :x :x]]
        winner (check-winner board)]
    (is (= :x winner))))

(deftest diagonal-test-2
  (test "diagonal-2")
  (let [board [[nil nil :x]
               [nil :x nil]
               [:x nil nil]]
        winner (check-winner board)]
    (is (= :x winner))))

(deftest horizontal-test
  (testing "horizontal")
  (let [board [[:o :o :o]
               [:o :x :x]
               [nil :x :x]]
        winner (check-winner board)]
    (is (= :o winner))))

(deftest vertical-test
  (testing "vertical")
  (let [board [[:o :x :o]
               [:o :x :x]
               [:o :o :x]]
        winner (check-winner board)]
    (is (= :o winner))))

(deftest draw-test
  (testing "draw")
  (let [board [[:x :x :o]
               [:o :o :x]
               [:x :x :o]]
        winner (check-winner board)]
    (is (= nil winner))))
