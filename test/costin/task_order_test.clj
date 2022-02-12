(ns costin.task-order-test
  (:require [costin.task-order :refer :all]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check.generators :as gen]
            [clojure.test :refer :all]))

(deftest no-tasks
  (testing "No tasks given")
  (is (= (order-tasks {}) [])))

(deftest task-order-test
  (testing "Basic task order test")
  (is (= (order-tasks {"abc" #{"def"} "def" #{"ghi"} "ghi" #{}}) [#{"ghi"} #{"def"} #{"abc"}])))

(def task-and-relation-generator
  (gen/fmap
   #(apply hash-map (apply concat %))
   (gen/bind
    (gen/not-empty (gen/set gen/small-integer)) ; could extend this to any equable
    (fn [task-set]
      (gen/set (gen/tuple
                (gen/elements task-set)
                ; why 3? because gen has bind and fmap, but not reduce (or HOF
                ; checking), so we can't weight the generation probabilities so
                ; that it's solvable. so we give max 3 and maybe it's solvable
                (gen/set (gen/elements task-set) {:max-elements 3})))))))

; running (println (gen/generate task-and-relation-generator)) looks ok

; FIXME: :failure :no-gen but why? even if I put a println at the beginning of :args
; to print its arguments it doesn't print anything
(stest/summarize-results (stest/check `order-tasks {:gen {`order-tasks task-and-relation-generator}}))
