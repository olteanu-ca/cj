(ns costin.task-order
  "Our task is to have the computer take an set of tasks and the dependencies between
  them and deliver an ordering for them. There are 4 ways we could have done this:
  1. Use an SMT program/constraint solver through its API. Not a great idea since only
  Z3 has a kind of documented API out of the SMTs. We didn't do enough research on
  constraint solvers; maybe Choco Solver was actually the best solution.
  2. Use an SMT program/constraint solver by describing what we want in SMT2. A good
  idea, since SMT2 is standardized and has more functionality than described in the
  problem statement — no doubt the extra stuff would be very useful if our tasks
  were more complex. But since we want to make this easy to run on system that don't
  have any such programs already installed, we would prefer a clojure solution.
  3. DIY program. We did this since the problem is really easy and not much more
  effort than using a library. It would be a very bad idea if we would want to
  extend it to cover integer constraints and the like.
  4. Use the clojure datalog library. We didn't choose this because it seemed rather
  old and probably not worth the effort for this easy program"
  (:gen-class)
  (:require
    [clojure.set :as set]
    [clojure.spec.alpha :as spec]))


(defn order-tasks
  "forall 'a type,
  relations : {'a #('a)} the key, representing a task, depends on all the given values
  if successful, return [#{'a}] a list of how the tasks should be run; first run all
  tasks in the first element of the vector, then the second, then the third, ...
  if unsuccessful, return #{'a} a set of the tasks that couldn't be run"
  [relations]
  (loop [unordered-tasks (keys relations)
         ordered-tasks []
         result []]
    (if (empty? unordered-tasks)
      result
      (let [next-steps (group-by
                         (comp empty? #(set/difference % ordered-tasks)
                               (partial get relations))
                         unordered-tasks)
            next-ordered-tasks (set (get next-steps true))]
        (if (= next-ordered-tasks ordered-tasks)
          unordered-tasks
          (recur (set (get next-steps false))
                 (set/union ordered-tasks next-ordered-tasks)
                 (conj result next-ordered-tasks)))))))


(defn validate-tasks
  "Validates that the tasks have been ordered correctly"
  [tasks relations ordered-tasks]
  (first
    (reduce (fn [[is-valid remaining-tasks] next-task-set]
              (if is-valid
                [(reduce (fn [is-valid next-task]
                           (and is-valid
                                (not (contains? remaining-tasks next-task))
                                (every? (comp not (partial contains? remaining-tasks))
                                        (get relations next-task))))
                         is-valid
                         next-task-set)
                 (set/difference remaining-tasks next-task-set)]
                [false nil]))
            [true tasks]
            ordered-tasks)))


(spec/fdef order-tasks
           :args (fn [[relations]]
                   (set/subset? (set (flatten (vals relations))) (set (keys relations))))
           :ret (spec/or :failure set? :success (spec/and vector? (spec/every set?)))
           :fn #(let [[tasks relations] (:args %)
                      ordered-tasks (val (:ret %))
                      in-tasks (fn [s] (set/subset? s tasks))]
                  (case (key (:ret %))
                    :failure (spec/conform in-tasks ordered-tasks)
                    :success (validate-tasks tasks relations ordered-tasks))))
