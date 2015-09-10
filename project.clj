(defproject clanhr/auth "0.4.4"
  :description "ClanHR's Auth Library"
  :url "https://github.com/clanhr/auth"
  :license {:name "The MIT License"
            :url "file://LICENSE"}
  :dependencies [[org.clojure/clojure "1.7.0-beta2"]
                 [environ "1.0.0"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-mock "0.2.0"]
                 [clanhr/result "0.2.0"]
                 [clanhr/reply "0.1.0"]
                 [clj-jwt "0.0.13"]
                 [clj-time "0.9.0"]]
  :plugins [[lein-environ "1.0.0"]]
  :profiles {:test {:env {:secret "test_secret"}}
             :dev {:env {:secret "dev_secret"}}})
