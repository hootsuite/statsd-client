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
package com.hootsuite.statsd.handlers

import com.typesafe.config.Config
import com.hootsuite.statsd.StatsdClient

/**
 * Basic handler that will send all messages to statsd. Wrapper for the Java StatdsClient from Etsy.
 *
 * Config parameters:
 *
 * statsd.host - Server host
 * statsd.port - Server port
 * statsd.hostpfx - Local hostname prefix for stats key generation
 * statsd.prefix - Functional prefix for stats key generation
 */
class UdpStatsdClient(config: Config) extends StatsdClient {
  private val host = config.getString("statsd.host")
  private val port = config.getInt("statsd.port")
  private val client = new etsy.StatsdClient(host, port)

  private val prefix = {
    val hpfx = config.getString("statsd.hostpfx").takeWhile(_ != '.').mkString
    val pfx = config.getString("statsd.prefix")
    s"$pfx.$hpfx.".replaceAll("\\.{1,}", ".")
  }

  override def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit =
    client.increment(s"${prefix}${key}", magnitude, sampleRate)

  override def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit =
    client.decrement(s"${prefix}${key}", magnitude, sampleRate)

  override def gauge(key: String, magnitude: Double, sampleRate: Double = 1.0): Unit =
    client.gauge(s"${prefix}${key}", magnitude.toDouble, sampleRate)

  override def timer(key: String, value: Int, sampleRate: Double = 1.0): Unit =
    client.timing(s"${prefix}${key}", value, sampleRate)

}
