(ns costin.reverse
  (:gen-class))

(defn char-digit-to-int
  [c]
  (- (int c) (int \0)))

(defn form-expr
  [input]
  (loop [i 0
         exprs []
         state nil] ; nil = whitespace, number = partially parsed number
    (if (= (count input) i)
      (if (nil? state) exprs (conj exprs state))
      (let [next-char (nth input i)]
        (cond
          (= next-char \space) (if (nil? state)
                                 (recur (inc i) exprs nil)
                                 (recur (inc i) (conj exprs state) nil))
          (<= (int \0) (int next-char) (int \9)) (let [next-digit (char-digit-to-int next-char)]
                                                   (if (nil? state)
                                                     (recur (inc i) exprs next-digit)
                                                     (recur (inc i) exprs (+ (* state 10) next-digit))))
          :else (recur (inc i) (conj exprs next-char) nil)))))) ; it's an operation

; easier than converting to a string and using read-string
(defn convert-operation [op]
  (case op
    \+ +
    \- -
    \* *
    \/ /))

(defn rpol
  [input]
  (first
   (reduce (fn [[total saved-numbers] next]
             (if (number? next)
               [(if (nil? total) next total) (cons next saved-numbers)]
               (let [[arg2 arg1 & rest-saved-numbers] saved-numbers
                     op-result ((convert-operation next) arg1 arg2)]
                 [op-result (cons op-result rest-saved-numbers)])))
           [nil nil]
           (form-expr input))))
