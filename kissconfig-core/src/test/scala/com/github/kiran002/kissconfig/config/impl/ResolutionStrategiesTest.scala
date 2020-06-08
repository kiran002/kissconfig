package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.impl.ResolutionStrategies._
import org.scalatest.flatspec.AnyFlatSpec
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ResolutionStrategiesTest extends AnyFlatSpec {

  val camelCaseToUnderScore = CamelCaseToUnderScore()
  val camelCaseToDotCase    = CamelCaseToDotCase()
  val underScoreToCamelCase = UnderScoreToCamelCase()

  "CamelCaseToUnderscore strategy " should " convert camelcase to _" in {
    assert(camelCaseToUnderScore.resolve("testMyCase").equals("test_my_case"))
  }

  it should " ignore the first character" in {
    val str = camelCaseToUnderScore.resolve("TestMyCase")
    assert(str.equals("test_my_case"))
  }

  "UnderscoreToCamelCase strategy " should " convert _ to camelcase" in {
    val str = underScoreToCamelCase.resolve("test_my_case")
    assert(str.equals("testMyCase"))
  }

  it should " ignore the first character" in {
    val str = underScoreToCamelCase.resolve("_test_my_case")
    assert(str.equals("_testMyCase"))
  }

  "CamelCaseToDotCase strategy " should " convert camelcase to dotcase" in {
    val str = camelCaseToDotCase.resolve("testMyCase")
    assert(str.equals("test.my.case"))
  }

  it should " not do anything incase string is lowercase" in {
    val str = camelCaseToDotCase.resolve("test")
    assert(str.equals("test"))
  }
}
