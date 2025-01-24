(ns stateless-frontend-architecture.views
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [stateless-frontend-architecture.subs :as subs]
   ;;
   [stateless-frontend-architecture.views.long-loading :as long-loading]
   [stateless-frontend-architecture.views.often-updates-form :as often-updates-form]
   [stateless-frontend-architecture.views.classical-form :as classical-form]
   [stateless-frontend-architecture.views.multiple-elements :as multiple-elements]))

(defn mixed-usage []
  [:div
   [:span "This panel will display some content using other panels data"]
   ])


(defn form-one-change-panel []
  [:div "form panel with update on submit"])

(defn form-each-field-change-panel []
  [:div
   [:span "form panel with tons of updates on change"]
   [often-updates-form/main-panel]])

(defn multiple-items [items]
  [:div [:span "Here will be X amount of elements displayed with some settings to modify one of them"]
   [multiple-elements/multiple-items items]])

(defn long-loading-panel []
  [:div "Long loading panel probably from some weather http call..."
   ])

(defn example-panel []
  [:div
   [:span "Example panel containing 4 elements"]
   [:br]
   [:br]
   [long-loading-panel]
   [:hr]
   [form-one-change-panel]
   [:hr]
   [form-each-field-change-panel]
   [:hr]
   [multiple-items multiple-elements/updated-items]
   [:hr]
   [mixed-usage]])

;; Items schema
;; {:items {1 {:text "Number 1 item" :checked? false}
;;          2 {:text "Number 2 item" :checked? false}}}
;; From backend comes list:
;; [{:id x :text y} {:id x :text y}]
;; From UI we have:
;; :checked? bool
;;
;;
;; Component data woudl look like this
;; {:main-component
;;  {:multiple-items-component
;;      {:title "Multiple items component"
;;       :description "Here will be X amount of elements..."
;;
;;
;;Inspiration talk:
;;https://vimeo.com/861600197 minute 22:30
;;parens of the dead: https://github.com/magnars/parens-of-the-dead-s2/tree/episode-34
;;
;;state {:vehicles [{:vehicle/id 1 :vehicle/name "Batmobile"}
;;                  {:vehicle/id 2 :vehicle/name "Model Y"}]}
;;(parepare-index-page state)
;;-> (get-in [:sections 0])
;;
;;{:kind :textdown-menu
;; :text "Batmobile"
;; :actions [[:save-in-store [:transient :vehicle-open?] true]]}
;; :options [{:text "Batomible"
;;            :selected? true}
;;            {:text "Modely Y
;;             :actions [[:go-to-location {:page-id :page.charging/index :query-params {:vehicle "vehicle-2"}}]]}
;;
;;
;;article: https://cjohansen.no/stateless-data-driven-uis/
;;
;;
;;Continnue and think how to do it
;;OR try to implement the article with re-frame.
;;
;;General idea:
;;Main-panel is one view
;;One view has multiple sections
;;Each of those sections has it's data (title, description components inside)
;;Each of those sections is different but some data is between them (so e.g. one loads much longer)
;;
;;Idea is to separate it into 3 layers:
;;1. First gather data at the beginning
;;2. Transform this data into UI structure of the view
;;3. Put it into component
;;
;;^ Here to think about, how to do it with re-frame subscribes and dispatches
;;
;;^ Going to far for now, but we may have some tooling to cache between views, but still needs to test it here.
;;
;;I want to see what are the costs of future changes, if we would need to break it down lower and how we can do it




(def data-schema
  {:items {1 {:text "Number 1 item" :checked? false}
           2 {:text "Number 2 item" :checked? false}}})

(defn main-panel []
  (let [name @(re-frame/subscribe [::subs/name])
        long-loading-data (re-frame/dispatch [:long-geo-fetch])
        open? (r/atom false)]
    (fn []
      [:div
       [:h1
        "Hello from " name]
       [:button {:on-click (fn [_]
                             (reset! open? true))} "Load example"]
       (when @open?
         [example-panel])])))
