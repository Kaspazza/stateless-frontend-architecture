(ns stateless-frontend-architecture.views.multiple-elements
  (:require
   [re-frame.core :as re-frame]))

(def example-items (vec (map
                         (fn [num] {:id num :text (str "Number" num "item") :checked? false})
                         (range 1000))))

(def updated-items (map
                    #(assoc % :on-change [:item (:id %) (not (:checked? %))])
                    example-items))

(defn item [{:keys [text checked? on-change]}]
  [:div {:style {:display "flex"
                 :flex-direction "column"}}
   [:span (str "Item " text)]
   [:label
    [:input {:type      "checkbox"
             :checked    checked?
             :on-change #(re-frame/dispatch on-change)}]
    (str " Checked: " checked?)]])

(defn multiple-items [items]
  [:div
   (for [{:keys [id] :as i} items]
     ^{:key (str id)}
     [item i])])
