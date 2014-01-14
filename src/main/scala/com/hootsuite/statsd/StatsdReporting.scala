package com.hootsuite.statsd

/**
 * Wrapper class for statsdClient to easily allow applications to
 * time operations and do nothing when no statsdClient is defined.
 */
trait StatsdReporting {

  protected val statsdClient: Option[StatsdHandler]

  // Prefer System.nanoTime for timing durations.
  def now: Long = System currentTimeMillis

  /** Set the given timer, if a statsd client is defined */
  def timer(key: String, time: Int, sampleRate: Double = 1.0): Unit = statsdClient map { _.timer(key, time, sampleRate) }

  /** Increment the given counter, if a statsd client is defined */
  def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = statsdClient map { _.inc(key, magnitude, sampleRate) }

  /** Decrement the given counter, if a statsd client is defined */
  def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = statsdClient map { _.dec(key, magnitude, sampleRate) }

  /** Set the given gauge, if a statsd client is defined */
  def gauge(key: String, amt: Int, sampleRate: Double = 1.0): Unit = statsdClient map { _.gauge(key, amt, sampleRate) }

  /** Times the duration of the supplied thunk */
  def timed[T](key: String)(operation: => T): T = {
    val start = System.nanoTime
    val result = operation
    val duration = (System.nanoTime - start) / 1000000L

    timer(key, duration.toInt)

    result
  }

}
