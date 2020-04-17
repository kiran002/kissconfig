package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.KissConfig
import com.github.kiran002.kissconfig.config.models.Input
import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec

class BasicTypeHelperTest extends AnyFlatSpec {
  private val bth = new BasicTypeHelper()

  private val intSymbol    = KissConfig.get[Int]
  private val stringSymbol = KissConfig.get[String]
  private val boolSymbol   = KissConfig.get[Boolean]
  private val listSymbol   = KissConfig.get[List[String]]

  "BasicTypeHelper" should " handle Int/String/Boolean" in {
    assert(bth.canHandle(intSymbol))
    assert(bth.canHandle(stringSymbol))
    assert(bth.canHandle(boolSymbol))
  }

  it should "not handle List[String]" in {
    assert(!bth.canHandle(listSymbol))
  }

  it should "extract int/string/boolean properly" in {

    val config    = ConfigFactory.defaultApplication()
    val myInt     = bth.get(intSymbol)(Input(config, Some("myInt")))
    val myBoolean = bth.get(boolSymbol)(Input(config, Some("myBoolean")))
    val myString  = bth.get(stringSymbol)(Input(config, Some("myString")))

    assert(myInt == 5)
    assert(myBoolean == true)
    assert(myString.equals("myString"))
  }

}
