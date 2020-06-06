package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.KissConfig
import com.github.kiran002.kissconfig.config.models.Input

import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BasicTypeHelperTest extends AnyFlatSpec {
  private val bth = new BasicTypeHelper()

  private val intSymbol    = KissConfig.get[Int]
  private val shortSymbol    = KissConfig.get[Short]
  private val longSymbol    = KissConfig.get[Long]
  private val stringSymbol = KissConfig.get[String]
  private val boolSymbol   = KissConfig.get[Boolean]
  private val doubleSymbol   = KissConfig.get[Double]
  private val listSymbol   = KissConfig.get[List[String]]

  "BasicTypeHelper" should " handle Short/Int/Long/Double/String/Boolean" in {
    assert(bth.canHandle(intSymbol))
    assert(bth.canHandle(shortSymbol))
    assert(bth.canHandle(longSymbol))
    assert(bth.canHandle(stringSymbol))
    assert(bth.canHandle(boolSymbol))
    assert(bth.canHandle(doubleSymbol))
  }

  it should "not handle List[String]" in {
    assert(!bth.canHandle(listSymbol))
  }

  it should "extract short/int/long/string/boolean properly" in {

    val config    = ConfigFactory.defaultApplication()
    val myInt     = bth.get(intSymbol)(Input(config, Some("myInt")))
    val myBoolean = bth.get(boolSymbol)(Input(config, Some("myBoolean")))
    val myString  = bth.get(stringSymbol)(Input(config, Some("myString")))
    val myShort  = bth.get(shortSymbol)(Input(config, Some("myShort")))
    val myLong  = bth.get(longSymbol)(Input(config, Some("myLong")))
    val myDouble  = bth.get(doubleSymbol)(Input(config, Some("myDouble")))

    assert(myInt == 5)
    assert(myDouble == 1.5)
    assert(myBoolean == true)
    assert(myString.equals("myString"))
    assert(myShort==6)
    assert(myLong==92233720368547758L)
  }

}
