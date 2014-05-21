(ns nesjam.core
  (:require [quil.core :as q]
            [nesjam.world :as world]
            [nesjam.hud :as hud]
            [nesjam.input :as input]
            [nesjam.mytime :as mytime]
            [nesjam.helpers :as helpers])
  (:gen-class :main true))

(defn swap-state! [state function]
  (swap! (q/state state) function))

(defn reset-state! [state value]
  (reset! (q/state state) value))

(defn update-state! [state & args]
  (let [namespace-symbol (symbol (str "nesjam." (name state)))
        update-fn (intern `~namespace-symbol 'update)
        partial-args (concat [update-fn] args)]
    (swap-state! state (apply partial partial-args))
    @(q/state state)))

(defn setup []
  (q/smooth)
  (q/background 200)
  (q/frame-rate 60)

  (q/set-state! :world (atom (world/create (q/width) (q/height)))
                :hud (atom (hud/create (q/width) (q/height)))
                :input (atom (input/create))
                :mytime (atom (mytime/create (helpers/now)))))

(defn update! []
  (update-state! :mytime (helpers/now))
  (def elapsed-time (:elapsed-time @(q/state :mytime)))

  (update-state! :input elapsed-time)
  (def user-input @(q/state :input))

  (update-state! :world elapsed-time user-input)
  (def world @(q/state :world))

  (update-state! :hud elapsed-time user-input world))

(defn draw []
  (update!)

  (q/background 128)

  (world/draw @(q/state :world))
  (hud/draw @(q/state :hud)))

(defn -main [& args]
  (q/sketch :title "Mr. Gimmick"
            :setup setup
            :draw draw
            :size [240 256]))

(-main)
