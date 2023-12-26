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

(def match {:date "2021-06-11T21:00:00Z"
            :home-team {:id 1 :name "Turkey" :flag "https://flagcdn.com/w80/tr.png"}
            :away-team {:id 2 :name "Italy" :flag "https://flagcdn.com/w80/it.png"}})

(defn display-team [{:keys [name flag]}]
  [:div.dt.dt--fixed
   [:div.dtc.tc.v-mid.pv4 [:img {:src flag}]]
   [:div.dtc.tc.v-mid.pv4 name]
   [:div.dtc.tc.v-mid.pv4
    [:input#wow {:type "number" :name "wow" :min 0 :max 50}]]])

(defn display-match [{:keys [date home-team away-team] :as data}]
  [:div.tc date " " (display-team home-team) " " (display-team away-team)])

(defn display-login-form []
  (let [gettext (fn [e] (-> e .-target .-value))
        emit    (fn [e] (re-frame/dispatch [::events/update-token (gettext e)]))]
    [:div.flex-grow.pa3.flex.items-center
     [:input#token {:on-change emit :type "text" :name "token" :placeholder "Token"}]
     [:button {:on-click #(re-frame/dispatch [::events/login])} "Login"]]))

(defn display-log-out-from []
  [:div.flex-grow.pa3.flex.items-center
   [:button {:on-click #(re-frame/dispatch [::events/logout])} "Logout"]])

(defn display-nav []
  (let [logged-in? (re-frame/subscribe [::subs/logged-in?])]
    [:nav.flex.justify-between.bb.bg-near-black
     [:div.white-70.hover-white.no-underline.flex.items-center.pa3 "Hahimur"]
     (if @logged-in?
       (display-log-out-from)
       (display-login-form))]))

(defn main-panel []
  (let [loading-matches? (re-frame/subscribe [::subs/loading-matches?])
        matches (re-frame/subscribe [::subs/fetch-matches-success])
        matches-error (re-frame/subscribe [::subs/fetch-matches-error])]
    [:div
     (display-nav)
     [:div.pa4-l.pad-m.pa4-ns
      [:h1.f1 "Euro 2020"]
      [:h3.f1 "Games"]
      (when @loading-matches? "Loading...")
      (when @matches-error "Nice")
      (map display-match [match])]]))
    ;;  [:button.bw0.br2.bg-blue.pa2.white.fw1.tc.ttu {:on-click #(re-frame/dispatch [::events/update-test-chart test-chart])} "Make API call"]
    ;;  [:button {:on-click #(re-frame/dispatch [::events/update-name "W"])} "Update Name"]
    ;;  (chart-outer chart-data)]))
