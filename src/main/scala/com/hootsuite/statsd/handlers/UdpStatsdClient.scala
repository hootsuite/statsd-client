package com.hootsuite.statsd.handlers

import com.typesafe.config.Config

/**
 * Basic handler that will send all messages to statsd. Wrapper for the Java StatdsClient from Etsy.
 */
class UdpStatsdClient(config: Config) extends com.hootsuite.statsd.StatsdClient {
  private val host = config.getString("statsd.host")
  private val port = config.getInt("statsd.port")
  private val client = new etsy.StatsdClient(host, port)

  private val prefix = {
    val hpfx = config.getString("statsd.hostpfx").takeWhile(_ != '.').mkString
    val pfx = config.getString("statsd.prefix")
    s"$pfx.$hpfx.".replaceAll("\\.{1,}", ".")
  }

  override def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = client.increment(s"${prefix}${key}", magnitude, sampleRate)

  override def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = client.decrement(s"${prefix}${key}", magnitude, sampleRate)

  override def gauge(key: String, magnitude: Double, sampleRate: Double = 1.0): Unit = client.gauge(s"${prefix}${key}", magnitude.toDouble, sampleRate)

  override def timer(key: String, value: Int, sampleRate: Double = 1.0): Unit = client.timing(s"${prefix}${key}", value, sampleRate)

}
