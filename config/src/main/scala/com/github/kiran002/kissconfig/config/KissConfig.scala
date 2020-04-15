package com.github.kiran002.kissconfig.config

import com.github.kiran002.kissconfig.config.api.{ResolutionStrategy, TypeHelper}
import com.github.kiran002.kissconfig.config.impl.{
  BasicTypeHelper,
  CaseClassTypeHelper,
  CollectionTypeHelper,
  OptionalTypeHelper
}
import com.github.kiran002.kissconfig.config.internal.KissConfigException
import com.github.kiran002.kissconfig.config.models.Input
import com.typesafe.config.Config

import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => ru}
import scala.util.{Failure, Success, Try}

class KissConfig(config: Config, resolutionStrategy: Option[ResolutionStrategy] = None) {

  TypeHelper.register(new BasicTypeHelper)
  TypeHelper.register(new OptionalTypeHelper)
  TypeHelper.register(new CollectionTypeHelper)
  TypeHelper.register(new CaseClassTypeHelper)

  ResolutionStrategy.register(resolutionStrategy)

  def get[T: TypeTag]: T = {
    Try {
      TypeHelper.get
        .filter(x => x.canHandle(ru.typeOf[T]))
        .head
    } match {
      case Success(value) =>
        value.get(ru.typeOf[T])(Input(config, None)).asInstanceOf[T]
      case Failure(exception) =>
        throw KissConfigException(s"Type(${ru.typeOf[T]} currently not supported", exception)
    }
  }
}
object KissConfig {

  def get[T] = ru.typeOf[T]

}
