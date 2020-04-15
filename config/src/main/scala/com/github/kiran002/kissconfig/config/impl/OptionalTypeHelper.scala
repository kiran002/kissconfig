package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.TypeHelper
import com.github.kiran002.kissconfig.config.models.Input

import scala.reflect.runtime.universe
import scala.util.Try

class OptionalTypeHelper extends TypeHelper {

  private val optionTypeSymbol = universe.typeOf[Option[_]].typeSymbol

  override def canHandle(objType: universe.Type): Boolean = objType.typeSymbol == optionTypeSymbol

  override def get(objType: universe.Type): Input => Any = {

    //TODO: Change this
    val top             = objType.typeArgs.head
    val fn              = TypeHelper.get.filter(x => x.canHandle(top)).head.get(top)
    val x: Input => Any = j => Try(fn(j)).toOption
    x
  }
}
