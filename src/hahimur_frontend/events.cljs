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

(re-frame/reg-event-db
 ::update-name
 (fn [db [_ name]]
   (assoc db :name name)))

(re-frame/reg-event-db
 ::update-test-chart
 (fn [db [_ test-chart]]
   (assoc db :test-chart test-chart)))

(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::fetch-tournament                      ;; usage:  (dispatch [:handler-with-http])
 (fn [{:keys [db]} _]                    ;; the first param will be "world"
   {:db   (assoc db :show-twirly true)   ;; causes the twirly-waiting-dialog to show??
    :http-xhrio {:method          :get
                 :uri             "https://web-production-6d94.up.railway.app/tournaments/?format=json"
                 :timeout         8000                                           ;; optional see API docs
                 :response-format (ajax/json-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                 :on-success      [:good-http-result]
                 :on-failure      [:bad-http-result]}}))