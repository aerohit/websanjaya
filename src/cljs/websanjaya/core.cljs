(ns websanjaya.core
  (:require [reagent.core :as reagent]
            [figwheel.client :as fw]))

(defn hacker-news []
  [:div.hacker-news "Hacker News"
   [:ul
    [:li "Item 1"]
    [:li "Item 2"]]])

(defn main []
  (reagent/render-component [hacker-news]
                            (.getElementById js/document "reagent-root")))

(enable-console-print!)
(main)

(fw/start {:websocket-url   "ws://localhost:4449/figwheel-ws"
           :on-jsload (fn [] (println "Reloaded!"))})
