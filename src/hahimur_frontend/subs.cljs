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
   (:fetch-matches-success db)))

(re-frame/reg-sub
 ::fetch-matches-error
 (fn [db]
   (:fetch-matches-error db)))

