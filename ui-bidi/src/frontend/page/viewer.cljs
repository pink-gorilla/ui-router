(ns frontend.page.viewer
  (:require
   [promesa.core :as p]
   [reagent.core :as r]
   [taoensso.timbre :refer-macros [debug info error]]
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

(defn message [m]
  (fn [_route]
    [:div m]))

(defn handler-promise-view [hp]
  (info "page is a promise: " hp)
  (let [view (r/atom (message "loading page.."))]
    (p/then hp (fn [result]
                 (info "page-promise has been resolved!")
                 (info "result: " result)
                 (reset! view result)
                 ))
    (p/catch hp (fn [err]
                 (error "error in resolving page-promise: " err)
                 (reset! view (message [:p.text-xl.text-bold.text-blue-500.bg-red-200
                                          "page load error!"
                                        [:p.text-red-500 
                                          (str err)]]))
                 ))
     view))

(defn view-route [route]
  (let [h (get-page-handler route)
        view (if (p/promise? h)
               (handler-promise-view h)
               (r/atom h))]
    (fn [route]
      [@view route])))


(defn page-viewer []
  [error-boundary
   ^{:key [@generation @current]}
     [view-route @current]])




