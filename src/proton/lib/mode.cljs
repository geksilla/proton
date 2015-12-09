(ns proton.lib.mode
  (:require [cljs.nodejs :as nodejs]))

(def path (nodejs/require "path"))

(defonce modes (atom {}))
(defonce editors (atom []))

(defn- find-first [f coll] (first (filter f coll)))

(defn editor-grammar [editor] (.-name (.getGrammar editor)))

(defn file-extension [editor]
  "Returns file extenstion.
  For dotfiles returns file name."
   (let [parsed (.parse path (.getPath editor))
         extension (.-ext parsed)]
    (if (= extension "")
        (.-base parsed)
        extension)))

(defn is-mode-activated? [editor] (get editor :active))

(defn define-mode [name & options]
  (swap! modes assoc-in [name] (apply hash-map options)))

(defn define-keybindings [name keymap]
  (swap! modes assoc-in [name :mode-keybindings] keymap))

(defn map-modes-with [key] (map #(vector (first %) (get-in (second %) [key])) (map identity @modes)))

(defn filter-modes-by-key-val [key val]
  (filter #(or (some #{val} (second %)) (= (second %) val)) (map-modes-with key)))

(defn find-mode-by-grammar [grammar]
  (if-let [filtered (first (filter-modes-by-key-val :atom-grammars grammar))]
   (first filtered) nil))

(defn mode-extensions-filter [ext]
  (fn [coll]
    (if-let [extensions (second coll)]
     (let [reg? (not (empty? (filter #(re-find % ext) (filter regexp? extensions))))
           str? (not (nil? (some #{ext} (filter string? extensions))))]
      (or reg? str?))
     false)))

(defn find-mode-by-file-extension [extension]
  (filter (mode-extensions-filter extension) (map-modes-with :file-extensions)))

(defn get-mode-keybindings [editor]
  (if-let [mode-name (find-mode-by-grammar (editor-grammar editor))]
    (get-in @modes [mode-name :mode-keybindings])
    nil))

(defn get-available-mode [editor]
   (let [by-grammar (find-mode-by-grammar (editor-grammar editor))
         by-extension (find-mode-by-file-extension (file-extension editor))]))

(defn activate-mode [editor]
  (when-not (find-first #(= (get % :id)) @editors)
   (do
      (let [modes (get-available-mode editor)]))))
