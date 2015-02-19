(ns websanjaya.core
  (:require [ring.adapter.jetty :as rjetty]
            [websanjaya.ring-handler :as rhandler]))

(defn -main
  [& args]
  (rjetty/run-jetty rhandler/handler {:port 4000}))
