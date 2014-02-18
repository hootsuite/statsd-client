package com.hootsuite.statsd

/**
 * See the StatsdReporting trait for API documentation
 */
trait StatsdClient {

  def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0)

  def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0)

  def gauge(key: String, magnitude: Double, sampleRate: Double = 1.0)

  def timer(key: String, value: Int, sampleRate: Double = 1.0)

}
