(ns costin.myapp
  (:gen-class))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn forms-line [s]
  (let [first-elem (first s)
        equal-to-fst (fn [r] (every? #(= first-elem %) r))]
    (if (equal-to-fst (rest s))
      first-elem
      nil)))

(defn check-winner [board]
  (assert (= 3 (count board)))
  (doall (map (fn [line] (assert (= 3 (count line)))) board))
  (first (filter some? (concat
                        (map forms-line board)
                        (map forms-line (apply map vector board))
                        [(forms-line
                         (map (fn [idx] (nth (nth board idx) idx)) (range 3)))]
                        [(forms-line
                             (map (fn [idx] (nth (nth board idx) (- 2 idx))) (range 3)))]))))


(defn winner [board]
  "Takes a 3-length vector of 3-length vectors of :x, :o, and nil representing a
  tic-tac-toe board. Returns the winner (:o or :x) if there is one or :draw"
  (try
    (let [w (check-winner board)]
      (if (= nil w)
        :draw
        w))
    (catch java.lang.AssertionError ex nil)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)}))
