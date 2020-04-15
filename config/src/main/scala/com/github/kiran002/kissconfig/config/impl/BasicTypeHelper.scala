package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.TypeHelper
import com.github.kiran002.kissconfig.config.models.Input

import scala.reflect.runtime.universe

/**
  * BasicTypeHelper helps extract the most simple types (i.e, Primitive types and Strings)
  */
class BasicTypeHelper extends TypeHelper {
  //BasicType Extractors
  private val booleanExtractor: Input => Any = x => x.config.getBoolean(x.configKey.get)
  private val intExtractor: Input => Any     = x => x.config.getInt(x.configKey.get)
  private val stringExtractor: Input => Any  = x => x.config.getString(x.configKey.get)

  //Map of supported types with their extractors
  private val symbolToFunction: Map[universe.Symbol, Input => Any] =
    Map(
      universe.typeOf[Boolean].typeSymbol -> booleanExtractor,
      universe.typeOf[Int].typeSymbol     -> intExtractor,
      universe.typeOf[String].typeSymbol  -> stringExtractor
    )

  /**
    *
    * @param objType: type of the object
    * @return : true if it can handle objType false otherwise
    */
  override def canHandle(objType: universe.Type): Boolean =
    symbolToFunction.contains(objType.typeSymbol)

  /**
    *
    * @param objType  type of the object
    * @return : function, that takes config object and config key as input and returns the extracted value
    */
  override def get(objType: universe.Type): Input => Any =
    symbolToFunction(objType.typeSymbol)
}
