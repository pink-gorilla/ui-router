(ns router.core
  (:require
   [reagent.core :as r]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rfe]
   [reitit.coercion.schema :as rsc]
   [reitit.frontend.controllers :as rfc]
   ))

(def current-route (r/atom nil))

(def reitit-wrap-fn-a (r/atom nil))

(defn on-navigate [match _]
  ; on-navigate updates current-route when the URL changes.
  (reset! current-route match))

(defn on-navigate-with-controllers [new-match _]
  (swap! current-route
         (fn [old-match]
           (if new-match
             (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))
             new-match))))

(defn log-fn [& params]
  (fn [_]
    (apply js/console.log params)))

(defn start-router! [routes reitit-wrap-fn]
  (println "starting reitit router with routes: " routes " reitit-wrap-fn: " reitit-wrap-fn)
  (reset! reitit-wrap-fn-a reitit-wrap-fn)
  (let [router (rtf/router routes
                           {:data {:controllers [{:start (log-fn "start" "root-controller")
                                                  :stop (log-fn "stop" "root controller")}]
                                   :coercion rsc/coercion}})]
    ;(rfe/start! router on-navigate {:use-fragment true})
    (rfe/start! router on-navigate-with-controllers {:use-fragment true})
    ))




