package com.hootsuite.statsd.handlers

import com.hootsuite.statsd.StatsdClient


/**
 * A no-op client
 */
class NoopStatsdClient extends StatsdClient {

  override def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = {}
  override def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = {}
  override def gauge(key: String, magnitude: Double, sampleRate: Double = 1.0): Unit = {}
  override def timer(key: String, value: Int, sampleRate: Double = 1.0): Unit = {}
}
