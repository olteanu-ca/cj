(ns costin.tictactoe
  (:gen-class)
  (:require
    [clojure.spec.alpha :as spec]))


(defn elem-in-seq
  [sequence elem]
  (some? (some (partial = elem) sequence)))


(defn forms-line
  "Takes a seq and returns nil if the elements are unequal or the first element
  if they are"
  [s]
  {:pre [(spec/valid? seqable? s)]
   :post [(spec/valid? (spec/or :in (partial elem-in-seq s) :nil nil?) %)]}
  (let [first-elem (first s)
        equal-to-fst (fn [r] (every? #(= first-elem %) r))]
    (when (equal-to-fst (rest s))
      first-elem)))


(defn check-winner
  "Takes a square matrix of :x, :o, and nil representing a tic-tac-toe board.
  Returns the winner (:o or :x) if there is one, nil if there isn't, or throws
  an exception in case of invalid length in the input"
  [board]
  (let [board-size (count board)]
    (first (filter some? (concat
                           (map forms-line board) ; horizontal
                           (map forms-line (apply map vector board)) ; vertical
                           [(forms-line
                              (map
                                (fn [idx] (nth (nth board idx) idx))
                                (range board-size)))] ; top left to bottom right
                           [(forms-line
                              (map (fn [idx]
                                     (nth
                                       (nth board idx)
                                       (- board-size 1 idx)))
                                   (range board-size)))]))))) ; bottom left to top right

(defn winner
  "Takes a 3-length vector of 3-length vectors of :x, :o, and nil representing a
  tic-tac-toe board. Returns the winner (:o or :x) if there is one, :draw if
  there isn't, or nil if the input length is invalid"
  [board]
  {:pre [(spec/and (forms-line (cons (count board) (map count board))) (every? (partial elem-in-seq [:o :x :draw]) (flatten board)))]
   :post [(spec/valid? (partial elem-in-seq [:o :x :draw nil]) %)]}
  (try
    (let [w (check-winner board)]
      (if (nil? w)
        :draw
        w))
    (catch java.lang.IndexOutOfBoundsException _ nil)))
