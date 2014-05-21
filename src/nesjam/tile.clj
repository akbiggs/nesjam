(ns nesjam.tile
  (:require [nesjam.point :as point]
            [quil.core :as q]))

(defn create [tiletype position size & props]
  (let [props-map (apply hash-map props)]
    (merge props-map
     {:tiletype tiletype
      :position position
      :size size})))

(defn draw [tile]
  (let [{:keys [x y]} (:position tile)
        size (:size tile)]
    (q/rect x y size size)))
