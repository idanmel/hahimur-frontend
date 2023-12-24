(ns hahimur-frontend.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]  
   [hahimur-frontend.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))


(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::fetch-matches                      ;; usage:  (dispatch [:handler-with-http])
 (fn [{:keys [db]} _]                    ;; the first param will be "world"
   {:db   (assoc db :loading-matches? true)   ;; causes the twirly-waiting-dialog to show??
    :http-xhrio {:method          :get
                 :uri             "https://web-production-6d94.up.railway.app/tournaments/?format=json"
                 :timeout         8000                                           ;; optional see API docs
                 :response-format (ajax/json-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                 :on-success      [:fetch-matches-success]
                 :on-failure      [:fetch-matches-error]}}))

(re-frame/reg-event-db
 ::fetch-matches-success
 (fn [db [_ {:keys [data]}]]
   (assoc db 
          :matches data 
          :loading false)))

(re-frame/reg-event-db
 ::fetch-matches-error
 (fn [db [_ data]]
   (assoc db
          :matches data
          :loading false)))