(ns websanjaya.core
  (:require [ring.adapter.jetty :as rjetty]
            [websanjaya.ring-handler :as rhandler]
            [websanjaya.compojure-handler :as chandler]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as json]
            [ring.util.response :refer [response]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :refer [render]]
            [clojure.java.io :as io]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/hal+json; charset=utf-8"}
   :body (str "data:" data)}
  ;(json/wrap-json-response #(response {:data data}))
  )

(defn home [request]
  (render (io/resource "index.html") request))

(defn hacker-news []
  "Response from hacker news")

(defn reddit []
  "Response from reddit")

(defroutes site-routes []
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

(def site-and-api (routes
                    ;site
                    rest-api
                    ))

;(defn -main
  ;[& args]
  ;;(rjetty/run-jetty rhandler/handler {:port 4000})
  ;;(rjetty/run-jetty #'chandler/app {:port 4000})
  ;;(rjetty/run-jetty (wrap-defaults app-routes site-defaults) {:port 4000})
  ;(-> (handler/api app-routes)
      ;(json/wrap-json-body)
      ;(json/wrap-json-params)
      ;(json/wrap-json-response))
  ;)
