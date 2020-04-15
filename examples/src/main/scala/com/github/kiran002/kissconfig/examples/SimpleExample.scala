package com.github.kiran002.kissconfig.examples

import com.github.kiran002.kissconfig.config.KissConfig
import com.github.kiran002.kissconfig.examples.models._
import com.typesafe.config.ConfigFactory

object SimpleExample extends App {

  private val config = ConfigFactory.defaultApplication()
  private val kc     = new KissConfig(config)
  val primaryTypes   = kc.get[PrimaryTypes]
  println(s"My Integer : ${primaryTypes.myInt}")
  println(s"My String : ${primaryTypes.myString}")
  println(s"My Boolean : ${primaryTypes.myBoolean}")

  val listsMaps = kc.get[ListsMaps]
  println(s"My List : ${listsMaps.lists}")
  println(s"My Maps : ${listsMaps.map}")

  val customTypes        = kc.get[CustomTypes]
  val nestedPrimaryTypes = customTypes.pt
  println(s"Nested My Integer : ${nestedPrimaryTypes.myInt}")
  println(s"Nested My String : ${nestedPrimaryTypes.myString}")
  println(s"Nested My Boolean : ${nestedPrimaryTypes.myBoolean}")

  val nestedListsMaps = customTypes.lm
  println(s"Nested My List : ${nestedListsMaps.lists}")
  println(s"Nested My Maps : ${nestedListsMaps.map}")

  val booleanMaps = kc.get[BooleanMaps]
  println(s"Maps of Booleans: ${booleanMaps.ma}")

  val lis = kc.get[Lists]
  println(s"Lists of Double : ${lis.listsDouble}")
  println(s"Sum of Double : ${lis.listsDouble.sum}")
  println(s"Lists of Int : ${lis.listsInt}")
  println(s"Max of Int : ${lis.listsInt.max}")
}
