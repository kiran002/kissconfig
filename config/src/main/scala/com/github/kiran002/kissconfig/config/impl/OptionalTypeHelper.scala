package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.TypeHelper
import com.typesafe.config.Config

import scala.reflect.runtime.universe

import scala.util.Try

//TODO: Support Lists, custom types, maps
class OptionalTypeHelper extends TypeHelper {

  private val optionTypeSymbol = universe.typeOf[Option[_]].typeSymbol

  override def canHandle(objType: universe.Type): Boolean = objType.typeSymbol == optionTypeSymbol

  override def get(objType: universe.Type): (Config, Option[String]) => Any = {
    //TODO: Change this
    val top                                = objType.typeArgs.head
    val fn                                 = TypeHelper.get.filter(x => x.canHandle(top)).head.get(top)
    val x: (Config, Option[String]) => Any = (j, k) => Try(fn(j, k)).toOption
    x
  }
}
