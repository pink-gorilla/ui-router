(ns demo.notebook.page-nav
  (:require
   [re-frame.core :as rf]
   [frontend.routes :refer [nav! current-route]]))

(current-route)

(nav! 'reval.page.viewer/viewer-page)


(rf/dispatch [:bidi/goto :devtools])




