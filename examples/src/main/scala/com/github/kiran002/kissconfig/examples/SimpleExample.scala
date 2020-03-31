package com.github.kiran002.kissconfig.examples

import com.github.kiran002.kissconfig.config.KissConfig
import com.github.kiran002.kissconfig.examples.models.{
  BooleanMaps,
  CustomTypes,
  Lists,
  ListsMaps,
  PrimaryTypes
}
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

  val customTypes        = KissConfig.get[CustomTypes](config)
  val nestedPrimaryTypes = customTypes.pt
  println(s"Nested My Integer : ${nestedPrimaryTypes.myInt}")
  println(s"Nested My String : ${nestedPrimaryTypes.myString}")
  println(s"Nested My Boolean : ${nestedPrimaryTypes.myBoolean}")

  val nestedListsMaps = customTypes.lm
  println(s"Nested My List : ${nestedListsMaps.lists}")
  println(s"Nested My Maps : ${nestedListsMaps.map}")

  val booleanMaps = KissConfig.get[BooleanMaps](config)
  println(s"Maps of Booleans: ${booleanMaps.ma}")

  val lis = KissConfig.get[Lists](config)
  println(s"Lists of Double : ${lis.listsDouble}")
  println(s"Sum of Double : ${lis.listsDouble.sum}")
  println(s"Lists of Int : ${lis.listsInt}")
  println(s"Max of Int : ${lis.listsInt.max}")
}
