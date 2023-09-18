(ns goldly.page.page
  (:require
   [frontend.page]))

(defn get-page-handler [{:keys [handler]}]
  (frontend.page/get-page handler))

(defn show-page
  ; not quite sure what is the purpose of this. 2023-09-15 awb99
  "shows a page 
   expects: kw and route-map"
  [route-map]
  (get-page-handler route-map))

#_(defn show-page
    "shows a page 
   expects: kw and route-map"
    [route-map]
    (reagent-page route-map))

(defn add-page
  "add-page is exposed to sci
   defines a new browser-based page 
   that can be used in the routing table to define new pages"
  [p page-id]
  (frontend.page/add-page page-id p))


