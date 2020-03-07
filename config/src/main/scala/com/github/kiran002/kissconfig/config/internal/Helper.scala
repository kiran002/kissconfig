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
          tr: List[(String, ru.Type)],
          clz: ru.ClassSymbol,
          rt: ru.Type): Any = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val cm = m.reflectClass(clz)
    val ctor = rt.decl(ru.termNames.CONSTRUCTOR).asMethod
    val ctorm = cm.reflectConstructor(ctor)

    val bool = ru.typeOf[Boolean].resultType
    val in = ru.typeOf[Int].resultType
    val st = ru.typeOf[String].resultType
    val ls = ru.typeOf[List[String]].resultType
    val ops = ru.typeOf[Option[String]].resultType
    val ops2 = ru.typeOf[Option[_]].resultType
    val ms = ru.typeOf[Map[String, Any]].resultType
    val mss = ru.typeOf[Map[String, String]].resultType


    import scala.collection.JavaConverters._

    val t = tr.map { x =>

      val h = x._2.typeSymbol.asClass
      val cm = m.reflectClass(h)
      x._2.typeSymbol
      val typ = Option(x._2.typeArgs)


      val co = x._2.typeSymbol


      println(typ, x._2.typeSymbol, x._2.termSymbol, h.isPrimitive)

      x._2 match {
        case `bool` => config.getBoolean(x._1)
        case `in` => config.getInt(x._1)
        case `st` => config.getString(x._1)
        case `ls` => config.getStringList(x._1).asScala.toList
        case `ops2` => scala.util.Try(config.getString(x._1)).toOption
        case `ops` => scala.util.Try(config.getString(x._1)).toOption
        case `ms` =>
          config
            .getConfig(x._1)
            .entrySet()
            .asScala
            .map { x =>
              x.getKey -> x.getValue.unwrapped().asInstanceOf[Any]
            }
            .toMap
        case `mss` =>
          val temp = config.getConfig(x._1)
          temp
            .entrySet()
            .asScala
            .map { x =>
              x.getKey -> x.getValue.unwrapped().asInstanceOf[String]
            }
            .toMap

        case o if h.isCaseClass => // must be custom type, if subclass of product
          val rr = o.members.sorted.collect {
            case m: MethodSymbol if m.isCaseAccessor =>
              (m.name.toString, m.returnType)
          }

          val ctor = o.decl(ru.termNames.CONSTRUCTOR).asMethod
          val ctorm = cm.reflectConstructor(ctor)
          val config2 = config.getConfig(x._1)
          this.get(config2, rr, h, o)
      }
    }
    //    println(t)
    ctorm(t: _*)
  }
}
