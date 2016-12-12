(defproject clanhr/auth "1.23.0"
  :description "ClanHR's Auth Library"
  :url "https://github.com/clanhr/auth"
  :license {:name "The MIT License"
            :url "file://LICENSE"}
  :dependencies[[org.clojure/clojure "1.8.0"]
                [environ "1.1.0"]
                [ring/ring-core "1.5.0"]
                [ring/ring-mock "0.3.0"]
                [clanhr/result "0.16.0"]
                [clanhr/reply "1.1.0"]
                [clanhr/clanhr-api "1.10.1"]
                [clj-jwt "0.1.1"]
                [clj-time "0.12.2"]]
  :plugins [[lein-environ "1.1.0"]]
  :aliases {"autotest" ["trampoline" "with-profile" "+test" "test-refresh"]}
  :profiles {:test {:env {:secret "test_secret"}
                    :plugins [[com.jakemccrary/lein-test-refresh "0.15.0"]]}
             :dev {:env {:secret "dev_secret"}}})
