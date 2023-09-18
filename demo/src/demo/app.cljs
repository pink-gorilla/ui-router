(ns demo.app
  (:require
   [promesa.core :as p]
   [frontend.page :refer [set-resolver!]]
   [demo.helper :refer [link-dispatch]]))

(println "demo app loading..")

(defn page-test [_route]
  [:div
   [:h1 "ui-bidi test-page (not via page-registry!)"]
   [:p [link-dispatch [:bidi/goto 'demo.page.main/page-main] "main"]]])

(def page-dict
  {'demo.page.promise/page-test page-test})

(defn page-resolver [s]
  (let [p (get page-dict s)]
    p))

;(set-resolver! page-resolver)

(defn page-resolver-promise [s]
  (let [page (get page-dict s)]
    (if page
      ;(p/resolved page)
      (p/delay 3000 page)
      nil)))

(set-resolver! page-resolver-promise)

