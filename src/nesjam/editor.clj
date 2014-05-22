(ns nesjam.editor
  (:require [nesjam.input :as input]
            [nesjam.tilemap :as tilemap]
            [nesjam.helpers :as helpers]
            [nesjam.serializer :as serializer]
            [nesjam.console :as console]

            [quil.core :as q]))

(defn create []
  {:tiletype 0
   :console (console/create \~)})

(defn- make-tile-op [op]
  (fn [position world]
    (update-in world [:tilemap]
               (fn [tilemap] (op position tilemap)))))

(def add-tile (make-tile-op
               #(tilemap/add-tile %1 (:tiletype @instance) %2)))

(def remove-tile (make-tile-op tilemap/remove-tile))

(defn- save-level! [in world editor]
  (let [filename (:filename editor)]
    (when (seq? filename))
      (serializer/serialize! filename (:tilemap world)))
  world)

(defn- load-level [name world]
  (let [loaded-level (serializer/deserialize name)]
    (if (nil? loaded-level)
      world
      (assoc world :tilemap loaded-level))))

(defn- start-editing-filename! [world]
  (swap! instance #(assoc % :changing-filename? true))
  world)

(defn- edit-filename! [in]
  (let [{:keys [last-key-tapped]} in
        filename (:filename @instance)
        filename-with-char-added (str filename last-key-tapped)]
    (swap!
     instance
     #(if (input/key-held? \backspace in)
        (assoc % :filename "")
        (case last-key-tapped
          \newline (assoc % :changing-filename? false)
          \backspace (assoc % :filename
                       (apply str (drop-last filename)))
          (assoc % :filename filename-with-char-added))))))

(defn- edit-world! [in world]
  (helpers/react*
   world

   (input/left-mouse-click?) (add-tile (:mouse-pos in))
   (input/right-mouse-click?) (remove-tile (:mouse-pos in))

   (input/key-tapped? \s in) (save-level! in)
   (input/key-tapped? \l in) (load-level (:filename @instance))
   (input/key-tapped? \r in) (start-editing-filename!)))

(defn edit! [in world]
  (if (:changing-filename? @instance)
    (do
      (edit-filename! in)
      world)
    (edit-world! in world)))

(defn draw [world]
  (q/push-style)

  (q/fill 255 0 0)
  (q/text "EDIT MODE" 2 10)

  (q/fill 0 0 255)
  (q/rect 2 (- (q/height) 17) 80 15)

  (q/fill 255)
  (let [{:keys [filename changing-filename?]} @instance
        filename (if changing-filename?
                   (helpers/add-cursor filename 1000)
                   filename)]
    (q/text filename 6 (- (q/height) 5)))

  (q/pop-style))
