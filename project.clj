(defproject websanjaya "0.1.0-SNAPSHOT"
  :description "Web crawler for HN, Reddit etc."
  :url "https://github.com/aerohit/websanjaya"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.4"]
                 [org.clojure/clojurescript "0.0-2850"]
                 [compojure "1.3.1"]
                 [reagent "0.5.0-alpha3"]
                 [figwheel "0.2.3-SNAPSHOT"]
                 [cljs-http "0.1.26"]
                 [ring/ring-json "0.3.1"]
                 [clj-http "1.0.1"]
                 [enlive "1.1.5"]]
  :main ^:skip-aot websanjaya.core
  :target-path "target/%s"
  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-figwheel "0.2.3-SNAPSHOT"]
            [lein-ring "0.9.1"]]
  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/app.js"
                           :output-dir "resources/public/js/out"
                           :source-map true
                           :optimizations :none
                           :asset-path "/static/js/out"
                           :warnings true
                           :main "websanjaya.core"
                           :pretty-print true
                           :preamble  ["reagent/react.js"]}}]}
  :figwheel {:server-port 4449
             :server-logfile "logs/figwheel_server.log"}
  :ring {:handler websanjaya.core/site-and-api}
  :profiles {:uberjar {:aot :all}})
