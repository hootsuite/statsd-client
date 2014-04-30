// ==========================================================================
// Copyright 2015 HootSuite Media, Inc.
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
package com.hootsuite.statsd.handlers

import com.typesafe.config.Config
import com.hootsuite.statsd.StatsdClient

import scala.concurrent.{Future, ExecutionContext}

/**
 * Basic handler that will send all messages to statsd. Wrapper for the Java StatdsClient from Etsy.
 *
 * This client can and should be configured to make the statsd calls asynchronously, on a separate
 * thread pool / execution context. See the `ec` param below.
 *
 * Even though the underlying client is a NIO implementation and uses UDP, under some circumstances
 * unavailability of the statsd servers can dramatically affect the calling thread's performance.
 *
 * An execution context backed by a thread pool with a single thread should be enough for sending
 * the stats. As with any bulkheading use a dedicated EC and avoid using a shared one (such as
 * `scala.concurrent.ExecutionContext.Implicits.global`).
 *
 * Config parameters:
 *
 * statsd.host - Server host
 * statsd.port - Server port
 * statsd.hostpfx - Local hostname prefix for stats key generation
 * statsd.prefix - Functional prefix for stats key generation
 *
 * @param ec Execution context to be used by this client wrapper to invoke the underlying Etsy client;
 *           if a None/no execution context is supplied the calls will be made on the calling thread.
 */
class UdpStatsdClient(config: Config, ec: Option[ExecutionContext]) extends StatsdClient {

  private val host = config.getString("statsd.host")
  private val port = config.getInt("statsd.port")
  private val client = new etsy.StatsdClient(host, port)

  private val prefix = {
    val hpfx = config.getString("statsd.hostpfx").takeWhile(_ != '.').mkString
    val pfx = config.getString("statsd.prefix")
    s"$pfx.$hpfx.".replaceAll("\\.{1,}", ".")
  }

  override def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = {
    lazy val doInc: Boolean = client.increment(s"$prefix$key", magnitude, sampleRate)
    ec.map(Future(doInc)(_)).getOrElse(doInc)
  }

  override def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = {
    lazy val doDec = client.decrement(s"$prefix$key", magnitude, sampleRate)
    ec.map(Future(doDec)(_)).getOrElse(doDec)
  }

  override def gauge(key: String, magnitude: Double, sampleRate: Double = 1.0): Unit = {
    lazy val doGauge = client.gauge(s"$prefix$key", magnitude.toDouble, sampleRate)
    ec.map(Future(doGauge)(_)).getOrElse(doGauge)
  }

  override def timer(key: String, value: Int, sampleRate: Double = 1.0): Unit = {
    lazy val doTiming = client.timing(s"$prefix$key", value, sampleRate)
    ec.map(Future(doTiming)(_)).getOrElse(doTiming)
  }

}
