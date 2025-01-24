(ns stateless-frontend-architecture.views.long-loading
  (:require
   [ajax.core :as ajax]
   [re-frame.core :as re-frame]))

;;Events
(re-frame/reg-event-fx
 :long-geo-fetch
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             "https://get.geojs.io/v1/ip/country.json"
                 :timeout         20000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::fetch-delayed-success]
                 :on-failure      [::fetch-activity-failure]}}))

(re-frame/reg-event-fx
 ::fetch-delayed-success
 (fn [_ [_ response]]
   {:dispatch-later [{:ms      10000
                      :dispatch [::fetch-activity-success response]}]}))

(re-frame/reg-event-db
 ::fetch-activity-success
 (fn [db [_ response]]
   (assoc db :random-activity response)))

(re-frame/reg-event-db
  ::fetch-activity-failure
  (fn [db [_ error]]
    (assoc db :random-activity-error error)))
