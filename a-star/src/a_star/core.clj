(ns a-star.core (:gen-class))

(set! *warn-on-reflection* true)

(defrecord Location [x y])

(defrecord Node [location parent h])

(defn x [node]
    (:x (:location node)))

(defn y [node]
    (:y (:location node)))

(def all_nodes {})

(defn find-neighbours [parent]
    (let [parent-x (x parent)
          parent-y (y parent)]
        [(Node. (Location. (inc parent-x) parent-y) parent (atom nil))
         (Node. (Location. (dec parent-x) parent-y) parent (atom nil))
         (Node. (Location. parent-x (inc parent-y)) parent (atom nil))
         (Node. (Location. parent-x (dec parent-y)) parent (atom nil))]))


(defn distance [end node]
    (let [dx (- (x node) (:x end))
          dy (- (y node) (:y end))]
          (Math/sqrt (+ (* dx dx) (* dy dy)))))

(defn traverse-tree [node path]
    (let [new-path (cons {:x (x node) :y (y node)} path)]
        (cond (:parent node)
            (recur (:parent node) new-path)
            :else
                new-path)))

(defn a-star [start end heuristic get-children find-path]
    (let [start-node (Node. (Location. (:x start) (:y start)) nil (atom nil))
          end-location (Location. (:x end) (:y end))]
        (loop [ open (sorted-set-by heuristic start-node)
                closed []]
            (cond
                (seq open)
                    (let [current (first open)]
                        (cond
                            (= (:location current) end-location)
                                (find-path current [])
                            (some #{current} closed)
                                (recur (disj open current) closed)
                            :else
                                (recur (into (disj open current) (get-children current)) (conj closed current))))
                :else
                    "No path found"))))

(defn distance-from-end [end]
    (partial distance end))

(defn compy [h]
    (fn [a b]
        (if-not @(:h a)
            (reset! (:h a) (h a)))
        (if-not @(:h b)
            (reset! (:h b) (h b)))
        (let [c (compare @(:h a) @(:h b))
              x (compare (x a) (x b))
              y (compare (y a)(y b))]
            (if (not= c 0)
                c
                (if (not= x 0)
                    x
                    (if (not= y 0)
                        y
                        0))))))

(defn -main []
    (let [start {:x 0 :y 0} end {:x 5 :y 5}]
        (time (a-star start end (compy (distance-from-end end)) find-neighbours traverse-tree))))
