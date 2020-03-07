package com.github.kiran002.kissconfig.examples

import com.github.kiran002.kissconfig.config.KissConfig
import com.github.kiran002.kissconfig.examples.models.{CustomTypes, ListsMaps, PrimaryTypes}
import com.typesafe.config.ConfigFactory

object SimpleExample extends App {

  private val config = ConfigFactory.defaultApplication()

  val primaryTypes = KissConfig.get[PrimaryTypes](config)
  println(s"My Integer : ${primaryTypes.myInt}")
  println(s"My String : ${primaryTypes.myString}")
  println(s"My Boolean : ${primaryTypes.myBoolean}")

  val listsMaps = KissConfig.get[ListsMaps](config)
  println(s"My List : ${listsMaps.lists}")
  println(s"My Maps : ${listsMaps.map}")

  val customTypes = KissConfig.get[CustomTypes](config)
  val NestedPrimaryTypes = customTypes.pt
  println(s"Nested My Integer : ${NestedPrimaryTypes.myInt}")
  println(s"Nested My String : ${NestedPrimaryTypes.myString}")
  println(s"Nested My Boolean : ${NestedPrimaryTypes.myBoolean}")

  val NestedListsMaps = customTypes.lm
  println(s"Nested My List : ${NestedListsMaps.lists}")
  println(s"Nested My Maps : ${NestedListsMaps.map}")
}
