package com.hootsuite.statsd

sealed trait StatsdMessage

case class Timer(key: String, time: Int) extends StatsdMessage

case class Increment(key: String) extends StatsdMessage

case class Decrement(key: String) extends StatsdMessage

