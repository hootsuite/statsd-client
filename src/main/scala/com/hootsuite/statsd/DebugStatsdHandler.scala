package com.hootsuite.statsd

import collection.JavaConverters._
import scala.util.Random


/**
 *
 */
object DebugStatsdHandler {

  private val stats = new java.util.concurrent.ConcurrentHashMap[String, Int]().asScala

  def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit =
    maybe(sampleRate) {
      stats.put(key, stats.getOrElse(key, 0) + magnitude)
    }

  def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit =
    maybe(sampleRate) {
      stats.put(key, stats.getOrElse(key, 0) - magnitude)
    }

  def gauge(key: String, magnitude: Int, sampleRate: Double = 1.0): Unit =
    maybe(sampleRate) {
      stats.put(key, magnitude)
    }

  def timer(key: String, value: Int, sampleRate: Double = 1.0): Unit =
    maybe(sampleRate) {
      stats.put(key, value)
    }

  private def maybe(sampleRate: Double)(block: => Unit): Unit = {
    if(sampleRate >= 1.0 || Random.nextDouble() < sampleRate) {
      block
    }
  }
}
