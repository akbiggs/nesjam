(ns nesjam.world
  (:require [nesjam.tilemap :as tilemap]
            [nesjam.editor :as editor])
  (:use [nesjam.helpers]))

(defn create [width height]
  {:tilemap (tilemap/create 8 width height)
   :editor (editor/create)
   :edit-mode? true
   :objects nil})

(defn update-objects [elapsed-time user-input world]
  (for [object (:objects world)]
    ((:update object) elapsed-time user-input world object)))

(defn update [elapsed-time user-input world]
  (if->> world :edit-mode?
    (editor/edit! user-input)
    (update-objects elapsed-time user-input)))

(defn draw [world]
  (tilemap/draw (:tilemap world))
  (doseq [object (:objects world)]
    ((:draw object) object))
  (when (:edit-mode? world)
    (editor/draw world)))
