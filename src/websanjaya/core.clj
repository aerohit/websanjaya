(ns websanjaya.core
  (:require [ring.adapter.jetty :as rjetty]))

(defn app-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello from Ring"})

(defn -main
  [& args]
  (rjetty/run-jetty app-handler {:port 4000}))
