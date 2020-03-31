package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.TypeHelper
import com.typesafe.config.Config

import scala.reflect.runtime.universe

class BasicTypeHelper extends TypeHelper {

  private val booleanExtractor: (Config, Option[String]) => Any = (x, y) => x.getBoolean(y.get)
  private val intExtractor: (Config, Option[String]) => Any     = (x, y) => x.getInt(y.get)
  private val stringExtractor: (Config, Option[String]) => Any  = (x, y) => x.getString(y.get)

  //Primary types
  private val symbolToFunction: Map[universe.Symbol, (Config, Option[String]) => Any] =
    Map(
      universe.typeOf[Boolean].typeSymbol -> booleanExtractor,
      universe.typeOf[Int].typeSymbol     -> intExtractor,
      universe.typeOf[String].typeSymbol  -> stringExtractor
    )

  override def canHandle(objType: universe.Type): Boolean =
    symbolToFunction.contains(objType.typeSymbol)

  override def get(objType: universe.Type): (Config, Option[String]) => Any =
    symbolToFunction(objType.typeSymbol)
}
