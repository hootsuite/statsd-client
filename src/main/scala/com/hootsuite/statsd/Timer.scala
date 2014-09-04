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

class Timer {
  private val start = System.nanoTime

  /**
   * @return milliseconds elapsed (truncated)
   */
  def stop: Long =
    (System.nanoTime - start) / 1000000L

  /**
   * @return milliseconds elapsed (truncated)
   */
  def msElapsed: Double =
    (System.nanoTime - start).toDouble / 1000000.0
}
