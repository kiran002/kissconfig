package com.github.kiran002.kissconfig.config.internal

import com.typesafe.config.Config

import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => ru}


object Helper {

  def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]

  def classAccessors[T: TypeTag] = typeOf[T].members.sorted.collect {
    case m: MethodSymbol if m.isCaseAccessor => (m.name.toString, m.returnType)
  }

  def get(config: Config,
          tuplesOfFields: List[(String, ru.Type)],
          classSymbol: ru.ClassSymbol,
          classType: ru.Type): Any = {

    val runtimeMirror = ru.runtimeMirror(getClass.getClassLoader)
    val classMirror = runtimeMirror.reflectClass(classSymbol)
    val constructorSymbol = classType.decl(ru.termNames.CONSTRUCTOR).asMethod
    val constructorMirror = classMirror.reflectConstructor(constructorSymbol)
    //Primary types
    val booleanType = ru.typeOf[Boolean].resultType
    val integerType = ru.typeOf[Int].resultType
    val stringType = ru.typeOf[String].resultType
    //List types
    val listOfStringType = ru.typeOf[List[String]].resultType
    //Optional type
    val optionalStringType = ru.typeOf[Option[String]].resultType
    //Map types
    val mapOfStringToAny = ru.typeOf[Map[String, Any]].resultType
    val mapOfStringToString = ru.typeOf[Map[String, String]].resultType


    import scala.collection.JavaConverters._

    val seqOfConfigValues = tuplesOfFields.map { nameTypeTuple =>

      val classSymbol = nameTypeTuple._2.typeSymbol.asClass

      nameTypeTuple._2 match {
        case `booleanType` => config.getBoolean(nameTypeTuple._1)
        case `integerType` => config.getInt(nameTypeTuple._1)
        case `stringType` => config.getString(nameTypeTuple._1)
        case `listOfStringType` => config.getStringList(nameTypeTuple._1).asScala.toList
        case `optionalStringType` => scala.util.Try(config.getString(nameTypeTuple._1)).toOption
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

        case o if classSymbol.isCaseClass => // must be custom type, if subclass of product
          val tupleOfFields = o.members.sorted.collect {
            case m: MethodSymbol if m.isCaseAccessor =>
              (m.name.toString, m.returnType)
          }
          this.get(config.getConfig(nameTypeTuple._1), tupleOfFields, classSymbol, o)
      }
    }
    constructorMirror(seqOfConfigValues: _*)
  }
}
