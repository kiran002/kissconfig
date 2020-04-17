package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.TypeHelper
import com.github.kiran002.kissconfig.config.models.Input

import scala.reflect.runtime.universe
import scala.util.Try

/**
  * [[OptionalTypeHelper]] helps extract Optional types
  */
class OptionalTypeHelper extends TypeHelper {

  private val optionTypeSymbol = universe.typeOf[Option[_]].typeSymbol

  /**
    * Is the typehelper able to handle this particular type ([[objType]])
    * @param objType: type of the object
    * @return : true if it can handle [[objType]] false otherwise
    */
  override def canHandle(objType: universe.Type): Boolean = objType.typeSymbol == optionTypeSymbol

  /**
    * Returns a function that can be used to extract values compatible with objType
    * @param objType  type of the object
    * @return : function, that takes config object and config key as input and returns the extracted value
    */
  override def get(objType: universe.Type): Input => Any = {
    //TODO: Change this
    val top             = objType.typeArgs.head
    val fn              = TypeHelper.get.filter(x => x.canHandle(top)).head.get(top)
    val x: Input => Any = j => Try(fn(j)).toOption
    x
  }
}
