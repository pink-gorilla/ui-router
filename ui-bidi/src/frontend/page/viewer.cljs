(ns frontend.page.viewer
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [subscribe]]
   [frontend.page :refer [reagent-page]]
   [frontend.routes :refer [current]]))

(defonce generation (r/atom 1))

; https://stackoverflow.com/questions/33299746/why-are-multi-methods-not-working-as-functions-for-reagent-re-frame
; ^{:key @current-route} [pages @current-route]

(defn page-viewer []
  (fn []
    ^{:key [@generation @current]}
    [reagent-page @current])) ; multimethod fix

(defn refresh-page
  "used in goldly for dynamic reloading when page source was changed"
  []
  (swap! generation inc))


