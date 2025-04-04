(ns router.app
  (:require
   [reagent.dom.client :as rdom]
   [fipp.edn :as fedn]
   [router.core :refer [current-route]]))


(defn not-found []
  [:div 
   [:h1 "Page not found"]
   [:pre (with-out-str (fedn/pprint @current-route))]])

(defn app []
  (let [match @current-route
        {:keys [data]} match
        view (:view data)]
    [:div
     (if view
       [view match]
       [not-found])]))
 

(defn mount []
  (let [root (rdom/create-root (.getElementById js/document "app"))]
    (rdom/render root [app])))
 