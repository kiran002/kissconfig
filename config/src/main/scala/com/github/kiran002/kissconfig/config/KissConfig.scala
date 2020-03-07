package com.github.kiran002.kissconfig.config

import com.github.kiran002.kissconfig.config.internal.Helper
import com.typesafe.config.Config

import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => ru}



object KissConfig {

  def get[T: TypeTag](config: Config):T = {
    val tuplesOfFields: List[(String, ru.Type)] = Helper.classAccessors[T]
    val appConfig = ru.typeOf[T].typeSymbol.asClass
    Helper.get(config, tuplesOfFields, appConfig, ru.typeOf[T]).asInstanceOf[T]
  }
}