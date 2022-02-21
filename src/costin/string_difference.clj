(ns costin.string-difference
  (:gen-class))

(defn diff
  "If B is made of a permutaton of letters of A plus one additional character, returns that extra character"
  [A B]
  (let [a-occurence
        (reduce
         (fn [acc-map next-letter]
           (let [letter-occurence (inc (get acc-map next-letter 0))]
             (assoc acc-map next-letter letter-occurence)))
         {}
         (seq A))
        extra-letter
        (reduce
         (fn [b-acc-map next-letter] (let [b-letter-occurence (inc (get b-acc-map next-letter 0))
                                           a-letter-occurence (get a-occurence next-letter 0)]
                                       (if (> b-letter-occurence a-letter-occurence)
                                         (reduced next-letter)
                                         (assoc b-acc-map next-letter b-letter-occurence))))
         {}
         (seq B))]
    (if (char? extra-letter)
      extra-letter
      (throw (Exception. "Could not find extra letter")))))

