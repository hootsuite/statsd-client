# Statsd Client #


## What is statsd-client? #

statsd-client is a simple Scala wrapper around [Etsy's fantastic Java Statsd Client](https://github.com/etsy/statsd/tree/master/examples)
for sending statistics update UDP packets to a statsd server.


## Key Features ##

* Typesafe configuration
* Debug and no-op modes
* Nonblocking IO


## Installation ##

Use SBT to package the .jar and include it in your project. Add the StatsdReporting trait and define the client
implementation in classes that need statsd reporting.


## Configuration ##

Override the values in reference.conf in your application specific Typesafe configuration file.



