(ns nesjam.text-field
  (:require [nesjam.helpers :as helpers]
            [nesjam.input :as input]
            [nesjam.point :as point]

            [quil.core :as q])
  (:use [nesjam.helpers :only [defn-opts]]))

(defn-defaults create [pos width {text ""
                                  auto-focus false
                                  bg-color [0 0 255]
                                  text-color [255 255 255]}]
  {:position pos
   :size (point/create width 50)
   :text text
   :editing? auto-focus
   :bg-color bg-color
   :text-color text-color})

(defn edit-text [in field]
  (let [{:keys [text]} field
        {:keys [last-key-tapped]} in]
    (merge
     field
     (if (input/key-held? \backspace in)
       {:text ""}
       (case last-key-tapped
         \backspace {:text (apply str (drop-last text))}
         \newline {:editing? false}
         {:text (str text last-key-tapped)})))))

(defn update [in field]
  (helpers/react field (:editing? field) (edit-text in)))

(defn draw [field]
  (let [{:keys [text bg-color text-color position size]} field]
    (apply q/fill bg-color)
    (q/rect (:x position) (:y position) (:x size) (:y size))

    (apply q/fill text-color)
    (q/text text (+ (:x position) 2) (+ (:y position) 2))))