package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.{ResolutionStrategy, TypeHelper}
import com.github.kiran002.kissconfig.config.models.Input
import com.github.kiran002.kissconfig.config.{KissConfig, PrimaryTypes}
import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec

class OptionalTypeHelperTest extends AnyFlatSpec with BeforeAndAfter {

  private val bth = new OptionalTypeHelper()

  before {
    TypeHelper.register(new BasicTypeHelper)
    TypeHelper.register(new CollectionTypeHelper)
    TypeHelper.register(new CaseClassTypeHelper)
    ResolutionStrategy.register(None)
  }

  private val intSymbol          = KissConfig.get[Int]
  private val stringSymbol       = KissConfig.get[String]
  private val optionalListSymbol = KissConfig.get[Option[List[String]]]
  private val optionalString     = KissConfig.get[Option[String]]
  private val optionalCase       = KissConfig.get[Option[PrimaryTypes]]

  "OptionalTypeHelper" should " not handle Int/String/Boolean" in {
    assert(!bth.canHandle(intSymbol))
    assert(!bth.canHandle(stringSymbol))

  }

  it should " handle optionalString and optionalListSymbol" in {
    assert(bth.canHandle(optionalListSymbol))
    assert(bth.canHandle(optionalString))
  }

  it should "extract int/string/boolean properly" in {
    val config = ConfigFactory.defaultApplication()
    val myString =
      bth.get(optionalString)(Input(config, Some("myString"))).asInstanceOf[Option[String]]
    val myString2 =
      bth.get(optionalString)(Input(config, Some("myString2"))).asInstanceOf[Option[String]]
    assert(myString.get.equals("myString"))
    assert(myString2.isEmpty)
  }

  it should "extract lists properly" in {
    val config = ConfigFactory.defaultApplication()
    val lists = bth
      .get(optionalListSymbol)(Input(config, Some("lists")))
      .asInstanceOf[Option[List[String]]]
      .get
    val lists2 =
      bth.get(optionalListSymbol)(Input(config, Some("lists1"))).asInstanceOf[Option[List[String]]]
    assert(lists.size == 3)
    assert(lists.head.equals("1"))
    assert(lists.last.equals("3"))
    assert(lists2.isEmpty)
  }

  it should "extract case classes properly" in {
    val config = ConfigFactory.defaultApplication()

    val optic = bth
      .get(optionalCase)(Input(config, Some("pt")))
      .asInstanceOf[Option[PrimaryTypes]]

    val lists2 =
      bth.get(optionalCase)(Input(config, Some("pt2"))).asInstanceOf[Option[PrimaryTypes]]

    assert(lists2.isEmpty)
    assert(optic.get.myInt == 5)
    assert(optic.get.myBoolean)
    assert(optic.get.myString.equals("myString"))

  }

}
