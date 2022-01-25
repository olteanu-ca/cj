(ns costin.backspace
  (:gen-class)
  (:require
    [clojure.string :as string]))


(defn eliminate-last-char
  "Removes the last character of a string"
  [s]
  (let [s-len (count s)]
    (if (zero? s-len)
      s
      (subs s 0 (dec s-len)))))


(defn apply-bs
  "Interprets its parameter as a string representing user input where # stands
  for backspace. Returns the input if backspace removes the previous character
  in the input"
  [user-input]
  (let [s (seq user-input)]
    (reduce
      (fn [acc
           next]
        (if (= next \#)
          (eliminate-last-char acc)
          (string/join [acc next])))
      ""
      s)))
