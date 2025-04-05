(ns demo.page
  (:require
   [reitit.frontend.easy :as rfe]
   [fipp.edn :as fedn]
   [schema.core :as s]))

 (defn nav []
   [:div 
    [:hr]
    [:p "nav"]
    [:ul
     [:li [:a {:href (rfe/href 'demo.page/page-main {:id 2} {:foo "bar"} "zzz")} "Main"]]
     [:li [:a {:href (rfe/href :about {:id 2} {:foo "bar"} "zzz")} "About"]]
     [:li [:a {:href (rfe/href 'demo.page/page-item {:id 1})} "Item 1"]]
     [:li [:a {:href (rfe/href 'demo.page/page-item {:id 2} {:a 42})} "Item 2"]]
     [:li [:a {:href (rfe/href 'demo.page/page-item {:id 3} {:foo "bar"})} "Item 3"]]
     [:li [:a {:href (rfe/href 'demo.page/page-lazy {:id 3} {:foo "bar"})} "Lazy"]]
     [:li [:a {:href (rfe/href 'demo.page/page-lazy2 {:id 3} {:foo "bar"} {:b 12})} "Lazy v2"]]
     [:li [:a {:href (rfe/href 'demo.page/page-unknown {:id 2} {:foo "bar"} "zzz")} "unknown"]]
     [:li [:a {:href (rfe/href :bad {:id 2} {:foo "bar"} "zzz")} "bad"]]
     ]])
 
 (defn wrap-nav [page match]
   [:div.bg-blue-300
    [page match]
    [nav]])

(defn page-main [_match]
  [:h1 "This is the main page."])

(defn page-item [match]
  [:div 
   [:h1.text-bold.text-blue-500 "I am the item-page!"]
   [:hr]
   [:p "Match details:"]
   [:pre (with-out-str (fedn/pprint match))]
   ])
 
(defn page-lazy [match]
  [:div
   [:h1.text-bold.text-blue-500 "I am the lazy-page!"]
   [:hr]
   [:p "Match details:"]
   [:pre (with-out-str (fedn/pprint match))]
   [:a {:href (rfe/href 'demo.page/page-lazy2 {:id 3} {:foo "bar"})} "goto lazy 2"]
   ])

(defn page-lazy2 [match]
  [:div
   [:h1.text-bold.text-blue-500 "I am the lazy-page v2!"]
   [:hr]
   [:p "Match details:"]
   [:pre (with-out-str (fedn/pprint match))]])

(defn page-bad [match]
  (throw (ex-info "something bad happend" {:message "this is for testing"})))


 (def routes
   [["/" {:name 'demo.page/page-main :view page-main}]
    ["/about" {:name :about :view (fn [] [:h1 "About"])}]
    ["/lazy" {:name 'demo.page/page-lazy}]
    ["/lazy2" {:name 'demo.page/page-lazy2}]
    ["/bad" {:name :bad :view page-bad}]
    ["/:id/item" {:name 'demo.page/page-item :view page-item
                  :parameters {:path {:id s/Int}
                               :query {(s/optional-key :a) s/Int
                                       (s/optional-key :foo) s/Keyword}}
              }]
    ])