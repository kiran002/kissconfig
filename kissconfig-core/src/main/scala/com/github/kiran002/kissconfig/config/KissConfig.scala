package com.github.kiran002.kissconfig.config

import com.github.kiran002.kissconfig.config.api.TypeHelper
import com.github.kiran002.kissconfig.config.internal.KissConfigException
import com.github.kiran002.kissconfig.config.models.Input
import com.typesafe.config.Config

import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}

/**
  * KissConfig
  *
 * @param config : Input [[Config]] object, that acts as the source
  */
class KissConfig(config: Config) {

  /**
    *
     * @param key : When the expect type is not a top level object in config, you can use key to extract a particular key and extract values
    * @tparam T : Type Param of the input class that should be populated with [[config]]
    * @return : Populated instance of type [[T]]
    */
  def get[T: TypeTag](key: Option[String] = None, prefix: Option[String] = None): T = {
    val objType = typeOf[T]
    Try {
      TypeHelper.get.filter(_.canHandle(objType)).head
    } match {
      case Success(value) =>
        value.get(objType)(Input(config, key, prefix)).asInstanceOf[T]
      case Failure(exception) =>
        throw KissConfigException(s"""Type($objType) currently not supported""", exception)
    }
  }

}

object KissConfig {

  /**
    * Wrapper around [[scala.reflect.runtime.universe]]'s typeOf method
    *
     * @tparam T : Input type parameter T
    * @return : type of [[T]]
    */
  def get[T: TypeTag]: Type = typeOf[T]

}
