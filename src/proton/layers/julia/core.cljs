(ns proton.layers.julia.core
  (:require [proton.lib.mode :as mode])
  (:use [proton.layers.base :only [init-layer! get-initial-config get-keybindings get-packages get-keymaps]]))

(defmethod get-initial-config :julia
  []
  [])

(defmethod init-layer! :julia
  [_ config]
  (mode/define-mode :julia
    :atom-grammars "Julia")
  (println "init julia"))

(defmethod get-keybindings :julia
  []
  {})

(defmethod get-packages :julia
  []
  [:language-julia
   :latex-completions
   :julia-client
   :ink])

(defmethod get-keymaps :julia
  []
  [])
