(ns proton.layers.lang.javascript.core
  (:require [proton.layers.core.actions :as actions :refer [state]]
            [proton.lib.package_manager :as package])
  (:use [proton.layers.base :only [init-layer! get-initial-config get-keybindings get-packages get-keymaps describe-mode]]))

(defmethod get-initial-config :lang/javascript
  []
  [["proton.lang.javascript.linter" "eslint"]])

(defn- toggle-linter [linter]
  (case (keyword linter)
   :eslint (do
            (package/enable-package "linter-eslint")
            (package/disable-package "linter-jshint"))
   :jshint (do
            (package/enable-package "linter-jshint")
            (package/disable-package "linter-eslint"))))

(defmethod init-layer! :lang/javascript
  [_ config]
  (let [config-map (into (hash-map) config)]
    (toggle-linter (config-map "proton.lang.javascript.linter")))
  (println "hello javascript"))

(defmethod get-keybindings :lang/javascript
  []
  {})

(defmethod get-packages :lang/javascript
  []
  [:atom-ternjs
   :javascript-snippets
   :language-javascript
   ; TODO move linters according to core layers in future
   :linter
   :linter-eslint
   :linter-jshint
   :autocomplete-modules
   ; TODO move this to frameworks layer
   :react
   :react-snippets
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
         :r {:action "tern:rename" :target actions/get-active-editor :title "tern rename variable"}}
     :L {:category "linters"
          :e {:fx (fn [] (toggle-linter "eslint")) :title "use eslint"}
          :j {:fx (fn [] (toggle-linter "jshint")) :title "use jshint"}}}})
