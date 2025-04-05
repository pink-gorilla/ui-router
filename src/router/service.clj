(ns router.service
  (:require
   [extension :refer [get-extensions]]))

(defn get-cljs-routes [_module-name config exts _default-config]
  (let [routes (->> (get-extensions exts {:cljs-routes-reitit nil})
                    (map :cljs-routes-reitit)
                    (remove nil?)
                    (into []))]
    {:routes routes
     :reitit (:reitit config)}))