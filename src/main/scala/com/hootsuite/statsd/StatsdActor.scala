package com.hootsuite.statsd

import akka.actor.Actor

import etsy.StatsdClient

/**
 * Basic actor that will send all messages to statsd.
 * 
 * @param statsdHost the statsd hostname
 * @param statsdPort the UDP port to connect to on statsdHost
 * @param prefix the base prefix string for all statsd events.
 */
class StatsdActor(statsdHost: String, statsdPort: Int, prefix: String) extends Actor {

  val client = new StatsdClient(statsdHost, statsdPort, null)
  val cleanPrefix = if (prefix.endsWith(".")) prefix.substring(0, prefix.length - 1) else prefix

  def receive = {
    case Timer(key, time) => client.timing(s"${prefix}.${key}", time)
    case Increment(key) => client.increment(s"${prefix}.${key}")
    case Decrement(key) => client.decrement(s"${prefix}.${key}")
  }
}

