package com.github.kiran002.kissconfig.config

import com.github.kiran002.kissconfig.config.api.{ResolutionStrategy, TypeHelper}
import com.github.kiran002.kissconfig.config.impl.{
  BasicTypeHelper,
  CaseClassTypeHelper,
  CollectionTypeHelper,
  OptionalTypeHelper
}
import com.github.kiran002.kissconfig.config.internal.KissConfigException
import com.typesafe.config.Config

import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => ru}
import scala.util.{Failure, Success, Try}

object KissConfig {

  TypeHelper.register(new BasicTypeHelper)
  TypeHelper.register(new OptionalTypeHelper)
  TypeHelper.register(new CollectionTypeHelper)
  TypeHelper.register(new CaseClassTypeHelper)

  def get[T: TypeTag](config: Config, resolutionStrategy: Option[ResolutionStrategy] = None): T = {
    ResolutionStrategy.register(resolutionStrategy)
    Try {
      TypeHelper.get
        .filter(x => x.canHandle(ru.typeOf[T]))
        .head
    } match {
      case Success(value) =>
        value.get(ru.typeOf[T])(config, None).asInstanceOf[T]
      case Failure(exception) =>
        throw KissConfigException(s"Type(${ru.typeOf[T]} currently not supported", exception)
    }
  }
}
