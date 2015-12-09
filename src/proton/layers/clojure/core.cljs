(ns proton.layers.clojure.core
  (:require [proton.lib.mode :as mode]
    [proton.lib.atom :as atom-env :refer [set-grammar]])
  (:use [proton.layers.base :only [init-layer! get-initial-config get-keybindings get-packages get-keymaps]]))

(defmethod init-layer! :clojure
  [_ config]
  (mode/define-mode :clojure
     :atom-grammars ["Clojure"]
     :file-extensions [#"\.proton$"]
     :init (fn []
            (atom-env/set-grammar "Clojure")))
  (mode/define-keybindings :clojure
    {:t {:category "toggles"
         :p {:action "parinfer:toggleMode" :title "Toggle Parinfer Mode"}}})

  (println "init clojure"))

(defmethod get-initial-config :clojure [] [])

(defmethod get-keybindings :clojure
  []
  {})

(defmethod get-packages :clojure
  []
  [:Parinfer])

(defmethod get-keymaps :clojure
  []
  [])
