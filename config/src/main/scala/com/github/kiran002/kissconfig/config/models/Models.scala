package com.github.kiran002.kissconfig.config.models

import com.typesafe.config.Config

import scala.reflect.runtime.universe.Type

case class FieldInfo(name: String, typ: Type)

case class Input(config: Config, configKey: Option[String])
