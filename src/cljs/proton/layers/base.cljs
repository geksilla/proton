(ns proton.layers.base)

(defn dispatch [layer-name]
  (keyword layer-name))

(defn package-dispatch [package-name]
 (keyword package-name))

;; multimethods to be used inside layer
(defmulti init-layer! dispatch)
(defmulti get-initial-config dispatch)
(defmulti get-packages dispatch)
(defmulti get-keybindings dispatch)
(defmulti get-keymaps dispatch)
(defmulti describe-mode dispatch)
(defmulti post-init package-dispatch)
(defmulti package-deps package-dispatch)
