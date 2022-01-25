(ns costin.tictactoe-test
  (:require
    [clojure.test :refer :all]
    [costin.tictactoe :refer :all]))


(deftest form-line-t
  (testing "Test for form-line")
  (let [x (forms-line (seq [:o :o :o]))]
    (is (= x :o))))


(deftest diagonal-test
  (testing "Diagonal top left - bottom right")
  (let [board [[:x :o :x]
               [:o :x :o]
               [:o :x :x]]
        w (winner board)]
    (is (= :x w))))


(deftest diagonal-test-2
  (test "Diagonal bottom left - top righ")
  (let [board [[:x :o :x]
               [:o :x :o]
               [:x :o :o]]
        w (winner board)]
    (is (= :x w))))


(deftest horizontal-test
  (testing "Horizontal")
  (let [board [[:o :o :o]
               [:o :x :x]
               [nil :x :x]]
        w (winner board)]
    (is (= :o w))))


(deftest vertical-test
  (testing "Vertical")
  (let [board [[:o :x :o]
               [:o :x :x]
               [:o :o :x]]
        w (winner board)]
    (is (= :o w))))


(deftest draw-test
  (testing "No winner")
  (let [board [[:x :x :o]
               [:o :o :x]
               [:x :x :o]]
        w (winner board)]
    (is (= :draw w))))


(deftest invalid-length-test
  (testing "Invalid input test")
  (let [board [[:x nil :o]
               [:x :o nil]
               [:o nil]]
        w (winner board)]
    (is (nil? w))))
