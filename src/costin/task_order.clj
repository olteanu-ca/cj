(ns costin.task-order
  (:gen-class)
  (:require
    [clojure.set :as set]
    [clojure.spec.alpha :as spec]
    [clojure.spec.test.alpha :as stest]))


(defn order-tasks
  "forall 'a type,
  tasks : #{'a} all the tasks we have
  relations : {'a #('a)} the key, representing a task, depends on all the given values
  if successful, return [#{'a}] a list of how the tasks should be run; first run all tasks in the first element of the vector, then the second, then the third,...
  if unsuccessful, return #{'a} a set of the tasks that couldn't be run"
  [tasks relations]
  (loop [unordered-tasks tasks
         ordered-tasks []
         result []]
    (if (empty? unordered-tasks)
      result
      (let [next-steps (group-by
                         (comp empty? #(set/difference % ordered-tasks) (partial get relations))
                         unordered-tasks)
            next-ordered-tasks (set (get next-steps true))]
        (if (= next-ordered-tasks ordered-tasks)
          unordered-tasks
          (recur (set (get next-steps false))
                 (set/union ordered-tasks next-ordered-tasks)
                 (conj result next-ordered-tasks)))))))


(defn validate-tasks
  "Validates that the tasks have been ordered correctly"
  [[tasks relations ordered-tasks]]
  (first
    (reduce (fn [[is-valid remaining-tasks] next-task-set]
              (if is-valid
                [(reduce (fn [is-valid next-task]
                           (and is-valid
                                (not (contains? remaining-tasks next-task))
                                (every? (comp not (partial contains? remaining-tasks)) (get relations next-task))))
                         is-valid
                         next-task-set)
                 (set/difference remaining-tasks next-task-set)]
                [false nil]))
            [true tasks]
            ordered-tasks)))


(spec/fdef order-tasks
           :args (fn [args]
                   (and
                     (set? (first args))
                     (spec/conform (spec/every-kv (partial contains? (first args)) #(set/subset? % (first args))) (second args))))
           ; FIXME Can't figure out why ret doesn't do anything regardless of what you put in it. fn doesn't work either, probably because :ret doesn't
           :ret (spec/or :failure set? :success (spec/and vector? (spec/every set?)))
           :fn #(let [[tasks relations] (:args %)
                      ordered-tasks (val (:ret %))
                      in-tasks (fn [s] (set/subset? s tasks))]
                  (case (key (:ret %))
                    :failure (spec/conform in-tasks ordered-tasks)
                    :success (spec/conform validate-tasks [tasks relations ordered-tasks])))) ; only basic property, not a proper check that the result really checks out

(stest/instrument `order-tasks)
