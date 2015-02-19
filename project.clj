(defproject websanjaya "0.1.0-SNAPSHOT"
  :description "Web crawler for HN, Reddit etc."
  :url "https://github.com/aerohit/websanjaya"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring "1.3.2"]]
  :main ^:skip-aot websanjaya.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
