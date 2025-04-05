(ns router.app
  (:require
   [reagent.dom.client :as rdom]
   [fipp.edn :as fedn]
   [router.core :refer [current-route reitit-wrap-fn-a]]
   [router.view :refer [viewer]]))

(defn not-found []
  [:div
   [:h1 "Page not found"]
   [:pre (with-out-str (fedn/pprint @current-route))]])

(defn wrap [view match]
  (if @reitit-wrap-fn-a
    [@reitit-wrap-fn-a view match]
    [view match]))

(defn page []
  (let [match @current-route
        {:keys [data]} match
        {:keys [view name]} data]
    [:div
     (cond
       ; match, and defined view(-fn)
       view
       [wrap view match]
       ; match, and only a name (use viewer to lookup name as a symbol)
       name
       [wrap viewer match]
       ; we got no match
       :else
       [not-found])]))

(defn mount []
  (let [root (rdom/create-root (.getElementById js/document "app"))]
    (rdom/render root [page])))
