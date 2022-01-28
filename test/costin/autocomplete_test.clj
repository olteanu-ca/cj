(ns costin.autocomplete-test
  (:require [costin.autocomplete :refer :all]
            [clojure.test :refer :all]))

(deftest single-char
  (testing "Single character")
  (is (completes? "a" "abc")))

(deftest trivial-autocomplete
  (testing "Same autcomplete as target")
  (is (completes? "abc" "abc")))

(deftest using-autocomplete
  (testing "Valid autocomplete")
  (is (completes? "ll" "Hello")))

(deftest negative-using-autocomplete
  (testing "Invalid autocomplete")
  (is (not (completes? "la" "Hello"))))
