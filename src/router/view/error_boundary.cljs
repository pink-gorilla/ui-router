(ns router.view.error-boundary
  (:require
   [taoensso.timbre :as timbre :refer-macros [error]]
   [reagent.core :as r]
   ;[clojure.string :as string]
   ;[shadow.cljs.modern :refer [defclass]]
   ;[applied-science.js-interop :as j]
   ;["react" :as react]
   ))

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

#_(defn error-boundary2 [child]
    (r/create-class
     {:display-name "ErrorBoundary"

      :constructor (fn [this _]
                     (set! (.-state this) #js {:hasError false}))

      :component-did-catch
      (fn [this error info]
        (js/console.error "Caught error:" error info)
        (.setState this #js {:hasError true}))

      :reagent-render
      (fn [child]
        (let [state (.-state (r/current-component))]
          (if (.-hasError state)
            [:div {:style {:color "red"}} "Something went wrong."]
            [child])))}))

#_(defn- render-error-component [{:keys [error info]}]
    [:div
     {:style {:width           "100%"
              :min-width       300
              :backgroundColor "rgba(255,0,0,0.2)"
              :padding         8}}
     [:h6 error]
     [:pre info]])

#_(def ^:dynamic *render-error* render-error-component)

#_(defn catch []
    (let [error-state (r/atom nil)
          info-state  (r/atom nil)]
      (r/create-class
       {:component-did-catch
        (fn re-catch-block [this error info]
          (reset! error-state (let [max-error-lenght 200]
                                (when-let [err (str error)]
                                  (if (-> err count (< max-error-lenght))
                                    (-> err
                                        (subs 0 max-error-lenght)
                                        (str " ..."))))))
          (reset! info-state (some->> info
                                      .-componentStack
                                      (string/split-lines)
                                      (remove string/blank?)
                                      (drop-while #(re-find #"re_catch" %))
                                      (take 3)
                                      (string/join "\n"))))
        :reagent-render (fn [& body]
                          (if @error-state
                            [*render-error*
                             {:error @error-state :info @info-state}]
                            (when-not (->> body (remove nil?) empty?)
                              (into [:<>] body))))})))

#_(defn error-boundary3 [comp]
    (let [error (r/atom nil)]
      (r/create-class
       {:component-did-catch (fn [this e info])
        :get-derived-state-from-error (fn [e]
                                        (reset! error e)
                                        #js {})
        :reagent-render (fn [comp]
                          (if @error
                            [:div
                             "Something went wrong."
                             [:button {:on-click #(reset! error nil)} "Try again"]]
                            comp))})))

#_(defn error-view [& args]
    [:div "error: "
     (pr-str args)])

#_(defclass ErrorBoundary
    (extends react/Component)
    (field handle-error)
    (field hash)
    (constructor [this ^js props]
                 (super props)
                 (set! (.-state this) #js {:error nil :hash (j/get props :hash)})
                 (set! hash (j/get props :hash))
                 (set! handle-error (fn [error]
                                      (set! (.-state this) #js {:error error}))))

    Object
    (render [this ^js props]
            (j/let [^js {{:keys [error]} :state
                         {:keys [children]} :props} this]
              (if error
                (r/as-element [error-view error])
                children))))

#_(j/!set ErrorBoundary
          :getDerivedStateFromError (fn [error] #js {:error error})
          :getDerivedStateFromProps (fn [props state]
                                      (when (not= (j/get props :hash)
                                                  (j/get state :hash))
                                        #js {:hash (j/get props :hash) :error nil})))
