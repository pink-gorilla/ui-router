(ns router.view
  (:require
   [promesa.core :as p]
   [reagent.core :as r]
   [taoensso.timbre :refer-macros [debug info error]]
   [webly.spa.resolve :refer [get-resolver]]
   [router.view.error-boundary :as eb]))

(defonce generation (r/atom 1))

(defn refresh-page
  "used for dynamic reloading when page source was changed"
  []
  (swap! generation inc))

; https://stackoverflow.com/questions/33299746/why-are-multi-methods-not-working-as-functions-for-reagent-re-frame
; ^{:key @current-route} [pages @current-route]

(defn create-error-view [view-symbol err]
  (fn [_match]
    [:div
     "error resolving: " view-symbol
     "error: "
     (pr-str err)]))

(defn symbol->atom [view-symbol]
  (let [resolve-fn (get-resolver)
        view-a (r/atom nil)
        resolve-p (resolve-fn view-symbol)]
    (-> resolve-p
        (p/then (fn [view-fn]
                  (reset! view-a view-fn)))
        (p/catch (fn [err]
                   (error "error in resolving page-promise: " err)
                   (reset! view-a (create-error-view view-symbol err)))))
    view-a))

(defn view-route [view-symbol match]
  (r/with-let
    [view-fn-a (symbol->atom view-symbol)]
    (when @view-fn-a
      ;[:> eb/ErrorBoundary {:hash 55}
      [eb/error-boundary  ; 3 ; catch ; error-boundary2
       [@view-fn-a match]])))

(defn viewer [{:keys [data] :as match}]
  (let [view-symbol (:name data)]
    (if (symbol? view-symbol)
      [view-route view-symbol match]
      [:div
       "cannot show route :name " (str view-symbol) " because it is not a symbol."])))
