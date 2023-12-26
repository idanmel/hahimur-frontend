(ns hahimur-frontend.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::loading-matches?
 (fn [db]
   (:loading-matches? db)))

(re-frame/reg-sub
 ::fetch-matches-success
 (fn [db]
   (:matches db)))

(re-frame/reg-sub
 ::fetch-matches-error
 (fn [db]
   (:fetch-matches-error db)))

(re-frame/reg-sub
 ::logged-in?
 (fn [db]
   (:logged-in? db)))

(re-frame/reg-sub
 ::token
 (fn [db]
   (:token db)))