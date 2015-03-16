# Changelog

## 2.1.0

* Require an explicit execution context for UdpStatsdLogger's constructor, 
  for bulkheading calls to the statsd library. Calling the statsd lib from 
  the request thread can cause severe performance degradation when statsd 
  servers are inaccessible (even though the implementation uses NIO).

* Removed hardcoded default host = "graph.hootsuitemedia.com".

## 2.0.4

* Added Changelog.
* Switched to HS local repository.
