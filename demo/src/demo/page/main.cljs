(ns demo.page.main
  (:require
   [frontend.page :refer [add-page]]
   [demo.helper :refer [link-dispatch]]))

(defn page-main [_route]
  [:div
   [:h1 "ui-bidi demo"]
   [:p [link-dispatch [:bidi/goto 'demo.page.promise/page-test] "test"]]
   [:p [link-dispatch [:bidi/goto 'demo.page.unknown/page-unknown] "unknown-page"]]])

(add-page 'demo.page.main/page-main page-main)
