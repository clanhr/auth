(defproject clanhr/auth "1.11.4"
  :description "ClanHR's Auth Library"
  :url "https://github.com/clanhr/auth"
  :license {:name "The MIT License"
            :url "file://LICENSE"}
  :dependencies.edn "https://raw.githubusercontent.com/clanhr/dependencies/master/dependencies.edn"
  :dependencies []

  :dependency-sets [:clojure
                    :security
                    :common
                    :ring
                    :clanhr]
  :plugins [[lein-environ "1.0.0"]
            [clanhr/shared-deps "0.2.6"]]
  :profiles {:test {:env {:secret "test_secret"}}
             :dev {:env {:secret "dev_secret"}}})
