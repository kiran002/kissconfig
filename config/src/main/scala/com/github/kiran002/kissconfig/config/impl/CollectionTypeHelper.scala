package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.TypeHelper
import com.typesafe.config.Config

import scala.collection.JavaConverters._
import scala.reflect.runtime.universe

class CollectionTypeHelper extends TypeHelper {

  private val listTypeSymbol   = universe.typeOf[List[_]].typeSymbol
  private val mapOfStringToAny = universe.typeOf[Map[String, Any]].typeSymbol

  private val mapExtractor: (Config, Option[String]) => Any = (x, y) =>
    x.getConfig(y.get)
      .entrySet()
      .asScala
      .map { entry =>
        entry.getKey -> entry.getValue.unwrapped().asInstanceOf[Any]
      }
      .toMap

  private val listExtractor: (Config, Option[String]) => Any = (x, y) =>
    x.getAnyRefList(y.get).asScala.toList

  private val listOfTypes = Map(listTypeSymbol -> listExtractor, mapOfStringToAny -> mapExtractor)

  override def canHandle(objType: universe.Type): Boolean = listOfTypes.contains(objType.typeSymbol)

  override def get(objType: universe.Type): (Config, Option[String]) => Any =
    listOfTypes(objType.typeSymbol)
}
