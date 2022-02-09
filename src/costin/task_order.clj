(ns costin.task-order
  (:gen-class)
  (:require
    [clojure.spec.alpha :as spec]
    [clojure.set :as set]))

(defn find-transitive-closure
  "elements : #{'a} all the tasks we have
   relations : {string #(string)} the key depends on all the given values, which must be in elements
   return : [#{string}] a list of how the tasks should be run; first run all tasks in the first element of the vector, then the second, then the third,..."
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
        (recur (set (get next-steps false))
               (set/union ordered-elements next-ordered-elements)
               (conj result next-ordered-elements)))))) ; TODO: check if the recur values are the same as the previous recursion; if they are, we've diverged
