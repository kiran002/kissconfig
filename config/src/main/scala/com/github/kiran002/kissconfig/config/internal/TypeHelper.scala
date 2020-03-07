package com.github.kiran002.kissconfig.config.internal

import com.typesafe.config.Config

import scala.reflect.runtime
import scala.reflect.runtime.{universe => ru}
import scala.collection.JavaConverters._

class TypeHelper(config: Config) {

  private val primaryType: PartialFunction[(String, ru.Type), Any] = new PartialFunction[(String, ru.Type), Any] {
    //Primary types
    private val booleanType = ru.typeOf[Boolean].resultType
    private val integerType = ru.typeOf[Int].resultType
    private val stringType = ru.typeOf[String].resultType

    override def isDefinedAt(x: (String, ru.Type)): Boolean = x._2 match {
      case `booleanType` | `integerType` | `stringType` => true
      case _ => false
    }

    override def apply(nameTypeTuple: (String, ru.Type)): Any = nameTypeTuple._2 match {
      case `booleanType` => config.getBoolean(nameTypeTuple._1)
      case `integerType` => config.getInt(nameTypeTuple._1)
      case `stringType` => config.getString(nameTypeTuple._1)
    }
  }

  private val listType: PartialFunction[(String, ru.Type), Any] = new PartialFunction[(String, ru.Type), Any] {
    //List types
    private val listOfStringType = ru.typeOf[List[String]].resultType

    override def isDefinedAt(x: (String, ru.Type)): Boolean = x._2 match {
      case `listOfStringType` => true
      case _ => false
    }

    override def apply(nameTypeTuple: (String, ru.Type)): Any = nameTypeTuple._2 match {
      case `listOfStringType` => config.getStringList(nameTypeTuple._1).asScala.toList
    }
  }


  private val optionalType: PartialFunction[(String, ru.Type), Any] = new PartialFunction[(String, ru.Type), Any] {
    //Optional type
    private   val optionalStringType = ru.typeOf[Option[String]].resultType


    override def isDefinedAt(x: (String, ru.Type)): Boolean = x._2 match {
      case `optionalStringType` => true
      case _ => false
    }

    override def apply(nameTypeTuple: (String, ru.Type)): Any = nameTypeTuple._2 match {
      case `optionalStringType` => config.getBoolean(nameTypeTuple._1)
    }
  }

  private val mapType: PartialFunction[(String, ru.Type), Any] = new PartialFunction[(String, ru.Type), Any] {
    private  val mapOfStringToAny = ru.typeOf[Map[String, Any]].resultType
    private  val mapOfStringToString = ru.typeOf[Map[String, String]].resultType

    override def isDefinedAt(x: (String, ru.Type)): Boolean = x._2 match {
      case `mapOfStringToAny` | `mapOfStringToString` => true
      case _ => false
    }

    override def apply(nameTypeTuple: (String, ru.Type)): Any = nameTypeTuple._2 match {
      case `mapOfStringToAny` =>
        config.getConfig(nameTypeTuple._1).entrySet().asScala
          .map { entry =>
            entry.getKey -> entry.getValue.unwrapped().asInstanceOf[Any]
          }.toMap
      case `mapOfStringToString` =>
        val temp = config.getConfig(nameTypeTuple._1)
        temp
          .entrySet()
          .asScala
          .map { x =>
            x.getKey -> x.getValue.unwrapped().asInstanceOf[String]
          }
          .toMap
    }
  }

  val func: PartialFunction[(String, ru.Type), Any] = primaryType orElse optionalType orElse listType orElse mapType
}
