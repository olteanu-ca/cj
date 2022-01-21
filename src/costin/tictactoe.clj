(ns costin.tictactoe
  (:gen-class))

(defn forms-line
  "Takes a seq and returns nil if the elements are unequal or the first element
  if they are"
  [s]
  (let [first-elem (first s)
        equal-to-fst (fn [r] (every? #(= first-elem %) r))]
    (if (equal-to-fst (rest s))
      first-elem
      nil)))

(defn check-winner
  "Takes a 3-length vector of 3-length vectors of :x, :o, and nil representing a
  tic-tac-toe board. Returns the winner (:o or :x) if there is one, nil if there
  isn't, or throws an exception in case of invalid length in the input"
  [board]
  (assert (= 3 (count board)))
  (doall (map (fn [line] (assert (= 3 (count line)))) board))
  (first (filter some? (concat
                        (map forms-line board) ; horizontal
                        (map forms-line (apply map vector board)) ; vertical
                        [(forms-line
                          (map
                           (fn [idx] (nth (nth board idx) idx))
                           (range 3)))] ; top left to bottom right
                        [(forms-line
                          (map (fn [idx] (nth
                                          (nth board idx)
                                          (- 2 idx)))
                               (range 3)))])))) ; bottom left to top right


(defn winner
  "Takes a 3-length vector of 3-length vectors of :x, :o, and nil representing a
  tic-tac-toe board. Returns the winner (:o or :x) if there is one, :draw if
  there isn't, or nil if the input length is invalid"
  [board]
  (try
    (let [w (check-winner board)]
      (if (= nil w)
        :draw
        w))
    (catch java.lang.AssertionError ex nil)))
