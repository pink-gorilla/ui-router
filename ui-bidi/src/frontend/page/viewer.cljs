(ns frontend.page.viewer
  (:require
   [promesa.core :as p]
   [reagent.core :as r]
   [frontend.page :refer [get-page]]
   [frontend.routes :refer [current]]
   [frontend.page.error-boundary :refer [error-boundary]]))

(defonce generation (r/atom 1))

(defn refresh-page
  "used in goldly for dynamic reloading when page source was changed"
  []
  (swap! generation inc))

; https://stackoverflow.com/questions/33299746/why-are-multi-methods-not-working-as-functions-for-reagent-re-frame
; ^{:key @current-route} [pages @current-route]

(defn get-page-handler [{:keys [handler]}]
  (get-page handler))

(defn handler-promise-view [hp]
  (let [view (r/atom [:div "loading page.."])]
    (p/then hp (fn [result]
                (reset! view result)))
    (p/catch hp (fn [err]
                 (println "error in page: " err)
                 (reset! view [:div "page loading error!"])
                 ))
    (fn [hp]
       @view)))

(defn page-viewer []
  (let [h (get-page-handler @current)
        view (if (p/promise? h)
               (handler-promise-view h)
               (r/atom h))]
      ^{:key [@generation @current]}
    [error-boundary
     [@view @current]]  
    ))




