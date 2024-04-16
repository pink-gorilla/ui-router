(ns demo.notebook.page-nav
  (:require
   [re-frame.core :as rf]
   [frontend.page :refer [nav! current-route]]))

(current-route)

(nav! 'demo.page/bongo)


(rf/dispatch [:bidi/goto :devtools])




