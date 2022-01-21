(ns costin.paul
  (:gen-class))

(defn between [min x max]
  (and (<= min x) (<= x max)))

(defn normalize-character
  "Uppercases character if alphabetic, nil otherwise"
  [c]
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
  (let [e (+ 1 (- (int previous-letter) (int \A)) (int next-letter))] ; adding 1 because A is 1st letter, not 0th
    (if (<= e (int \Z))
      (char e)
      (char (- e (- (int \Z) (int \A)) 1))))) ; there are (int Z) - (int A) + 1 characters between Z and A

(defn decode-letter
  "Decodes invidual letters with Paul cypher"
  [previous-letter next-letter]
  ; forall prev,
  ; enc(next) = 1 + (prev - A) + next
  ; x = 1 + (prev - A) + next
  ; x - 1 - (prev - A) = next
  ; dec(next) = next - 1 - prev + A
  (let [e (- (+ (int next-letter) (int \A)) 1 (int previous-letter))]
    (println (format "prev %s next %s result %s" previous-letter next-letter e))
    (char
     (if (>= e (int \A))
       e
       (+ e 1 (- (int \Z) (int \A)))))))

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

(defn decode
  "Decodes the input with the Paul cypher"
  [input]
  (let [s (seq input)]
    (nth
     (reduce (fn [[previous-letter acc]
                  next]
               (let [next-normalized (normalize-character next)]
                 (if (nil? next-normalized)
                   ; next is not a letter, just append it
                   [previous-letter (clojure.string/join [acc next])]
                   (if (nil? previous-letter)
                     [next-normalized (clojure.string/join [acc next-normalized])]
                     (let [decoded-next (decode-letter previous-letter next-normalized)]
                       [decoded-next (clojure.string/join [acc decoded-next])])))))
             [nil ""]
             s)
     1)))
