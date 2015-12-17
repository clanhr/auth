(defproject clanhr/auth "1.6.0"
  :description "ClanHR's Auth Library"
  :url "https://github.com/clanhr/auth"
  :license {:name "The MIT License"
            :url "file://LICENSE"}
  :dependencies [[org.clojure/clojure "1.7.0-beta2"]
                 [environ "1.0.1"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-mock "0.3.0"]
                 [clanhr/result "0.10.3"]
                 [clanhr/reply "0.9.0"]
                 [clanhr/clanhr-api "1.2.0"]
                 [clj-jwt "0.1.1"]
                 [clj-time "0.11.0"]]
  :plugins [[lein-environ "1.0.0"]]
  :profiles {:test {:env {:secret "test_secret"}}
             :dev {:env {:secret "dev_secret"}}})
