(ns websanjaya.maps
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [figwheel.client :as fw]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))

(def acco-icon-url "//www.vakantiediscounter.nl/atomic/images/pin-acco-small.png")

(def *url-params* {:departuredate "2015-04-19"
                   ;:city "barcelona"
                   :city "madrid"
                   :flexibility 2
                   :room "2_0_0"
                   :sort "price_asc"
                   :transporttype "VL"
                   :trip_duration_range "6-10"})

(def *some-url* "/api/pricerequest")

(defn fetch-price []
  (go (let [price-response (<! (http/get *some-url* {:query-params *url-params*}))]
        (println price-response))))

(defonce dyn-map (atom false))
(defonce marker  (atom false))

(defn gen-position []
  (js/google.maps.LatLng. (js/parseFloat -34.397)
                          (js/parseFloat 150.644)))

(defn gen-dyn-map-args [& {:as extra-opts}]
  (clj->js (merge {:center (gen-position)
                   :zoom 13
                   :scrollwheel false
                   :draggable false} extra-opts)))

(defn init-dyn-map! []
  (println :init-dyn-map!)
  (let [new-dyn-map (js/google.maps.Map.
                      (js/document.getElementById "googlemap")
                      (gen-dyn-map-args))]
    (reset! dyn-map new-dyn-map)
    new-dyn-map))

(defn init-dyn-marker! [dyn-map]
  (let [new-marker (js/google.maps.Marker.
                     (clj->js
                       {:position (gen-position)
                        :title (:name "Google flights")
                        :icon {:url acco-icon-url}}))]
    (reset! marker new-marker)
    (.setMap new-marker dyn-map)
    new-marker))

(defn init-dyn-all! []
  (-> (init-dyn-map!)
      (init-dyn-marker!))
  true)

(def maps-component
  (with-meta
    (fn []
      [:div#googlemap])
    {:component-did-mount init-dyn-all!}))

(defn component []
  [:div
   [maps-component]])

(defn init-data []
  (fetch-price))

(defn main []
  (init-data)
  (reagent/render-component [component]
                            (.getElementById js/document "reagent-root")))

(enable-console-print!)
(main)

(fw/start {:websocket-url   "ws://localhost:4449/figwheel-ws"
           :on-jsload (fn [] (println "Reloaded!"))})
