# ui-bidi [![GitHub Actions status |pink-gorilla/ui-bidi](https://github.com/pink-gorilla/ui-bidi/workflows/CI/badge.svg)](https://github.com/pink-gorilla/ui-bidi/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/ui-bidi.svg)](https://clojars.org/org.pinkgorilla/ui-bidi)

**End Users** this project is not for you.

A simple front-end page-router using bidi + pushy.

## Bidi Routing

Bidi has important benefits:
- It is isomorphic (clj and cljs) (forget compojure AND secretary) 
- It has a clear separation of concerns with handlers (both for 
  clj (ring-handler) and cljs (pushy)
- It is bi-directional; this allows making links! 
- It does not use macros, this means routes can be easily tested, transformed, 
  sent between client-server, stored, modified. 
- No linting errors due to macros.
- Easy testing of route definitions, and handler results (including wrapping routes)



## Demo

in demo directory:
```
clj -X:webly:npm-install
clj -X:webly:compile
clj -X:webly:run


```

