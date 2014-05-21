(ns nesjam.grid
  (:require [nesjam.point :as point]))

(defn create [square-size width height]
  {:square-size square-size
   :row-width (quot width square-size)})

(defn gridpos-to-index [gridpos grid]
  (let [{:keys [x y]} gridpos]
    (+ x (* (:row-width grid) y))))

(defn worldpos-to-gridpos [worldpos grid]
  (point/scalar-quot worldpos (:square-size grid)))

(defn worldpos-to-index [worldpos grid]
  (gridpos-to-index (worldpos-to-gridpos worldpos grid) grid))

(defn index-to-worldpos [index grid]
  (let [{:keys [square-size row-width]} grid
        col (mod index row-width)
        row (quot index row-width)]
    (point/create (* col square-size) (* row square-size))))

