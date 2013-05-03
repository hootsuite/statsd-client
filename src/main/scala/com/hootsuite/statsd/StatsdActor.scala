package com.hootsuite.statsd

import akka.actor.Actor

import etsy.StatsdClient

/**
 * Basic actor that will send all messages to statsd.
 * @param statsdHost the statsd hostname
 * @param statsdPort the UDP port to connect to on statsdHost
 * @param prefix the base prefix string for all statsd events.
 */
class StatsdActor(statsdHost: String, statsdPort: Int, prefix: String) extends Actor {

  val client = new StatsdClient(statsdHost, statsdPort, null)
  val cleanPrefix = if(prefix.endsWith(".")) prefix.substring(0, prefix.length - 1) else prefix

  def receive = {
    case Timer(key, time) => client.timing(s"${prefix}.${key}", time)
    case Increment(key) => client.increment(s"${prefix}.${key}")
    case Decrement(key) => client.decrement(s"${prefix}.${key}")
  }
}

/**
 * Statsd actor that will send the given percentage of messages to statsd.
 * @param statsdHost the statsd hostname
 * @param statsdPort the UDP port to connect to on statsdHost
 * @param sampleRate percentage chance of sending any given message, example 50, 80, 10, etc
 * @param prefix the base prefix string for all statsd events.
 */
class StatsdSamplingActor(statsdHost: String, statsdPort: Int, sampleRate: Int, prefix: String) extends Actor {

  val client = new StatsdClient(statsdHost, statsdPort, null)
  val cleanPrefix = if(prefix.endsWith(".")) prefix.substring(0, prefix.length - 1) else prefix

  val r = new java.util.Random

  def receive = {
    case msg: StatsdMessage if send => sendEvent(msg)
    case _ => {}
  }

  def send: Boolean = r.nextInt(100) < sampleRate

  def sendEvent(msg: StatsdMessage) {
    msg match {
      case Timer(key, time) => client.timing(s"${prefix}.${key}", time)
      case Increment(key) => client.increment(s"${prefix}.${key}")
      case Decrement(key) => client.decrement(s"${prefix}.${key}")
    }
  }
}
