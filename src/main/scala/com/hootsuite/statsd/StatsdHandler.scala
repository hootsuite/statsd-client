package com.hootsuite.statsd

import etsy.StatsdClient

/**
 * Basic handler that will send all messages to statsd.
 * 
 * @param statsdHost the statsd hostname
 * @param statsdPort the UDP port to connect to on statsdHost
 * @param prefix the base prefix string for all statsd events.
 */
class StatsdHandler(statsdHost: String, statsdPort: Int, prefix: String) {

  private val client = new StatsdClient(statsdHost, statsdPort, null)
  private val cleanPrefix = if (prefix.endsWith(".")) prefix.substring(0, prefix.length - 1) else prefix

  def sendTimer(timer: Timer) {
    client.timing(s"${prefix}.${timer.key}", timer.time)
  }

  def sendIncrement(increment: Increment) {
    client.increment(s"${prefix}.${increment.key}")
  }

  def sendDecrement(decrement: Decrement) {
    client.decrement(s"${prefix}.${decrement.key}")
  }

}
