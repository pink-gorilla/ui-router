(ns router.service
  (:require
   [taoensso.timbre :refer-macros [info warn error]]
   [promesa.core :as p]
   ;[webly.spa.mode :refer [get-routing-path]]
   [webly.spa.resolve :refer [get-resolver]]
   [router.core :refer [start-router!]]))

(defn start-router [{:keys [routes reitit]}]
  (info "starting cljs-router: " routes)
  (let [;rpath (get-routing-path)
        resolve-fn (get-resolver)
        routes-all (map #(resolve-fn %) routes)
        route-seq-p (p/all routes-all)
        reitit-wrap (:wrap reitit)
        reitit-p (if reitit-wrap
                   (resolve-fn reitit-wrap)
                   (p/resolved nil))
        done-p (p/all [route-seq-p reitit-p])]
    (-> done-p
        (p/then (fn [[route-seq reitit-wrap-fn]]
                  (info "routes have been successfully resolved.")
                  (let [routes (apply concat route-seq)]
                    (start-router! routes reitit-wrap-fn))))
        (p/catch (fn [err]
                   (error "start-router error: " err))))
    done-p))

