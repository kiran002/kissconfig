package com.github.kiran002.kissconfig.config.internal

import com.typesafe.config.Config

import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => ru}

object Helper {

  def get(config: Config,
          classSymbol: ru.ClassSymbol,
          classType: ru.Type): Any = {
    val tuplesOfFields = getListOfFields(classType)
    val runtimeMirror = ru.runtimeMirror(getClass.getClassLoader)
    val classMirror = runtimeMirror.reflectClass(classSymbol)
    val constructorSymbol = classType.decl(ru.termNames.CONSTRUCTOR).asMethod
    val constructorMirror = classMirror.reflectConstructor(constructorSymbol)

    val p = new TypeHelper(config)

    val seqOfConfigValues = tuplesOfFields.map { nameTypeTuple =>
      val classSymbol = nameTypeTuple._2.typeSymbol.asClass
      p.func.isDefinedAt(nameTypeTuple) match {
        case true => p.func(nameTypeTuple)
        case false if classSymbol.isCaseClass => // must be custom type
          this.get(config.getConfig(nameTypeTuple._1), classSymbol, nameTypeTuple._2)
      }
    }
    constructorMirror(seqOfConfigValues: _*)
  }

  private def getListOfFields(ip: ru.Type) = ip.members.sorted.collect {
    case m: MethodSymbol if m.isCaseAccessor => (m.name.toString, m.returnType)
  }
}
