(ns proton.lib.helpers
  (:require [clojure.string :as string :refer [upper-case lower-case]]
            [cljs.nodejs :as node]))

(def fs (node/require "fs"))
;; seperate map with overrides. 189 (underscore) kept getting resolved as '½' which we don't want.
(def char-code-override {189 "_"
                         9 "tab"})

(defn generate-div [text class-name]
  (let [d (.createElement js/document "div")]
    (set! (.-textContent d) text)
    (.add (.-classList d) class-name)
    d))

(defn read-file [path]
  (.readFileSync fs path #js {:encoding "utf8"}))

(defn is-file? [path]
  (try
    (.isFile (.lstatSync fs path))
    (catch js/Error e
      false)))

(defn extract-keyletter-from-event [event]
  (let [key-code (.. event -originalEvent -keyCode)
        key (if (nil? (char-code-override key-code))
                (.fromCharCode js/String key-code)
                (char-code-override key-code))
        shift-key (.. event -originalEvent -shiftKey)]

      (if shift-key
        (keyword (upper-case key))
        (keyword (lower-case key)))))

(defn extract-keycode-from-event [event]
  (.. event -originalEvent -keyCode))

(defn is-action? [tree sequence]
  (or
    (not (nil? (get-in tree (conj sequence :fx))))
    (not (nil? (get-in tree (conj sequence :action))))))

(defn console!
  ([s] (console! s nil))
  ([s key] (console! s key true))
  ([s key js-print]
   (let [k (if (nil? key) "proton" (str "proton:" key))
         s (str "["k"] " s)]
    (if js-print
      (.log js/console s)
      (println s)))))

(defn tree->html [tree]
  (->>
    (map (fn [element]
          (let [key (nth element 0)
                options (nth element 1)
                value (if (not (nil? (options :category)))
                          (options :category)
                          (if (not (nil? (options :title)))
                            (options :title)
                            (options :action)))]

            (str "<li class='flex-item'>[" (name key) "] ➜ " value "</li>")))
      (seq (dissoc tree :category)))
    (string/join " ")
    (conj [])
    (apply #(str "<p>Keybindings:</p><ul class='flex-container'>" % "</ul>"))))

(defn process->html [steps]
  (let [steps-html (map #(str "<tr><td class='process-step'>" (get % 0) "</td><td class='process-status'>" (get % 1) "</td></tr>") steps)]
    (str "<h2>Welcome to proton<h2><table>" (string/join " " steps-html) "</table>")))

(defn deep-merge
  "Deeply merges maps so that nested maps are combined rather than replaced.
  For example:
  (deep-merge {:foo {:bar :baz}} {:foo {:fuzz :buzz}})
  ;;=> {:foo {:bar :baz, :fuzz :buzz}}
  ;; contrast with clojure.core/merge
  (merge {:foo {:bar :baz}} {:foo {:fuzz :buzz}})
  ;;=> {:foo {:fuzz :quzz}} ; note how last value for :foo wins"
  [& vs]
  (if (every? map? vs)
    (apply merge-with deep-merge vs)
    (last vs)))

(defn deep-merge-with
  "Deeply merges like `deep-merge`, but uses `f` to produce a value from the
  conflicting values for a key in multiple maps."
  [f & vs]
  (if (every? map? vs)
    (apply merge-with (partial deep-merge-with f) vs)
    (apply f vs)))
