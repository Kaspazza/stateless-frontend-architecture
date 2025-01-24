(ns stateless-frontend-architecture.views.often-updates-form
  (:require
   [re-frame.core :as re-frame]))

;; Subs
(re-frame/reg-sub
 ::form
 (fn [db [_ id]]
   (get-in db [:form id] "")))

(re-frame/reg-sub
 ::form-is-valid?
 (fn [db [_ form-ids]]
   (every? #(get-in db [:form %]) form-ids)))

(re-frame/reg-sub
 ::animals
 (fn [db]
   (get db :animals [])))

;;Events
(re-frame/reg-event-db
 ::update-form
 (fn [db [_ id val]]
   (assoc-in db [:form id] val)))

(re-frame/reg-event-db
 ::save-form
 (fn [db]
   (let [form-data (:form db)
         animals (get db :animals [])
         updated-animals (conj animals form-data)]
     (-> db
         (assoc :animals updated-animals)
         (dissoc :form)))))

;;View
(def animal-types ["Dog" "Cat" "Mouse"])

(defn animal-list []
  (let [animals @(re-frame/subscribe [::animals])]
    [:div
     [:h1 "animal list"]
     [:ul
      (map (fn [{:keys [animal-type animal-name]}]
             [:li {:key animal-name} (str animal-name " (" animal-type ")")]) animals)]]))

(defn text-input [id label]
  (let [value (re-frame/subscribe [::form id])]
    [:div.field
     [:label.label label]
     [:div.control
      [:input.input {:value @value
                     :on-change #(re-frame/dispatch [::update-form id (-> % .-target .-value)])
                     :type "text" :placeholder "Text input"}]]]))

(defn select-input [id label options]
  (let [value (re-frame/subscribe [::form id])]
    [:div.field
     [:label.label label]
     [:div.control
      [:div.select
       [:select {:value @value :on-change #(re-frame/dispatch [::update-form id (-> % .-target .-value)])}
        [:option {:value ""} "Please select"]
        (map (fn [o] [:option {:key o :value o} o]) options)]]]]))

(defn main-panel []
  (let [is-valid? @(re-frame/subscribe [::form-is-valid? [:animal-name :animal-type]])]
    [:div.section
     [animal-list]
     [text-input :animal-name "Animal Name"]
     [select-input :animal-type "Animal Type" animal-types]
     [:button.button.is-primary {:disabled (not is-valid?)
                                 :on-click #(re-frame/dispatch [::save-form])} "Save"]]))
