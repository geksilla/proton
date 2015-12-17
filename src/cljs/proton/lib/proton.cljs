(ns proton.lib.proton
  (:require [cljs.reader :as reader]
            [cljs.nodejs :as node]
            [proton.lib.mode :as mode-manager]
            [proton.lib.helpers :as helpers]
            [proton.lib.atom :as atom-env]
            [proton.layers.base :as layerbase]))

(def config-path (str (.. js/process -env -HOME) "/.proton"))

(defn- method-filter
  "Takes `defmulti` method name and vector of values to dispatch.
  Returns list of associated dispatchers."
   [method dispatchers]
 (filter #(not (nil? %)) (map #(if (nil? (get-method method %)) nil (keyword %)) dispatchers)))

(defn get-vector-from-multimethod
  "Takes `defmulti` method name and list of dispatchers.
  Returns a vector as result of executed method."
   [method dispatchers]
  (reduce concat (map #(method (keyword %)) (method-filter method dispatchers))))

(defn get-config-template-path []
  (let [path (node/require "path")]
    (.resolve path (str js/__dirname "/../templates/proton.edn"))))

(defn has-config? []
  (helpers/is-file? config-path))

(defn create-default-config! []
  (let [child-process (node/require "child_process")
        template-path (get-config-template-path)]
    (.execSync child-process (str "cp " template-path " " config-path))))

(defn load-config []
  (if (not (has-config?))
    (create-default-config!))
  (reader/read-string (helpers/read-file config-path)))

(defn packages-for-layers [layers]
  (let [layers-packages (get-vector-from-multimethod layerbase/get-packages layers)
        dependencies (get-vector-from-multimethod layerbase/package-deps layers-packages)]
      (into [] (distinct (concat layers-packages dependencies)))))

(defn keybindings-for-layers [layers]
  (reduce helpers/deep-merge (map #(layerbase/get-keybindings (keyword %)) layers)))

(defn configs-for-layers [layers]
  (apply concat
    (filter #(not (empty? %))
      (map #(layerbase/get-initial-config (keyword %)) layers))))

(defn keymaps-for-layers [layers]
  (reduce concat (map #(layerbase/get-keymaps (keyword %)) layers)))

(defn init-layers! [layers config]
  (doall (map #(layerbase/init-layer! (keyword %) config) layers)))

(defn init-modes-for-layers [layers]
  (doall
    (map #(mode-manager/define-mode (get % :mode-name) (dissoc % :mode-name))
     (filter #(not (nil? (get % :mode-name))) (map #(layerbase/describe-mode %) layers)))))

(defn- on-active-pane-item [item]
  (if-let [editor (atom-env/get-active-editor)]
    (when (= (.-id editor) (.-id item))
     (mode-manager/activate-mode editor))))

(defn panel-item-subscription [] (.onDidChangeActivePaneItem atom-env/workspace on-active-pane-item))

(defn execute-methods
  "Takes `defmulti` method name and vector of values to dispatch.
  Executes all methods for associated dispatch values."
   [method dispatchers]
 (doall (map method (method-filter method dispatchers))))

(defn run-post-init
  "Takes packages (vector of keywords).
  Executes `post-init` multimethod for associated package names."
   [packages]
 (execute-methods layerbase/post-init packages))
