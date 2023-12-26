(ns hahimur-frontend.events
  (:require
   [clojure.walk :as walk]
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]
   [camel-snake-kebab.core :as csk]
   [hahimur-frontend.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))


(re-frame/reg-event-fx
 ::fetch-matches
 (fn [{:keys [db]} _]
   {:db (assoc db :loading-matches? true)
    :http-xhrio {:method          :get
                 :uri             "https://web-production-6d94.up.railway.app/tournaments/1/matches?format=json"
                 :timeout         8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::fetch-matches-success]
                 :on-failure      [::fetch-matches-error]}}))


(defn ->kebab-case-map-keywords [stuff]
  (walk/postwalk #(if (keyword? %) (csk/->kebab-case-keyword %) %) stuff))

(re-frame/reg-event-db
 ::fetch-matches-success
 (fn [db [_ {:keys [data]}]]
   (assoc db
          :matches (->kebab-case-map-keywords data)
          :loading-matches? false)))

(re-frame/reg-event-db
 ::fetch-matches-error
 (fn [db [_ result]]
   (assoc db
          :fetch-matches-error result
          :loading-matches? false)))

(re-frame/reg-event-db
 ::update-token
 (fn [db [_ token]]
   (assoc db :token token)))

(re-frame/reg-event-fx
 ::login
 (fn [{:keys [db]} _]
   {:db db
    :http-xhrio {:method          :get
                 :uri             "https://web-production-6d94.up.railway.app/tournaments/1/predictions"
                 :timeout         8000
                 :headers         {:token (:token db)}
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::login-success]
                 :on-failure      [::login-error]}}))

(re-frame/reg-event-db
 ::login-success
 (fn [db [_ {:keys [data]}]]
   (assoc db
          :logged-in? true
          :predictions data)))

(re-frame/reg-event-db
 ::login-error
 (fn [db [_ _]]
   (assoc db :logged-in? false)))

(re-frame/reg-event-db
 ::logout
 (fn [db [_ _]]
   (assoc db :logged-in? false)))

(re-frame/reg-event-fx
 ::save-predictions
 (fn [{:keys [db]} _]
   {:db (assoc db :saving-predictions? true)
    :http-xhrio {:method          :post
                 :uri             "https://web-production-6d94.up.railway.app/tournaments/1/predictions"
                 :timeout         8000
                 :params          {:predictions (:predictions db)}
                 :headers         {:token (:token db)}
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::saving-predictions-success]
                 :on-failure      [::saving-predictions-error]}}))

(re-frame/reg-event-db
 ::saving-predictions-error
 (fn [db [_ _]]
   (assoc db :saving-predictions? false)))

(re-frame/reg-event-db
 ::saving-predictions-success
 (fn [db [_ _]]
   (assoc db :saving-predictions? false)))
