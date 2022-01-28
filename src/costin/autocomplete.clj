(ns costin.autocomplete
  (:gen-class)
  (:require
    [clojure.spec.alpha :as spec]
    [clojure.string :as string]))

(defn completes? [input full-string]
   (let [last-matched (reduce
                       (fn [input-idx next-char]
                         (cond
                           (= input-idx (count input)) input-idx
                           (= next-char (nth input input-idx)) (inc input-idx)
                           :not-matched input-idx))
                       0 (seq full-string))]
     (= last-matched (count input))))

(spec/fdef completes?
  :args #((and ((comp string? first) %) ((comp string? second) %)))
  :ret boolean?)
