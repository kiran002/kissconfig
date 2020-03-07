package com.github.kiran002.kissconfig.examples

import com.github.kiran002.kissconfig.config.KissConfig
import com.github.kiran002.kissconfig.examples.models.AppConfig
import com.typesafe.config.ConfigFactory

object Application extends App {


  val kc = new KissConfig[AppConfig](ConfigFactory.defaultApplication())

  val appConfig = kc.get
  println(s"My Integer : ${appConfig.myInt}")
  println(s"My String : ${appConfig.myString}")
  println(s"My Boolean : ${appConfig.myBoolean}")
}
