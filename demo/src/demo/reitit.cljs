(ns demo.reitit
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.frontend.controllers :as rfc]
            [reitit.coercion.schema :as rsc]
            [schema.core :as s]
            [fipp.edn :as fedn]))

(defn home-page []
  [:div
   [:h2 "Welcome to frontend"]
   [:p "Look at console log for controller calls."]])

(defn item-page [match]
  (let [{:keys [path query]} (:parameters match)
        {:keys [id]} path]
    [:div
     [:ul
      [:li [:a {:href (rfe/href ::item {:id 1})} "Item 1"]]
      [:li [:a {:href (rfe/href ::item {:id 2} {:foo "bar"} "zzz")} "Item 2"]]]
     (when id
       [:h2 "Selected item " id])
     [:div "Query params: " [:pre (pr-str query)]]
     [:ul
      [:li [:a {:on-click #(rfe/set-query {:a 1})} "set a=1"]]
      [:li [:a {:on-click #(rfe/set-query {:a 2} {:replace true})} "set a=2 and replaceState"]]
      [:li [:a {:on-click (fn [_] (rfe/set-query #(assoc % :foo "zzz")))} "add foo=zzz"]]]
     [:button
      {:on-click #(rfe/navigate ::item {:path-params {:id 3}
                                        :query-params {:foo "aaa"}})}
      "Navigate example, go to item 3"]]))

(defonce match (r/atom nil))

(defn current-page []
  [:div
   [:ul
    [:li [:a {:href (rfe/href ::frontpage)} "Frontpage"]]
    [:li
     [:a {:href (rfe/href ::item-list)} "Item list"]]]
   (when @match
     (let [view (:view (:data @match))]
       [view @match]))
   [:pre (with-out-str (fedn/pprint @match))]])

(defn log-fn [& params]
  (fn [_]
    (apply js/console.log params)))

(def routes
  (rf/router
   ["/"
    [""
     {:name ::frontpage
      :view home-page
      :controllers [{:start (log-fn "start" "frontpage controller")
                     :stop (log-fn "stop" "frontpage controller")}]}]
    ["items"
      ;; Shared data for sub-routes
     {:view item-page
      :controllers [{:start (log-fn "start" "items controller")
                     :stop (log-fn "stop" "items controller")}]}

     [""
      {:name ::item-list
       :controllers [{:start (log-fn "start" "item-list controller")
                      :stop (log-fn "stop" "item-list controller")}]}]
     ["/:id"
      {:name ::item
       :parameters {:path {:id s/Int}
                    :query {(s/optional-key :a) s/Int
                            (s/optional-key :foo) s/Keyword}}
       :controllers [{:parameters {:path [:id]}
                      :start (fn [{:keys [path]}]
                               (js/console.log "start" "item controller" (:id path)))
                      :stop (fn [{:keys [path]}]
                              (js/console.log "stop" "item controller" (:id path)))}]}]]]
   {:data {:controllers [{:start (log-fn "start" "root-controller")
                          :stop (log-fn "stop" "root controller")}]
           :coercion rsc/coercion}}))

(defn mount []
  (rfe/start!
   routes
   (fn [new-match]
     (swap! match (fn [old-match]
                    (if new-match
                      (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))))))
   {:use-fragment true})
  
  ;(rd/render [current-page] (.getElementById js/document "app"))
  (let [root (rdom/create-root (.getElementById js/document "app"))]
    (rdom/render root [current-page])))
 
