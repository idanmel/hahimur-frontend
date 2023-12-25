(ns hahimur-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [hahimur-frontend.events :as events]
   [hahimur-frontend.subs :as subs]
   [reagent.core :as r]
   [reagent.dom :as rd]
   ["highcharts" :as highcharts]))

(defn mount-chart [comp]
  (highcharts/chart (rd/dom-node comp) (clj->js (r/props comp))))

(defn update-chart [comp]
  (mount-chart comp))

(defn chart-inner []
  (r/create-class
   {:component-did-mount   mount-chart
    :component-did-update  update-chart
    :reagent-render        (fn [comp] [:div.w-33-m])}))

(defn chart-outer [config]
  [chart-inner @config])

(def test-chart {:chart {:type   :bar}
                     :title  {:text "Chart title there"}
                     :xAxis  {:categories ["Apples", "Bananas", "Oranges"]}
                     :yAxis  {:title {:text "Fruit eaten"}}
                     :series [{:name "Jane" :data [1, 0, 4]}
                              {:name "John" :data [5, 7, 3]}]})

(def match {
            :date "2021-06-11T21:00:00Z" 
            :home-team {:id 1 :name "Turkey"}
            :away-team {:id 2 :name "Germany"}})

(defn display-team [{:keys [name flag]}]
  [:div name " " flag])

(defn display-match [{:keys [date home_team away_team] :as data}]
  (println data)
  [:div date " " (display-team home_team) " " (display-team away_team)])

(defn main-panel []
  (let [loading-matches? (re-frame/subscribe [::subs/loading-matches?])
        matches (re-frame/subscribe [::subs/fetch-matches-success])
        matches-error (re-frame/subscribe [::subs/fetch-matches-error])]
    [:div.pa4-l.pad-m.pa4-ns
     [:h1.f1 "Hahimur - Euro 2020"]
     [:h3.f1 "Games"]
     (when @loading-matches? "Loading...")
     (when @matches-error "Nice") 
     (map display-match @matches)]))
    ;;  [:button.bw0.br2.bg-blue.pa2.white.fw1.tc.ttu {:on-click #(re-frame/dispatch [::events/update-test-chart test-chart])} "Make API call"]
    ;;  [:button {:on-click #(re-frame/dispatch [::events/update-name "W"])} "Update Name"]
    ;;  (chart-outer chart-data)]))
