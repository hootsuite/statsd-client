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

import com.hootsuite.statsd.StatsdClient


/**
 * A no-op client
 */
class NoopStatsdClient extends StatsdClient {

  override def inc(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = {}
  override def dec(key: String, magnitude: Int = 1, sampleRate: Double = 1.0): Unit = {}
  override def gauge(key: String, magnitude: Double, sampleRate: Double = 1.0): Unit = {}
  override def timer(key: String, value: Int, sampleRate: Double = 1.0): Unit = {}
}
