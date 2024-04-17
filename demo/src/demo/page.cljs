(ns demo.page
  (:require
   [demo.helper :refer [link-dispatch]]))

(defn page-main [_route]
  [:div
   [:h1 "ui-bidi demo"]
   [:p [link-dispatch [:bidi/goto 'demo.page/page-test] "test"]]
   [:p [link-dispatch [:bidi/goto 'demo.page/page-unknown] "unknown-page"]]])

(defn page-test [_route]
  [:div
   [:h1 "ui-bidi test-page (not via page-registry!)"]
   [:p [link-dispatch [:bidi/goto 'demo.page/page-main] "main"]]])