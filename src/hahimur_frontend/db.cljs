(ns hahimur-frontend.db)

(def chart-wow {:chart {:type   :column :zoomtype :y}
                :title {:text "Podium"}
                :xAxis {:categories ["Germnay", "France", "Italy"]}
                :yAxis {:title {:text "Fruit eaten"}}
                :series [{:name "First" :data [1, 2, 4]}
                         {:name "Second" :data [5, 7, 3]}]})

(def default-db
  {:name "Idan"
   :test-chart chart-wow
   :loading-matches? true
   :matches []
   :logged-in? false
   :predictions []
   :saving-predictions? false})
