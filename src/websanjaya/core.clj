(ns websanjaya.core
  (:require [ring.adapter.jetty :as rjetty]
            [websanjaya.ring-handler :as rhandler]
            [websanjaya.compojure-handler :as chandler]))

(defn -main
  [& args]
  ;(rjetty/run-jetty rhandler/handler {:port 4000})
  (rjetty/run-jetty #'chandler/app {:port 4000}))
