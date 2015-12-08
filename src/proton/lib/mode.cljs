(ns proton.lib.mode)

(defonce modes (atom {}))
(defonce active-mode (atom {}))

(defn set-active-for [editor] ())

(defn define-mode [name & options]
  (swap! modes assoc-in [name] (apply hash-map options)))

(defn define-keybindings [name keymap]
  (swap! modes assoc-in [name :mode-keybindings] keymap))

(defn map-modes-with [key] (map #(vector (first %) (get-in (second %) [key])) (map identity @modes)))

(defn filter-mode-by-key-val [key val]
  (filter #(or (some #{val} (second %)) (= (second %) val)) (map-modes-with key)))

(defn find-mode-by-grammar [grammar]
  (if-let [filtered (first (filter-mode-by-key-val :atom-grammars grammar))]
   (first filtered) nil))

(defn get-mode-keybindings [editor]
  (if-let [mode-name (find-mode-by-grammar (.-name (.getGrammar editor)))]
    (get-in @modes [mode-name :mode-keybindings])
    nil))
