package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.{ResolutionStrategy, TypeHelper}
import com.github.kiran002.kissconfig.config.models.FieldInfo
import com.typesafe.config.Config

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._
import scala.util.{Success, Try}

class CaseClassTypeHelper extends TypeHelper {

  override def canHandle(objType: universe.Type): Boolean =
    Try(objType.typeSymbol.asClass.isCaseClass).getOrElse(false)

  override def get(objType: universe.Type): (Config, Option[String]) => Any = { (h, j) =>
    {
      val config            = if (j.isDefined) h.getConfig(j.get) else h
      val tuplesOfFields    = getListOfFields(objType)
      val runtimeMirror     = universe.runtimeMirror(getClass.getClassLoader)
      val classMirror       = runtimeMirror.reflectClass(objType.typeSymbol.asClass)
      val constructorSymbol = objType.decl(universe.termNames.CONSTRUCTOR).asMethod
      val constructorMirror = classMirror.reflectConstructor(constructorSymbol)

      val seqOfConfigValues = tuplesOfFields.map { nameTypeTuple =>
        Try {
          TypeHelper.get
            .filter(x => x.canHandle(nameTypeTuple.typ))
            .head
        } match {
          case Success(value) =>
            val func = ResolutionStrategy.get
            val key =
              if (func.isDefined) func.get.resolve(nameTypeTuple.name) else nameTypeTuple.name
            value.get(nameTypeTuple.typ)(config, Some(key))
        }
      }
      constructorMirror(seqOfConfigValues: _*)
    }
  }

  def getListOfFields(ip: universe.Type): List[FieldInfo] =
    ip.members.sorted.collect {
      case m: MethodSymbol if m.isCaseAccessor =>
        FieldInfo(m.name.toString, m.returnType)
    }
}
