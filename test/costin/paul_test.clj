(ns costin.paul-test
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.spec.gen.alpha :as genspec]
   [clojure.test :refer :all]
   [costin.paul :refer :all]))

(deftest blank-encode
  (testing "Encoding nothing")
  (let [result (encode "")]
    (is (= result ""))))

(deftest letter-encode
  (testing "Encoding single letter")
  ; uses the :post condition of decode-letter
  (let [g (spec/gen (spec/and char? #(between (int \A) (int %) (int \Z))))]
    (decode-letter (genspec/generate g) (genspec/generate g))))

(deftest string-encode
  (testing "Encoding string")
  (let [hello-result (encode "hello")
        newsletter-result (encode "newsletter")]
    (is (= hello-result "HMQXA"))
    (is (= newsletter-result "NSBPEQYNYW"))))

(deftest pre-text-encode
  (testing "Encoding with text before alphabetic")
  (let [result (encode "1 hug")]
    (is (= result "1 HCB"))))

(deftest blank-decode
  (testing "Decoding nothing")
  (let [result (decode "")]
    (is (= result ""))))

(deftest pre-text-decode
  (testing "Decoding with text before alphabetic")
  (let [result (decode "1 HCB")]
    (is (= result "1 HUG"))))

(deftest string-decode
  (testing "Decoding string")
  (let [hello-result (decode "HMQXA")]
    (is (= hello-result "HELLO"))))
