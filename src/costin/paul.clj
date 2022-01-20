(ns costin.paul
  (:gen-class))

(defn between [min x max]
  (and (<= min x) (<= x max)))

(defn normalize-character [c]
  "Uppercases character if alphabetic, nil otherwise"
  (cond
    (between (int \A) (int c) (int \Z)) c
    ; input = \a + x
    ; x = input - \a
    ; result = \A + x
    ; result = \A + input - \a
    (between (int \a) (int c) (int \z)) (char (+ (int c) (- (int \A) (int \a))))))

(defn encode-letter
  "Encodes individual letters with Paul cypher. Takes the previous letter and
  the one we want encoded."
  [previous-letter next-letter]
  (let [e (+ 1 (- (int previous-letter) (int \A)) (int next-letter))]
    (if (<= e (int \Z))
      (char e)
      (char (- e (- (int \Z) (int \A)) 1)))))

(defn encode
  "Encodes the input with the Paul cypher"
  [input]
  (let [s (seq input)]
    (nth
     (reduce (fn [[previous-letter acc]
                  next]
               (let [next-normalized (normalize-character next)]
                 (if (nil? next-normalized)
                   ; next is not a letter, just append it
                   [previous-letter (clojure.string/join [acc next])]
                   ; next-normalized is the uppercased letter, it will become
                   ; the previous letter
                   [next-normalized
                    (if (nil? previous-letter)
                      ; next-normalized is the first letter of the string,
                      ; just add it
                      (clojure.string/join [acc next-normalized])
                      ; encode next-normalized and add it
                      (clojure.string/join
                       [acc (encode-letter previous-letter next-normalized)]))])))
             [nil ""]
             s)
     1)))
