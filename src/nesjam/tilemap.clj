(ns nesjam.tilemap
  (:require [nesjam.point :as point]
            [nesjam.tile :as tile]
            [nesjam.grid :as grid]))

(defn create [tilesize width height]
  {:grid (grid/create tilesize width height)
   :tiles nil})

(defn tile-at-index [index tilemap]
  (get (:tiles tilemap) index))

(defn operate-on-tile [op position tilemap]
  (let [grid (:grid tilemap)
        index (grid/worldpos-to-index position grid)
        tile-at-pos (tile-at-index index tilemap)]
    (update-in tilemap [:tiles]
               #(op index tile-at-pos %))))

(defn add-tile [worldpos tiletype tilemap]
  (let [grid (:grid tilemap)]
    (operate-on-tile
     (fn [index _ tiles]
       (assoc tiles
         index
         (tile/create tiletype
                      (grid/index-to-worldpos index grid)
                      (:square-size grid))))
     worldpos tilemap)))

(defn remove-tile [worldpos tilemap]
  (operate-on-tile (fn [index _ tiles] (dissoc tiles index))
                   worldpos tilemap))

(defn draw [tilemap]
  (doseq [contained-tile (vals (:tiles tilemap))]
    (tile/draw contained-tile)))
