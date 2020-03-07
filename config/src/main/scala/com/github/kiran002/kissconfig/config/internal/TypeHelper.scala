package com.github.kiran002.kissconfig.config.internal

import com.typesafe.config.Config

import scala.collection.JavaConverters._
import scala.reflect.runtime.{universe => ru}
import scala.util.Try

class TypeHelper(config: Config) {

  private val primaryType: PartialFunction[(String, ru.Symbol, ru.Type), Any] = new PartialFunction[(String, ru.Symbol, ru.Type), Any] {
    //Primary types
    private val booleanType = ru.typeOf[Boolean].typeSymbol
    private val integerType = ru.typeOf[Int].typeSymbol
    private val stringType = ru.typeOf[String].typeSymbol

    override def isDefinedAt(x: (String, ru.Symbol, ru.Type)): Boolean = x._2 match {
      case `booleanType` | `integerType` | `stringType` => true
      case _ => false
    }

    override def apply(nameTypeTuple: (String, ru.Symbol, ru.Type)): Any = nameTypeTuple._2 match {
      case `booleanType` => config.getBoolean(nameTypeTuple._1)
      case `integerType` => config.getInt(nameTypeTuple._1)
      case `stringType` => config.getString(nameTypeTuple._1)
    }
  }

  private val listType: PartialFunction[(String, ru.Symbol, ru.Type), Any] = new PartialFunction[(String, ru.Symbol, ru.Type), Any] {
    //List types
    private val listTypeSymbol = ru.typeOf[List[_]].typeSymbol

    override def isDefinedAt(x: (String, ru.Symbol, ru.Type)): Boolean = x._2 match {
      case `listTypeSymbol` => true
      case _ => false
    }

    override def apply(nameTypeTuple: (String, ru.Symbol, ru.Type)): Any = nameTypeTuple._2 match {
      case `listTypeSymbol` => config.getAnyRefList(nameTypeTuple._1).asScala.toList
    }
  }


  private val optionalType: PartialFunction[(String, ru.Symbol, ru.Type), Any] = new PartialFunction[(String, ru.Symbol, ru.Type), Any] {
    //Optional type
    private val optionTypeSymbol = ru.typeOf[Option[_]].typeSymbol

    override def isDefinedAt(x: (String, ru.Symbol, ru.Type)): Boolean = x._2 match {
      case `optionTypeSymbol` => true
      case _ => false
    }

    override def apply(nameTypeTuple: (String, ru.Symbol, ru.Type)): Any = {
      val cs = (nameTypeTuple._1, nameTypeTuple._3.typeArgs.head.typeSymbol, nameTypeTuple._3)
      if (primaryType.isDefinedAt(cs))
        Try(primaryType(cs)).toOption
      else None
    }
  }

  private val mapType: PartialFunction[(String, ru.Symbol, ru.Type), Any] = new PartialFunction[(String, ru.Symbol, ru.Type), Any] {
    private val mapOfStringToAny = ru.typeOf[Map[String, Any]].typeSymbol

    override def isDefinedAt(x: (String, ru.Symbol, ru.Type)): Boolean = x._2 match {
      case `mapOfStringToAny` => true
      case _ => false
    }

    override def apply(nameTypeTuple: (String, ru.Symbol, ru.Type)): Any = nameTypeTuple._2 match {
      case `mapOfStringToAny` =>
        config.getConfig(nameTypeTuple._1).entrySet().asScala
          .map { entry =>
            entry.getKey -> entry.getValue.unwrapped().asInstanceOf[Any]
          }.toMap
    }
  }

  val func: PartialFunction[(String, ru.Symbol, ru.Type), Any] = primaryType orElse optionalType orElse listType orElse mapType
}
