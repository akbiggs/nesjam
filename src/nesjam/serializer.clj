(ns nesjam.serializer
  (:use [clojure.java.io]))

(defn serialize! [name data]
  (when-not (empty? name)
    (with-open [w (writer (str "resources/" name))]
      (.write w
              (with-out-str
                (binding [*print-dup* true] (prn data)))))))

(defn deserialize [name]
  (let [f (as-file (str "resources/" name))]
    (if (.exists f)
      (with-open [r (java.io.PushbackReader. f)]
        (read r))
      nil)))
