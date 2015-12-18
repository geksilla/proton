(ns proton.layers.lang.javascript.core
  (:require [proton.layers.core.actions :as actions :refer [state]]
            [proton.lib.package_manager :as package])
  (:use [proton.layers.base :only [init-layer! get-initial-config get-keybindings get-packages get-keymaps describe-mode]]))

(defmethod get-initial-config :lang/javascript
  []
  [["proton.lang.javascript.linter" "eslint"]])

(defmethod init-layer! :lang/javascript
  [_ config]
  (println "hello javascript"))

(defmethod get-keybindings :lang/javascript
  []
  {})

(defmethod get-packages :lang/javascript
  []
  [:atom-ternjs
   :javascript-snippets
   :language-javascript
   :linter
   :linter-eslint
   ;;:linter-jshint
   :react
   :docblockr])

(defmethod get-keymaps :lang/javascript
  []
  [])

(defmethod describe-mode :lang/javascript
  []
  {:mode-name :javascript
   :atom-scope ["source.js"]
   :mode-keybindings
    {:g {:category "go to"
         :g {:action "tern:definition" :target actions/get-active-editor :title "defintion"}
         :q {:action "tern:markerCheckpointBack" :target actions/get-active-editor :title "back from definition"}
         :r {:action "tern:references" :target actions/get-active-editor :title "references"}}
     :s {:category "symbols/show"
         :l {:action "symbols-view:toggle-file-symbols" :target actions/get-active-editor :title "file symbols"}}
     :r {:category "refactor"
         :r {:action "tern:rename" :target actions/get-active-editor :title "tern rename variable"}}}})
