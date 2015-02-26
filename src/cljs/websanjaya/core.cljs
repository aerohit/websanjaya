(ns websanjaya.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [figwheel.client :as fw]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))

(def ^:const *HN_URL* "/api/hacker-news")
(def ^:const *REDDIT_VIM* "/api/reddit/vim")

(defonce hn-response (atom nil))
(defonce reddit-response (atom nil))

(defn scrape-hn []
  (go (let [hn-content (<! (http/get *HN_URL*))]
        (reset! hn-response (:data (:body hn-content))))))

(defn scrape-reddit-vim []
  (go (let [content (<! (http/get *REDDIT_VIM*))]
        (reset! reddit-response (:data (:body content))))))

(defn hn-link [node]
  [:p {:key (:title node)}
   [:a {:href (:url node)}
    (:title node)]])

(defn hn-message []
  (when @hn-response
    [:div
     [:p "Latest posts on HN:"]
     (map hn-link (take 5 @hn-response))]))

(defn reddit-message []
  (when @reddit-response
    [:div
     [:p "Latest posts on Reddit:"]
     (map hn-link (take 5 @reddit-response))]))

(defn hacker-news []
  [:div.hacker-news
   "Hacker News"
   [hn-message]])

(defn reddit []
  [:div.reddit
   "Reddit"
   [reddit-message]])

(defn init-data []
  (scrape-hn)
  (scrape-reddit-vim))

(defn component []
  [:div
   [hacker-news]
   [reddit]])

(defn main []
  (init-data)
  (reagent/render-component [component]
                            (.getElementById js/document "reagent-root")))

(enable-console-print!)
(main)

(fw/start {:websocket-url   "ws://localhost:4449/figwheel-ws"
           :on-jsload (fn [] (println "Reloaded!"))})
