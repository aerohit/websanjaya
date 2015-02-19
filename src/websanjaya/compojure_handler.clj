(ns websanjaya.compojure-handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

(defroutes app
  (GET "/"        [] "This is the home page")
  (GET "/hello"   [] "Hello from Ring!")
  (GET "/goodbye" [] "Goodbye from Ring!")
  (route/not-found   "I don't know you"))
