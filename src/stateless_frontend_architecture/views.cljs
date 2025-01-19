(ns stateless-frontend-architecture.views
  (:require
   [re-frame.core :as re-frame]
   [stateless-frontend-architecture.subs :as subs]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      "Hello from " @name]
     ]))
