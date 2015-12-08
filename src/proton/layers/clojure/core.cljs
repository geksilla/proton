(ns proton.layers.clojure.core
  (:require [proton.lib.mode :as mode])
  (:use [proton.layers.base :only [init-layer! get-initial-config get-keybindings get-packages get-keymaps]]))

(defmethod init-layer! :clojure
  [_ config]
  (mode/define-mode :clojure
     :atom-grammars ["Clojure"]
     :file-extensions [#"\.proton$"])
  (mode/define-keybindings :clojure
    {:t {:category "toggles"
         :p {:action "parinfer:disable" :title "Toggle Parinfer"}}})

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
