package com.github.kiran002.kissconfig.config.api

import com.typesafe.config.Config

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.Type

trait TypeHelper {

  def canHandle(objType: Type): Boolean

  def get(objType: Type): (Config, Option[String]) => Any

}

object TypeHelper {

  private val listBuffer: ListBuffer[TypeHelper] = ListBuffer()

  def register(tt: TypeHelper): Unit = {
    listBuffer += tt
  }

  def get: List[TypeHelper] = listBuffer.toList

}
