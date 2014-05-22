(ns nesjam.console
  (:require [nesjam.helpers :as helpers]
            [nesjam.point :as point]
            [nesjam.text-field :as text-field]

            [quil.core :as q]))

(defn create [activation-key]
  {:field (text-field/create (point/create 10 (- (q/height) 10))
                             100)
   :activation-key activation-key})

(defn active? [console]
  (get-in console [:field :enabled?]))

(defn activate [console]
  (assoc-in console [:field :enabled?] true))

(defn enter-command [in console]
  (update-in console [:field] #(text-field/update in %)))

(defn update [in console]
  (helpers/react*
   console
   (and (not (active? console))
        (= (:last-key-tapped in) (:activation-key console)))
   (activate)

   (active? console)
   (enter-command in)))

(defn draw [console]
  (text-field/draw (:field console)))