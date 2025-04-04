(ns router.core
  (:require 
   [reagent.core :as r]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rfe]
   [reitit.coercion.schema :as rsc]
   ))

(def current-route (r/atom nil))

(defn on-navigate [match _]
  ; on-navigate updates current-route when the URL changes.
  (reset! current-route match))

(defn log-fn [& params]
  (fn [_]
    (apply js/console.log params)))

(defn start-router! [routes]
  (println "starting reitit router with routes: " routes)
  (let [router (rtf/router routes
                           {:data {:controllers [{:start (log-fn "start" "root-controller")
                                                  :stop (log-fn "stop" "root controller")}]
                                   :coercion rsc/coercion}}
                           )]
    (rfe/start! router on-navigate {:use-fragment true})))




