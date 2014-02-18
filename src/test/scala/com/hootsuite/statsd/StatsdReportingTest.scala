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
    (1 to 20).foreach { _ =>
      inc("test.counter3", 1, 0.1)
    }

    queryStats.get("test.counter3").value should be < 8
  }
}
