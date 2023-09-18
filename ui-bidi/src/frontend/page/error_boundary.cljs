(ns frontend.page.error-boundary
  (:require
   [taoensso.timbre :as timbre :refer-macros [error]]
   [reagent.core :as r]))

; shows how to implement error boundary:
; https://github.com/reagent-project/reagent/blob/c214466bbcf099eafdfe28ff7cb91f99670a8433/test/reagenttest/testreagent.cljs

; from component-did-catch
; #js {:componentStack 
       ; cmp@http://localhost:8000/r/cljs-runtime/reagent.impl.component.js:508:43
       ; cmp@http://localhost:8000/r/cljs-runtime/reagent.impl.component.js:508:43
       ; cmp@http://localhost:8000/r/cljs-runtime/reagent.impl.component.js:508:43
       ; div


(defn error-boundary [_ #_comp]
  (let [error-a (r/atom nil)
        info-a (r/atom nil)]
    (r/create-class
     {:component-did-catch (fn [_  _ i] #_this #_e  ; [this e i]
                             ; i is a js object with stacktrace
                             (error "page did catch: " i)
                             (reset! info-a i))

      :get-derived-state-from-error (fn [e]
                                      ; to saves the exception data; gets shows in the dom
                                      ;(println "pinkie component get-derived-state-from-error: " e)
                                      (reset! error-a e)
                                      #js {})
      :reagent-render (fn [comp]
                        (if @error-a
                          [:div.bg-red-300
                           "Error: "
                           (when @error-a (pr-str @error-a))]
                          comp))})))