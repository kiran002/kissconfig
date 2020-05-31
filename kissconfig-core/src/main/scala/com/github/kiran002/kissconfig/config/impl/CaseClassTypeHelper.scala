package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.{ResolutionStrategy, TypeHelper}
import com.github.kiran002.kissconfig.config.internal.KissConfigException
import com.github.kiran002.kissconfig.config.models.{FieldInfo, Input}

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}

/**
  * [[CaseClassTypeHelper]] helps extract and populate case classes
  */
class CaseClassTypeHelper extends TypeHelper {

  /**
    * Is the typehelper able to handle this particular type ([[objType]])
    * @param objType: type of the object
    * @return : true if it can handle [[objType]] false otherwise
    */
  override def canHandle(objType: universe.Type): Boolean =
    Try(objType.typeSymbol.asClass.isCaseClass).getOrElse(false)

  /**
    * Returns a function that can be used to extract values compatible with objType
    * @param objType  type of the object
    * @return : function, that takes config object and config key as input and returns the extracted value
    */
  override def get(objType: universe.Type): Input => Any = { ip =>
    {
      val config            = if (ip.configKey.isDefined) ip.config.getConfig(ip.configKey.get) else ip.config
      val tuplesOfFields    = getListOfFields(objType)
      val runtimeMirror     = universe.runtimeMirror(getClass.getClassLoader)
      val classMirror       = runtimeMirror.reflectClass(objType.typeSymbol.asClass)
      val constructorSymbol = objType.decl(universe.termNames.CONSTRUCTOR).asMethod
      val constructorMirror = classMirror.reflectConstructor(constructorSymbol)

      val seqOfConfigValues = tuplesOfFields.map { nameTypeTuple =>
        Try {
          TypeHelper.get.filter(_.canHandle(nameTypeTuple.typ)).head
        } match {
          case Success(value) =>
            val func = ResolutionStrategy.get
            val key =
              if (func.isDefined) func.get.resolve(nameTypeTuple.name) else nameTypeTuple.name
            val input = Input(config, Some(key))
            value.get(nameTypeTuple.typ)(input)
          case Failure(exception) =>
            throw KissConfigException(s"No TypeHelper defined for $nameTypeTuple", exception)
        }
      }
      constructorMirror(seqOfConfigValues: _*)
    }
  }

  /**
    * Returns a list of fields for a case class
    * @param ip: Input case class [[scala.reflect.runtime.universe.Type]]
    * @return list of sorted fields (exactly as defined in the case class definition
    */
  private def getListOfFields(ip: universe.Type): List[FieldInfo] =
    ip.members.sorted.collect {
      case m: MethodSymbol if m.isCaseAccessor =>
        FieldInfo(m.name.toString, m.returnType)
    }
}
