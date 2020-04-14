package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.ResolutionStrategy

object ResolutionStrategies {

  case class CamelCaseToUnderScore() extends ResolutionStrategy {
    override def resolve(s: String): String = {
      s.foldLeft("")((A, C) => { if (C.isUpper && A.length > 1) A + "_" + C else A + C })
        .toLowerCase
    }
  }

  case class UnderScoreToCamelCase() extends ResolutionStrategy {
    override def resolve(s: String): String = {
      val strings = s.split("_")
      val (str, tail) =
        if (strings.head.length == 0) ("_" + strings.tail.head.toLowerCase, strings.tail.drop(1))
        else (strings.head.toLowerCase, strings.tail)
      tail.foldLeft(str)((A, B) => A + (B.head.toUpper) + B.tail.toLowerCase)
    }
  }
}
