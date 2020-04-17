package com.github.kiran002.kissconfig.config.models

import com.typesafe.config.Config

import scala.reflect.runtime.universe.Type

/**
  * Model for holding Field information
  * @param name: Field name
  * @param typ:  Field Type
  */
case class FieldInfo(name: String, typ: Type)

/**
  * Model for holding Input config and key
  * @param config : Input source (Config Object)
  * @param configKey: Input Key (used to extract value from [[config]]
  */
case class Input(config: Config, configKey: Option[String])
