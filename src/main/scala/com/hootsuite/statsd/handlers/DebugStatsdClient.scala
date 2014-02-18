package com.hootsuite.statsd.handlers

import scala.util.Random
import com.hootsuite.statsd.StatsdClient
import scala.collection.concurrent.TrieMap


/**
 * A debug client that just stores the stats locally and can be queried later
 */
class DebugStatsdClient extends StatsdClient {

  val stats = new TrieMap[String, Int]
  val gaugeStats = new TrieMap[String, Double]


  override def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit =
    maybe(sampleRate) {
      stats.put(key, stats.getOrElse(key, 0) + magnitude)
    }

  override def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit =
    maybe(sampleRate) {
      stats.put(key, stats.getOrElse(key, 0) - magnitude)
    }

  override def gauge(key: String, magnitude: Double, sampleRate: Double = 1.0): Unit =
    maybe(sampleRate) {
      gaugeStats.put(key, magnitude)
    }

  override def timer(key: String, value: Int, sampleRate: Double = 1.0): Unit =
    maybe(sampleRate) {
      stats.put(key, value)
    }


  private def maybe(sampleRate: Double)(block: => Unit): Unit = {
    if(sampleRate >= 1.0 || Random.nextDouble() < sampleRate) {
      block
    }
  }
}
