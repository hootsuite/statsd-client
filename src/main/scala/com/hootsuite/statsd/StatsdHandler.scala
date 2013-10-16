package com.hootsuite.statsd

import com.typesafe.config.Config

import etsy.StatsdClient

/**
 * Basic handler that will send all messages to statsd.
 * 
 * @param statsdHost the statsd hostname
 * @param statsdPort the UDP port to connect to on statsdHost
 * @param prefix the base prefix string for all statsd events.
 */
class StatsdHandler(config: Config) {
  private val host = config.getString("statsd.host")
  private val port = config.getInt("statsd.port")
  private val client = new etsy.StatsdClient(host, port, null)

  private val prefix = {
    val hpfx = config.getString("statsd.hostpfx")
    val pfx = config.getString("statsd.prefix")
    s"$pfx.$hpfx.".replaceAll("\\.{1,}", ".")
  }

  def inc(k: String, v: Int = 1): Unit = client.increment(s"${prefix}${k}", v)
  def dec(k: String, v: Int = 1): Unit = client.decrement(s"${prefix}${k}", v)
  def gauge(k: String, v: Int): Unit = client.gauge(s"${prefix}${k}", v)
  def timer(k: String, v: Int): Unit = client.timing(s"${prefix}${k}", v)

  override def toString =
    "StatsdHandler(StatsdClient(%s, %s), %s)" format(host, port, prefix)
}
