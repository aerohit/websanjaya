(ns websanjaya.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :refer [render]]
            [ring.middleware.json :as json]
            [ring.util.response :refer [resource-response response]]
            [clojure.java.io :as io]))

(defn json-response [data & [status]]
  (response {:data "Greetings from HN"}))

(defn home [request]
  (resource-response "index.html"))

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

(def rest-api (-> (handler/api api-routes)
                  (json/wrap-json-body)
                  (json/wrap-json-response)))

(def site (handler/site site-routes))

(def site-and-api (routes rest-api site))
