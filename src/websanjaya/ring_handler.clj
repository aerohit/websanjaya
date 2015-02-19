(ns websanjaya.ring-handler)

(defn response-for [request]
  (let [uri (:uri request)]
    (cond
      (= uri "/")        "This is the home page"
      (= uri "/hello")   "Hello from Ring!"
      (= uri "/goodbye") "Goodbye from Ring!"
      :else              "I don't know you")))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (response-for request)})
