(ns websanjaya.core
  (:require [ring.adapter.jetty :as rjetty]
            [websanjaya.ring-handler :as rhandler]
            [websanjaya.compojure-handler :as chandler]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.response :refer [render]]
            [clojure.java.io :as io]))

(defn home [request]
  (render (io/resource "index.html") request))

(defroutes app
  (GET "/" [] home)
  (route/resources "/static")
  (route/not-found "<h1>Page not found<h1>"))

(defn -main
  [& args]
  ;(rjetty/run-jetty rhandler/handler {:port 4000})
  ;(rjetty/run-jetty #'chandler/app {:port 4000})
  (rjetty/run-jetty (wrap-defaults app site-defaults) {:port 4000}))
