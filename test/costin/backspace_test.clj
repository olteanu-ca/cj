(ns costin.backspace-test
  (:require [clojure.test :refer :all]
            [costin.backspace :refer :all]))

(deftest basic-backspace
  (testing "Basic backspace test")
  (let [result (apply-bs "abc#")]
    (is (= result "ab"))))

(deftest multiple-backspace
  (testing "Multiple backspaces")
  (let [result (apply-bs "abc###")]
    (is (= result ""))))

(deftest backspace-first
  (testing "Backspaces before text")
  (let [result (apply-bs "###abc")]
    (is (= result "abc"))))

(deftest middle-backspace
  (testing "Backspace in middle of text")
  (let [result (apply-bs "there###eir")]
    (is (= result "their"))))
