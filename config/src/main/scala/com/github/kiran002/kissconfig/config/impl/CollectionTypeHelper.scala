package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.TypeHelper
import com.github.kiran002.kissconfig.config.models.Input

import scala.collection.JavaConverters._
import scala.reflect.runtime.universe

/**
  * [[CollectionTypeHelper]] helps extract collectins like maps, lists
  */
class CollectionTypeHelper extends TypeHelper {

  private val listTypeSymbol   = universe.typeOf[List[_]].typeSymbol
  private val mapOfStringToAny = universe.typeOf[Map[String, Any]].typeSymbol

  private val mapExtractor: Input => Any = x =>
    x.config
      .getConfig(x.configKey.get)
      .entrySet()
      .asScala
      .map { entry =>
        entry.getKey -> entry.getValue.unwrapped().asInstanceOf[Any]
      }
      .toMap

  private val listExtractor: Input => Any = x =>
    x.config.getAnyRefList(x.configKey.get).asScala.toList

  private val listOfTypes = Map(listTypeSymbol -> listExtractor, mapOfStringToAny -> mapExtractor)

  /**
    *
    * @param objType: type of the object
    * @return : true if it can handle [[objType]] false otherwise
    */
  override def canHandle(objType: universe.Type): Boolean = listOfTypes.contains(objType.typeSymbol)

  /**
    * Returns a function that can be used to extract values compatible with objType
    * @param objType  type of the object
    * @return : function, that takes config object and config key as input and returns the extracted value
    */
  override def get(objType: universe.Type): Input => Any =
    listOfTypes(objType.typeSymbol)
}
