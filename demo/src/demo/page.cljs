(ns demo.page
  (:require
   [demo.helper :refer [link-dispatch]]))

(defn page-main [_route]
  [:div.bg-blue-300
   [:h1 "This is the main page."]
   [:p [link-dispatch [:bidi/goto 'demo.page/page-test] "test"]]
   [:p [link-dispatch [:bidi/goto 'demo.page/page-unknown] "unknown-page"]]])

(defn page-test [_route]
  [:div
   [:h1.text-bold.text-blue-500 "I am a test-page!"]
   [:p [link-dispatch [:bidi/goto 'demo.page/page-main] "main"]]])