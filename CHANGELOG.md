# Changelog

## Development

* Require an explicit execution context for UdpStatsdLogger's constructor, 
  for bulkheading calls to the statsd library. Calling the statsd lib from 
  the request thread can cause severe performance degradation when statsd 
  servers are inaccessible (even though the implementation uses NIO).

## 2.0.4

* Added Changelog.
* Switched to HS local repository.
