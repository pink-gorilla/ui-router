(ns frontend.page.viewer
  (:require
   [reagent.core :as r]
   [frontend.page :refer [get-page]]
   [promesa.core :as p]
   [frontend.routes :refer [current]]))

(defonce generation (r/atom 1))

(defn refresh-page
  "used in goldly for dynamic reloading when page source was changed"
  []
  (swap! generation inc))

; https://stackoverflow.com/questions/33299746/why-are-multi-methods-not-working-as-functions-for-reagent-re-frame
; ^{:key @current-route} [pages @current-route]

(defn get-page-handler [{:keys [handler]}]
  (get-page handler))

(defn- show-message [msg]
  (fn [_route] [:div msg]))

(defn page-viewer []
  (fn []
    (let [h (get-page-handler @current)
          view (if (p/promise? h)
                 (cond
                   (p/pending? h)  (show-message "Loading Page ...")
                   (p/resolved? h) @h
                   (p/rejected? h) (show-message "Page could not be loaded!")
                   :else (show-message "Page - something crazy happened!"))
                 h)]
      ^{:key [@generation @current]}
      [view @current])))




