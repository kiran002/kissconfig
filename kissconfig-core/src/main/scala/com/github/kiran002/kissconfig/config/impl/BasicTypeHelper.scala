package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.TypeHelper
import com.github.kiran002.kissconfig.config.models.Input

import scala.reflect.runtime.universe
import scala.util.Try

/**
  * BasicTypeHelper helps extract the most simple types (i.e, Primitive types and Strings)
  */
class BasicTypeHelper extends TypeHelper {
  //BasicType Extractors
  private val booleanExtractor: Input => Any = x => x.config.getBoolean(x.configKey.get)
  private val stringExtractor: Input => Any  = x => x.config.getString(x.configKey.get)
  private val intExtractor: Input => Any     = x => x.config.getInt(x.configKey.get)
  private val shortExtractor: Input => Any   = x => x.config.getInt(x.configKey.get).toShort
  private val longExtractor: Input => Any    = x => x.config.getLong(x.configKey.get)
  private val doubleExtractor: Input => Any  = x => x.config.getDouble(x.configKey.get)

  /**
    * Is the typehelper able to handle this particular type ([[objType]])
    *
     * @param objType : type of the object
    * @return : true if it can handle [[objType]] false otherwise
    */
  override def canHandle(objType: universe.Type): Boolean =
    Try {
      get(objType)
      true
    }.getOrElse(false)

  /**
    * Returns a function that can be used to extract values compatible with objType
    *
     * @param objType type of the object
    * @return : function, that takes config object and config key as input and returns the extracted value
    */
  override def get(objType: universe.Type): Input => Any =
    objType match {
      case t if t =:= StringType  => stringExtractor
      case t if t =:= IntegerType => intExtractor
      case t if t =:= ShortType   => shortExtractor
      case t if t =:= LongType    => longExtractor
      case t if t =:= BooleanType => booleanExtractor
      case t if t =:= DoubleType  => doubleExtractor
    }
}
