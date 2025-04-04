# ui-router [![GitHub Actions status |pink-gorilla/ui-router](https://github.com/pink-gorilla/ui-router/workflows/CI/badge.svg)](https://github.com/pink-gorilla/ui-router/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/ui-router.svg)](https://clojars.org/org.pinkgorilla/ui-router)

A simple front-end page-router using reitit.

## Reitit Routing

- Reitit is a fast router
- it supports schema-coercion 


:cljs-routes-reitit demo.page/routes

## Extension Setup

- Extensions can define additional routes, example: `:cljs-routes-reitit demo.page/routes`
- all additional routes are discovered via a cljs-service, and the router is started with all 
  discovered routes.




## Demo

in demo directory:
```
clj -X:webly:npm-install

One of the following 3:
clj -X:webly:page:compile
clj -X:webly:page:run

clj -X:webly:auth:compile
clj -X:webly:auth:run




```

