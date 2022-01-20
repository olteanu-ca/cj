(ns costin.paul-test
  (:require [clojure.test :refer :all]
            [costin.paul :refer :all]))

(deftest blank-encode
  (testing "Encoding nothing")
  (let [result (encode "")]
    (is (= result ""))))

(deftest letter-encode
  (testing "Encoding single letter")
  (let [result (encode "a")]
    (is (= result "A"))))

(deftest string-encode
  (testing "Encoding string")
  (let [result (encode "hello")]
    (is (= result "HMQXA"))))

(deftest pre-text-encode
  (testing "Encoding with text before alphabetic")
  (let [result (encode "1 hug")]
    (is (= result "1 HCB"))))
