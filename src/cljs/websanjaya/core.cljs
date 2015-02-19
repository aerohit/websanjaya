(ns websanjaya.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [figwheel.client :as fw]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))

(def ^:const *HN_URL* "/api/hacker-news")

(defonce hn-response (atom nil))

(defn scrape-hn []
  (go (let [hn-content (<! (http/get *HN_URL*))]
        (reset! hn-response (:data (:body hn-content))))))

(defn hn-link [node]
  [:p {:key (:title node)}
   [:a {:href (:url node)}
    (:title node)]])

(defn hn-message []
  (when @hn-response
    [:div
     [:p "Latest posts on HN:"]
     (map hn-link (take 5 @hn-response))]))

(defn hacker-news []
  [:div.hacker-news "Hacker News"
   [hn-message]])

(defn init-data []
  (scrape-hn))

(defn main []
  (init-data)
  (reagent/render-component [hacker-news]
                            (.getElementById js/document "reagent-root")))

(enable-console-print!)
(main)

(fw/start {:websocket-url   "ws://localhost:4449/figwheel-ws"
           :on-jsload (fn [] (println "Reloaded!"))})
