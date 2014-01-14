package com.hootsuite.statsd

import com.typesafe.config.Config

/**
 * Basic handler that will send all messages to statsd. Wrapper for the Java StatdsClient from Etsy.
 */
class StatsdHandler(config: Config) {
  private val host = config.getString("statsd.host")
  private val port = config.getInt("statsd.port")
  private val client = new etsy.StatsdClient(host, port, null)

  private val prefix = {
    val hpfx = config.getString("statsd.hostpfx").takeWhile(_ != '.').mkString
    val pfx = config.getString("statsd.prefix")
    s"$pfx.$hpfx.".replaceAll("\\.{1,}", ".")
  }

  /**
   * Increment a counter
   *
   * @param sampleRate between 0.0 and 1.0. The client will only send to the server sampleRate% of the time.
   */
  def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = client.increment(s"${prefix}${key}", magnitude, sampleRate)

  /**
   * Decrement a counter
   *
   * @param sampleRate between 0.0 and 1.0. The client will only send to the server sampleRate% of the time.
   */
  def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = client.decrement(s"${prefix}${key}", magnitude, sampleRate)

  /**
   * Set a gauge value
   *
   * @param sampleRate between 0.0 and 1.0. The client will only send to the server sampleRate% of the time.
   */
  def gauge(key: String, magnitude: Int, sampleRate: Double = 1.0): Unit = client.gauge(s"${prefix}${key}", magnitude.toDouble, sampleRate)

  /**
   * Set a timing value
   *
   * @param sampleRate between 0.0 and 1.0. The client will only send to the server sampleRate% of the time.
   */
  def timer(key: String, value: Int, sampleRate: Double = 1.0): Unit = client.timing(s"${prefix}${key}", value, sampleRate)


  override def toString =
    "StatsdHandler(StatsdClient(%s, %s), %s)" format(host, port, prefix)
}
