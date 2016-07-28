(defproject clanhr/auth "1.13.1"
  :description "ClanHR's Auth Library"
  :url "https://github.com/clanhr/auth"
  :license {:name "The MIT License"
            :url "file://LICENSE"}
  :dependencies[[org.clojure/clojure "1.8.0"]
                [environ "1.0.2"]
                [ring/ring-core "1.4.0"]
                [ring/ring-mock "0.3.0"]
                [clanhr/result "0.11.0"]
                [clanhr/reply "0.11.0"]
                [clanhr/clanhr-api "1.7.3"]
                [clj-jwt "0.1.1"]
                [clj-time "0.11.0"]]
  :plugins [[lein-environ "1.0.2"]]
  :aliases {"autotest" ["trampoline" "with-profile" "+test" "test-refresh"]}
  :profiles {:test {:env {:secret "test_secret"}
                    :plugins [[com.jakemccrary/lein-test-refresh "0.15.0"]]}
             :dev {:env {:secret "dev_secret"}}})
