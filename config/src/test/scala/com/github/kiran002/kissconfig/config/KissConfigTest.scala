package com.github.kiran002.kissconfig.config

import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec

case class PrimaryTypes(myInt: Int, myString: String, myBoolean: Boolean)

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
}