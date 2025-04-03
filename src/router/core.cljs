(ns router.core
  (:require 
   [reagent.core :as r]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rfe]))

(def routes
  [["/" {:name :home :view (fn [] [:h1 "Home"])}]
   ["/about" {:name :about :view (fn [] [:h1 "About"])}]])

(def router (rtf/router routes))



(def current-route (r/atom nil))

(defn on-navigate [match _]
  (reset! current-route match))

(defn init-router! []
  (rfe/start! router on-navigate {:use-fragment false}))

; Fragment router
; Fragment is simple integration which stores the current route in URL fragment, 
; i.e. after #. This means the route is never part of the request URI and server will always know which file to return (index.html) .


; on-navigate updates current-route when the URL changes.

; rfe/start! initializes the router.

(defn nav []
  [:nav
   [:a {:href (rfe/href :home)} "Home | "]
   [:a {:href (rfe/href :about)} "About"]])

; (rfe/href ::bar {} {:q :hello})


(defn app []
  (let [{:keys [view]} @current-route]
    [:div
     [nav]
     (if view [view] [:h1 "Page not found"])]))