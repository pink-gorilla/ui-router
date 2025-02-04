(ns frontend.page
  (:require
   [frontend.routes :refer [current]]
   [webly.spa.resolve :refer [get-resolver]]
   ))

(defn not-found-page []
  [:div.bg-red-500.m-5
   [:h1 (str "page " (:handler @current) " - not found!")]
   [:p "Current Page:" (str @current)]])


(defn resolve-page [page]
  (let [resolve-fn (get-resolver)]
     (resolve-fn page)))

(defn get-page [page-id]
  (if-let [page (resolve-page page-id)]
    page
    not-found-page))

