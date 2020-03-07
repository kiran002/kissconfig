package com.github.kiran002.kissconfig.config

import com.github.kiran002.kissconfig.config.internal.Helper
import com.typesafe.config.Config

import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => ru}


class KissConfig[T: TypeTag](config: Config) {

  private val tr: List[(String, ru.Type)] = Helper.classAccessors[T]

  private val runtimeMirror = ru.runtimeMirror(getClass.getClassLoader)
  private val appConfig = ru.typeOf[T].typeSymbol.asClass

  def get: T = Helper.get(config, tr, appConfig, ru.typeOf[T]).asInstanceOf[T]


}
