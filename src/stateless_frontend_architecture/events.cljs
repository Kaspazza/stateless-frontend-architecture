(ns stateless-frontend-architecture.events
  (:require
   [re-frame.core :as re-frame]
   [stateless-frontend-architecture.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
