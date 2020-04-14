package com.github.kiran002.kissconfig.config

import com.github.kiran002.kissconfig.config.api.ResolutionStrategy
import com.github.kiran002.kissconfig.config.impl.ResolutionStrategies
import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec

case class PrimaryTypes(myInt: Int, myString: String, myBoolean: Boolean)

case class PrimaryTypesU(my_int: Int, my_string: String, my_boolean: Boolean)

case class OptionalPrimaryTypes(myInt: Option[Int],
                                myString: Option[String],
                                myBoolean: Option[Boolean],
                                nonExistent: Option[String])

case class ListsMaps(lists: List[String], map: Map[String, String])

case class CustomTypes(pt: PrimaryTypes, lm: ListsMaps)

class KissConfigTest extends AnyFlatSpec {

  private val config    = ConfigFactory.defaultApplication()
  private val ptConfig  = KissConfig.get[PrimaryTypes](config)
  private val lmConfig  = KissConfig.get[ListsMaps](config)
  private val optConfig = KissConfig.get[OptionalPrimaryTypes](config)

  private val camelCaseToUnderScore = ResolutionStrategies.CamelCaseToUnderScore()
  private val underScoreToCamelCase = ResolutionStrategies.UnderScoreToCamelCase()
  private val ptWithResolutionStrategy =
    KissConfig.get[PrimaryTypes](config.getConfig("underscore"), Some(camelCaseToUnderScore))
  private val ptWithResolutionStrategy2 =
    KissConfig.get[PrimaryTypesU](config.getConfig("camelcase"), Some(underScoreToCamelCase))

  "KissConfig " should " be able to extract Primitives (Integers,Booleans)" in {
    assert(ptConfig.myInt == 5)
    assert(ptConfig.myBoolean)
  }
  it should "be able to extract strings" in {
    assert(ptConfig.myString.equals("myString"))
  }
  it should "be able to extract lists and maintain order" in {
    assert(lmConfig.lists.size == 3)
    assert(lmConfig.lists.head.equals("1"))
    assert(lmConfig.lists.last.equals("3"))
  }

  it should "be able to extract maps" in {
    val tmp = Map("key1" -> "val1", "key2" -> "val2")
    assert(lmConfig.map.equals(tmp))
  }

  it should "be able to extract Optional Strings/Ints/Booleans" in {
    assert(optConfig.myInt.get == 5)
    assert(optConfig.myBoolean.get)
    assert(optConfig.myString.get.equals("myString"))
  }

  it should " extract None, when config doesnt exist for an Optional Type" in {
    assert(optConfig.nonExistent.isEmpty)
  }

  it should " be able to resolve using CamelCaseToUnderScore Strategy" in {
    assert(ptWithResolutionStrategy.myInt == 5)
    assert(ptWithResolutionStrategy.myBoolean)
    assert(ptWithResolutionStrategy.myString.equals("myString"))
  }

  it should " be able to resolve using UnderScoreToCamelCase Strategy" in {
    assert(ptWithResolutionStrategy2.my_int == 5)
    assert(ptWithResolutionStrategy2.my_boolean)
    assert(ptWithResolutionStrategy2.my_string.equals("myString"))
  }
}
