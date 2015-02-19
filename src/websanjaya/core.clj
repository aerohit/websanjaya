(ns websanjaya.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :refer [render]]
            [clojure.java.io :as io]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (str "{\"data\": \"some message\"}")})

(defn home [request]
  (render (io/resource "index.html") request))

(defn hacker-news []
  "Response from hacker news")

(defn reddit []
  "Response from reddit")

(defroutes site-routes
  (GET "/"            [] home)
  (route/resources "/static")
  (route/not-found "<h1>Page not found<h1>"))

(defroutes api-routes
  (context "/api" []
           (GET "/hacker-news" request (json-response (hacker-news)))
           (GET "/reddit" request (json-response (reddit)))
           (ANY "*" [] (route/not-found "Endpoint doesn't exist"))))

(def rest-api (handler/api api-routes))
(def site (handler/site site-routes))

(def site-and-api (routes rest-api site))
