(ns frontend.page
  (:require
   [promesa.core :as p]
   [frontend.routes :refer [current]]))

(def page-dict (atom {}))

(defn get-page-from-dict [page-id]
  (get @page-dict page-id))

(defn available-pages []
  (-> @page-dict
      keys
      (into [])))

(defn add-page [page-id page-fn]
  (swap! page-dict assoc page-id page-fn))

#_(defmulti reagent-page
    (fn [x] (get x :handler)))

#_(defn available-pages []
    (->> (methods reagent-page)
         keys
         (remove #(= :default %))
         (into [])))

(defn not-found-page []
  [:div.bg-red-500.m-5
   [:h1 (str "page " (:handler @current) " - not found!")]
   [:p "Available pages: " (pr-str (available-pages))]
   [:p "Current Page:" (str @current)]])

#_(defmethod reagent-page :default [& args]
    [not-found-page])

(defn resolve-page-nothing [page-id]
  nil)

(def page-resolver (atom resolve-page-nothing))

(defn set-resolver! [resolver-fn]
  (reset! page-resolver resolver-fn))

(defn get-page [page-id]
  (if-let [page (get-page-from-dict page-id)]
    page
    (if-let [page (@page-resolver page-id)]
      (if (p/promise? page)
        (if (p/resolved? page)
          @page
          [:div "loading page.."])
        page)
      not-found-page)))

