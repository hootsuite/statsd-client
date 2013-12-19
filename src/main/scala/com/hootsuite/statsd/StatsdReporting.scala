package com.hootsuite.statsd

/**
 * Wrapper class for statsdClient to easily allow applications to
 * time operations and do nothing when no statsdClient is defined.
 */
trait StatsdReporting {

  protected val statsdClient: Option[StatsdHandler]

  private def now: Long = System nanoTime

  def timer(key: String, time: Int): Unit = {
    statsdClient map { _.timer(key, time) }
  }

  /** Increment the given counter, if a statsd client is defined */
  def inc(key: String): Unit =
    statsdClient map { _ inc key }

  /** Set the given gauge, if a statsd client is defined */
  def gauge(key: String, amt: Int): Unit =
    statsdClient map { _.gauge(key, amt) }

  /** Times the duration of the supplied thunk */
  def timed[T](key: String)(operation: => T): T = {
    val start = now
    val result = operation
    val duration = (now - start) / 1000000L

    timer(key, duration.toInt)

    result
  }

}
