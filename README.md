# ClanHR's Auth Library [![Build Status](https://travis-ci.org/clanhr/auth.svg)](https://travis-ci.org/clanhr/auth)

[![Clojars Project](http://clojars.org/clanhr/auth/latest-version.svg)](http://clojars.org/clanhr/auth)

Auth utilities on top of JWT and ring middlewares.

## Install

Add the following dependency to your `project.clj`:

    [clanhr/auth "0.4.0"]

## Usage

The `auth` middleware should be applied to your Ring handler:

```clojure
(:require 'clanhr.auth.auth-middleware :as auth)

(def app
  (-> handler
      (auth/run)))
```

Any request that don't have a valid JWT will be refused with an unauthorized response.

The api to create and validate tokens is given by `token-for` and `valid?` inside of `clanhr.auth.core` namespace.
