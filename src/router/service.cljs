(ns router.service
  (:require
   [taoensso.timbre :refer-macros [info warn error]]
   [promesa.core :as p]
   ;[webly.spa.mode :refer [get-routing-path]]
   [webly.spa.resolve :refer [get-resolver]]
   [router.core :refer [start-router!]]))

(defn start-router [cljs-routes]
  (info "starting cljs-router: " cljs-routes)
  (let [;rpath (get-routing-path)
        resolve-fn (get-resolver)
        routes-all (map #(resolve-fn %) cljs-routes)
        all-p (p/all routes-all)]
    (-> all-p
        (p/then (fn [route-seq]
                  (info "routes have been successfully resolved.")
                  (let [routes (apply concat route-seq)]
                    (start-router! routes))))
        (p/catch (fn [err]
                   (error "start-router error: " err))))
    all-p))

