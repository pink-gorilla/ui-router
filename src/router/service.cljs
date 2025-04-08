(ns router.service
  (:require
   [taoensso.timbre :refer-macros [info warn error]]
   [promesa.core :as p]
   ;[webly.spa.mode :refer [get-routing-path]]
   [webly.spa.resolve :refer [get-resolver]]
   [router.core :refer [start-router!]]))

(defonce start-data-a (atom nil))

(defn init-routes [{:keys [routes reitit]}]
  (info "starting reitit-routes: " routes)
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
                  (info "reitit-routes have been successfully inititalized.")
                  (let [routes (apply concat route-seq)]
                    (reset! start-data-a [routes reitit-wrap-fn]))))
        (p/catch (fn [err]
                   (error "init reitit-routers error: " err))))
    done-p))


(defn start-router []
  ; router can not be started in a service, because route-controllers might depend
  ; on other services that are not yet started.
  (if-let [[routes reitit-wrap-fn] @start-data-a] 
    (start-router! routes reitit-wrap-fn)
    (error "cannot start reitit router - start-data-a is nil")))