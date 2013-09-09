(ns a-star.core (:gen-class ))

(set! *warn-on-reflection* true)
(defrecord Location [x y])

(defrecord Node [location parent h])

(defn x [node]
  (:x (:location node)))

(defn y [node]
  (:y (:location node)))

(defn get-children [parent]
  (let [parent-x (x parent)
        parent-y (y parent)]
    [(Location. (inc parent-x) parent-y)
     (Location. (dec parent-x) parent-y)
     (Location. parent-x (inc parent-y))
     (Location. parent-x (dec parent-y))]))

(defn distance [end node]
  (let [dx (- (:x node) (:x end))
        dy (- (:y node) (:y end))]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))

(defn find-path [node path]
  (let [new-path (cons (:location node) path)]
    (cond (:parent node)
      (recur (:parent node) new-path)
      :else new-path)))

(defn compy [a b]
  (let [c (compare (:h a) (:h b))]
    (if (not= c 0)
      c
      (let [x (compare (x a) (x b))]
        (if (not= x 0) x (compare (y a) (y b)))))))

(defn distance-from-end [end]
  (partial distance end))

(defn a-star [start end]
  (let [h (distance-from-end end) start-node (Node. start nil (h start))]
    (loop [open (sorted-set-by compy start-node)
           closed #{}]
      (if (seq open)
        (let [current (first open)]
          (if (= (:location current) end)
            (find-path current [])
            (let [next-locs (remove closed (get-children current))]
            (recur (into (disj open current) (map #(Node. % current (h %)) next-locs)) (conj closed (:location current))))))
        "No path found"))))

(defn -main []
  (let [start (Location. 0 0)
        end (Location. 200 200)]
    (time (a-star start end))))
