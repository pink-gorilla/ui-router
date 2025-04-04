(ns router.service
  (:require
   [extension :refer [get-extensions]]))

(defn get-cljs-routes [_module-name _config exts _default-config]
  (->> (get-extensions exts {:cljs-routes-reitit nil})
       (map :cljs-routes-reitit)
       (remove nil?)
       (into [])))