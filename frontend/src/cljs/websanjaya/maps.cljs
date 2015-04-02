(ns websanjaya.maps
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [figwheel.client :as fw]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))

(defonce prices-map (atom false))

(def icon-url "//www.vakantiediscounter.nl/atomic/images/pin-acco-small.png")
(def *price-endpoint* "/api/pricerequest")
(def *req-params* {:departuredate "2015-06-19"
                   :city "madrid"
                   :flexibility 2
                   :room "2_0_0"
                   :sort "price_asc"
                   :transporttype "VL"
                   :trip_duration_range "6-10"})

(def cities ["rome" "barcelona" "lissabon" "istanbul" "new_york_city" "londen" "valencia" "praag"])

(def ams-location
  (js/google.maps.LatLng. (js/parseFloat 52.366667)
                          (js/parseFloat 4.9)))

(defn gen-prices-map-args [& {:as extra-opts}]
  (clj->js (merge {:center ams-location
                   :zoom 3
                   :scrollwheel false
                   :draggable false} extra-opts)))

(defn init-prices-map! []
  (let [new-prices-map (js/google.maps.Map.
                      (js/document.getElementById "googlemap")
                      (gen-prices-map-args))]
    (reset! prices-map new-prices-map)
    new-prices-map))

(defn position-for [location]
  (js/google.maps.LatLng. (js/parseFloat (:latitude location))
                          (js/parseFloat (:longitude location))))

(defn title-for-marker [data]
  (str (:label (:city data)) " - " (:price data) " euros"))

(defn add-marker [data]
  (let [new-marker (js/MarkerWithLabel.
                     (clj->js
                       {:position (position-for (:location data))
                        :title (title-for-marker data)
                        :labelContent (title-for-marker data)
                        :labelClass "labels"
                        :icon {:url icon-url}}))]
    (.setMap new-marker @prices-map)))

(defn price-for [city]
  (let [query-params (merge *req-params* {:city city})]
    (go (let [price-response (<! (http/get *price-endpoint* {:query-params query-params}))
              data (:data (:body price-response))]
          (add-marker data)))))

(defn fetch-prices []
  (doall (map price-for cities)))

(def maps-component
  (with-meta
    (fn []
      [:div#googlemap])
    {:component-did-mount init-prices-map!}))

(defn calendar []
  [:div [:h1 "Holidays!"]
   [:input#example1 {:type "text" :placeholder "Select a date"}]])

(defn calendar-did-mount []
  (let [options {:format "dd/mm/yyyy"
                 :autoclose true}
        elem (js/$ "#example1")]
    (.ready (js/$ js/document)
            (fn []
              (-> elem
                  (.datepicker (clj->js options))
                  (.on "changeDate" (fn [e] (prn (subs (.toISOString (.-date e)) 0 10)))))))))

(defn calendar-component []
  (reagent/create-class {:reagent-render calendar
                         :component-did-mount calendar-did-mount}))
(reagent/render-component [calendar-component]
                          (.getElementById js/document "app"))

(defn component []
  [:div
   [maps-component]])

(defn init-data []
  (fetch-prices))

(defn main []
  (init-data)
  (reagent/render-component [component]
                            (.getElementById js/document "reagent-root")))

(enable-console-print!)
(main)

(fw/start {:websocket-url   "ws://localhost:4449/figwheel-ws"
           :on-jsload (fn [] (println "Reloaded!"))})
