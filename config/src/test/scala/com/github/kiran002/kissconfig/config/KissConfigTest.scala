package com.github.kiran002.kissconfig.config

import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec

case class PrimaryTypes(myInt: Int, myString: String, myBoolean: Boolean)

case class ListsMaps(lists: List[String], map: Map[String, String])

case class CustomTypes(pt: PrimaryTypes, lm: ListsMaps)


class KissConfigTest extends AnyFlatSpec {

  private val config = ConfigFactory.defaultApplication()
  private val appConfig = KissConfig.get[PrimaryTypes](config)

  "KissConfig " should " be able to extract Primitives (Integers,Booleans)" in {
    assert(appConfig.myInt == 5)
    assert(appConfig.myBoolean)
  }
  "It" should "be able to extract strings" in {
    assert(appConfig.myString.equals("myString"))
  }

}
