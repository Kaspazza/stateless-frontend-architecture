(ns stateless-frontend-architecture.events
  (:require
   [re-frame.core :as re-frame]
   [stateless-frontend-architecture.db :as db]
   [day8.re-frame.http-fx]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

;;TODO
;;Ton of elements fetch
;;TODO
;;Regular form component


;;Form component
(re-frame/reg-event-db
 :save-form
 (fn [db [_ form-data]]
   (assoc db :form-data form-data)))
