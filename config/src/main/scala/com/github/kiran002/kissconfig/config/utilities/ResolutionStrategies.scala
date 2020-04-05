package com.github.kiran002.kissconfig.config.utilities

object ResolutionStrategies {

  def camelCaseToUnderScore(s: String): String =
    s.foldLeft("")((A, C) => { if (C.isUpper && A.length > 1) A + "_" + C else A + C }).toLowerCase

  def underScoreToCamelCase(s: String): String = {
    val strings = s.split("_")
    val (str, tail) =
      if (strings.head.length == 0) ("_" + strings.tail.head.toLowerCase, strings.tail.drop(1))
      else (strings.head.toLowerCase, strings.tail)
    tail.foldLeft(str)((A, B) => A + (B.head.toUpper) + B.tail.toLowerCase)
  }

}
