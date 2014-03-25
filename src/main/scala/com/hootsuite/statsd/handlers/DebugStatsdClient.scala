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
