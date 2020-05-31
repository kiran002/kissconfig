package com.github.kiran002.kissconfig.config.internal

case class KissConfigException(msg: String, cause: Throwable) extends RuntimeException(msg, cause) {}
