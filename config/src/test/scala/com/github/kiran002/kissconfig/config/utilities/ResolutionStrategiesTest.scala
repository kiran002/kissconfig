package com.github.kiran002.kissconfig.config.utilities

import org.scalatest.flatspec.AnyFlatSpec

class ResolutionStrategiesTest extends AnyFlatSpec {

  "CamelCaseToUnderscore strategy " should " convert camelcase to _" in {
    assert(ResolutionStrategies.camelCaseToUnderScore("testMyCase").equals("test_my_case"))
  }

  it should " ignore the first character" in {
    val str = ResolutionStrategies.camelCaseToUnderScore("TestMyCase")
    assert(str.equals("test_my_case"))
  }

  "UnderscoreToCamelCase strategy " should " convert _ to camelcase" in {
    val str = ResolutionStrategies.underScoreToCamelCase("test_my_case")
    assert(str.equals("testMyCase"))
  }

  it should " ignore the first character" in {
    val str = ResolutionStrategies.underScoreToCamelCase("_test_my_case")
    assert(str.equals("_testMyCase"))
  }
}
