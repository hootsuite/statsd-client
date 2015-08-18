// ==========================================================================
// Copyright 2014 HootSuite Media, Inc.
// --------------------------------------------------------------------------
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this work except in compliance with the License.
// You may obtain a copy of the License in the LICENSE file, or at:
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ==========================================================================
package com.hootsuite.statsd

import scala.concurrent.{Future, ExecutionContext}

object StatsdReporting {
  def slices(timestamps: Seq[(String, Long)]): Seq[(String, Int)] =
    (timestamps zip timestamps.tail) collect {
      case ((n1, t1), (n2, t2)) if t2 > t1=>
        val diff = (t2 - t1).toInt
        val slice = s"${n1}To${n2.capitalize}"

        slice -> diff
    }
}

/**
 * Add this trait and define a StatsdClient implementation to add statsd reporting. Use NoopStatsdClient
 * when stats aren't needed.
 */
trait StatsdReporting {

  import StatsdReporting._

  protected val statsdClient: StatsdClient

  /**
   * Increment a counter
   *
   * @param sampleRate between 0.0 and 1.0. The client will only send to the server
   *                   sampleRate% of the time.
   */
  def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit =
    statsdClient.inc(key, magnitude, sampleRate)

  /**
   * Decrement a counter
   *
   * @param sampleRate between 0.0 and 1.0. The client will only send to the server
   *                   sampleRate% of the time.
   */
  def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit =
    statsdClient.dec(key, magnitude, sampleRate)

  /**
   * Set a gauge value
   *
   * @param sampleRate between 0.0 and 1.0. The client will only send to the server
   *                   sampleRate% of the time.
   */
  def gauge(key: String, amt: Double, sampleRate: Double = 1.0): Unit =
    statsdClient.gauge(key, amt, sampleRate)

  /**
   * Set a timing value
   *
   * @param sampleRate between 0.0 and 1.0. The client will only send to the server
   *                   sampleRate% of the time.
   */
  def timer(key: String, time: Int, sampleRate: Double = 1.0): Unit =
    statsdClient.timer(key, time, sampleRate)

  /**
   * Times the duration of the supplied thunk
   */
  def timed[T](key: String, sampleRate: Double = 1.0)(operation: => T): T = {
    val timr = new Timer
    val result = operation
    val duration = timr.stop

    timer(key, duration.toInt, sampleRate)

    result
  }

  /**
   * Times the duration of the supplied future thunk
   */
  def timedFuture[T](key: String, sampleRate: Double = 1.0)
                    (operation: =>Future[T])
                    (implicit ec: ExecutionContext): Future[T] = {
    val timr = new Timer
    val result = operation
    result.onComplete {_ =>
      val duration = timr.stop
      timer(key, duration.toInt, sampleRate)
    }

    result
  }

  /**
   * Times the duration of the supplied function.
   * It returns a tuple of (result, duration)
   */
  def timeAndResult[T](key: String, sampleRate: Double = 1.0)(operation: => T): (T, Long) = {
    val timr = new Timer
    val result = operation
    val duration = timr.stop

    timer(key, duration.toInt, sampleRate)

    (result, duration)
  }

  /** Takes labelled timestamps and graphs the gaps between them. */
  def timeSlices(key: String => String, timestamps: Seq[(String, Long)]): Unit =
    slices(timestamps) foreach {
      case (slice, diff) =>
        val stat = key(slice)
        timer(stat, diff)
    }
}
