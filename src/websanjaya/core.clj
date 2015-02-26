(ns websanjaya.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :refer [render]]
            [ring.middleware.json :as json]
            [ring.util.response :refer [resource-response response]]
            [clojure.java.io :as io]
            [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]))

(def ^:dynamic *HN_URL* "https://news.ycombinator.com/")

(defn json-response [data & [status]]
  (response {:data data}))

(defn home [request]
  (resource-response "index.html"))

; TODO: make this asynchronous
; TODO: also figure out why doesn't clj-http.client doesn't work
(defn get-response [url]
  ;(html/html-resource (client/get url))
  (html/html-resource (java.net.URL. url)))

(defn hn-title-url [node]
  {:title (html/text node)
   :url (:href (:attrs node))})

(defn parse-hacker-news [content]
  (map hn-title-url (html/select content [:td.title :a])))

; TODO: make this asynchronous
(defn hacker-news []
  (let [response (get-response *HN_URL*)]
    (parse-hacker-news response)))

(defn reddit-url-for [topic]
  (str "http://www.reddit.com/r/" topic))

(defn reddit-title-url [node]
  {:title (html/text node)
   :url (:href (:attrs node))})

(defn parse-reddit [content]
  (map reddit-title-url (html/select content [:a.title.may-blank])))

; Reddit throws 429, bastard!
(defn reddit [topic]
  (let [;response (get-response (reddit-url-for topic))
        response (html/html-resource (java.io.StringReader. (slurp "vim.html")))]
    (parse-reddit response)))

(defroutes site-routes
  (GET "/"            [] home)
  (route/resources "/static")
  (route/not-found "<h1>Page not found<h1>"))

(defroutes api-routes
  (context "/api" []
           (GET "/hacker-news" request (json-response (hacker-news)))
           (GET "/reddit/:topic" [topic] (json-response (reddit topic)))
           (ANY "*" [] (route/not-found "Endpoint doesn't exist"))))

(def rest-api (-> (handler/api api-routes)
                  (json/wrap-json-body)
                  (json/wrap-json-response)))

(def site (handler/site site-routes))

(def site-and-api (routes rest-api site))
