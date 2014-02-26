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

import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.OptionValues._
import com.hootsuite.statsd.handlers.DebugStatsdClient



class StatsdReportingTest extends FlatSpec with Matchers with StatsdReporting {
  override protected val statsdClient: StatsdClient = new DebugStatsdClient

  val queryStats = statsdClient.asInstanceOf[DebugStatsdClient].stats
  val queryStatsGauge = statsdClient.asInstanceOf[DebugStatsdClient].gaugeStats

  "The debug statsd client" should "increment a counter" in {
    inc("test.counter")
    queryStats.get("test.counter").value should be (1)
  }

  it should "decrement a counter" in {
    inc("test.counter2")
    inc("test.counter2")
    dec("test.counter2")

    queryStats.get("test.counter2").value should be (1)
  }

  it should "set a gauge" in {
    gauge("test.gauge", 1.2)

    queryStatsGauge.get("test.gauge").value should be (1.2)
  }

  it should "set a timer" in {
    timer("test.timer", 100)

    queryStats.get("test.timer").value should be (100)
  }

  it should "time an event" in {
    timed("test.timer2") {
      Thread.sleep(20)
    }

    queryStats.get("test.timer2").value should be > 10
  }

  it should "only update a certain percentage of times when using sampleRate" in {
    (1 to 10).foreach { _ =>
      inc("test.counter3", 1, 0.1)
    }

    queryStats.get("test.counter3").value should be < 10
  }

  "Slicing" should "convert timestamps to labelled differences" in {
    val stamps = Seq("in" -> 1000000000L, "start" -> 1000001000L,
                     "finish" -> 1000003000L, "out" -> 1000003500L)

    val expected = Seq("inToStart" -> 1000, "startToFinish" -> 2000, "finishToOut" -> 500)

    expected should equal(StatsdReporting slices stamps)
  }

  it should "ignore negative slices" in {
    // this is how you can encode multiple series
    val stamps = Seq("in" -> 1000000000L, "start" -> 1000001000L,
                     "finish" -> 1000003000L, "out" -> 1000003500L,
                     "in" -> 1000000000L, "out" -> 1000003500L)

    val expected = Seq("inToStart" -> 1000, "startToFinish" -> 2000, "finishToOut" -> 500, "inToOut" -> 3500)

    expected should equal(StatsdReporting slices stamps)

  }
}
