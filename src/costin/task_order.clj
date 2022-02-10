(ns costin.task-order
  (:gen-class)
  (:require
    [clojure.spec.alpha :as spec]
    [clojure.set :as set]))

(defn find-transitive-closure
  "forall 'a type,
  elements : #{'a} all the tasks we have
  relations : {'a #('a)} the key, representing a task, depends on all the given values
  if successful, return [#{'a}] a list of how the tasks should be run; first run all tasks in the first element of the vector, then the second, then the third,...
  if unsuccessful, return #{'a} a set of the tasks that couldn't be run"
    [elements relations]
  (loop [unordered-elements elements
         ordered-elements []
         result []]
    (if (empty? unordered-elements)
      result
      (let [next-steps (group-by
                        (comp (partial empty?) #(set/difference % ordered-elements) (partial get relations))
                        unordered-elements)
            next-ordered-elements (set (get next-steps true))]
        (if (= next-ordered-elements ordered-elements)
          unordered-elements
          (recur (set (get next-steps false))
                 (set/union ordered-elements next-ordered-elements)
                 (conj result next-ordered-elements)))))))

(spec/fdef find-transitive-closure
  :args (fn [args] (spec/and
                    (set? (:elements args))
                    ((spec/every-kv (partial contains? (:elements args)) #(set/subset? % (:elements args))) (:relations args))))
  :ret (spec/or :failure set? :success (spec/and vector? (spec/every set?)))
  :fn #(let [in-elements (fn [s] (set/subset? s ((comp first :args) %)))]
         (case (key (:ret %))
           :failure (spec/conform in-elements (val (:ret %)))
           :success (spec/conform (spec/every in-elements))))) ; only basic property, not a proper check that the result really checks out

