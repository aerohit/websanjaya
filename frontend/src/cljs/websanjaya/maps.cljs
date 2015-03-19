(ns websanjaya.maps
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [figwheel.client :as fw]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))

(defonce dyn-map (atom false))

(def acco-icon-url "//www.vakantiediscounter.nl/atomic/images/pin-acco-small.png")
(def *some-url* "/api/pricerequest")
(def *req-params* {:departuredate "2015-06-19"
                   :city "madrid"
                   :flexibility 2
                   :room "2_0_0"
                   :sort "price_asc"
                   :transporttype "VL"
                   :trip_duration_range "6-10"})

(def cities ["barcelona" "madrid" "rome"])

(def ams-location
  (js/google.maps.LatLng. (js/parseFloat 52.366667)
                          (js/parseFloat 4.9)))

(defn gen-dyn-map-args [& {:as extra-opts}]
  (clj->js (merge {:center ams-location
                   :zoom 3
                   :scrollwheel false
                   :draggable false} extra-opts)))

(defn init-dyn-map! []
  (let [new-dyn-map (js/google.maps.Map.
                      (js/document.getElementById "googlemap")
                      (gen-dyn-map-args))]
    (reset! dyn-map new-dyn-map)
    new-dyn-map))

(defn position-for [location]
  (js/google.maps.LatLng. (js/parseFloat (:latitude location))
                          (js/parseFloat (:longitude location))))

(defn title-for-marker [data]
  (str (:label (:city data)) " - " (:price data) " euros"))

(defn add-marker [data]
  (println data)
  (let [new-marker (js/google.maps.Marker.
                     (clj->js
                       {:position (position-for (:location data))
                        :title (title-for-marker data)
                        :icon {:url acco-icon-url}}))]
    (.setMap new-marker @dyn-map)))

(defn price-for [city]
  (let [query-params (merge *req-params* {:city city})]
    (go (let [price-response (<! (http/get *some-url* {:query-params query-params}))
              data (:data (:body price-response))]
          (add-marker data)))))

(defn fetch-prices []
  (doall (map price-for cities)))

(def maps-component
  (with-meta
    (fn []
      [:div#googlemap])
    {:component-did-mount init-dyn-map!}))

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
