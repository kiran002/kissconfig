package com.github.kiran002.kissconfig.config.impl

import com.github.kiran002.kissconfig.config.api.ResolutionStrategy

/**
  * Collection of ResolutionStrategies that can be used out of the box with KissConfig
  */
object ResolutionStrategies {

  /**
    * [[CamelCaseToUnderScore]] defines the resolution strategy to convert a string from camelcase to underscore separated.
    * @example
    *        a string like 'camelCase' is transformed to `camel_case`
    *
    */
  case class CamelCaseToUnderScore() extends ResolutionStrategy {

    /**
      * The resolve method translates a field name from its scala convention to the hocon conventin
      * @param s          : The name of the field in the case class
      * @return output string : the configuration key,
      *         which should be used to extract the value from the config object
      */
    override def resolve(s: String): String = {
      s.foldLeft("")((A, C) => { if (C.isUpper && A.length > 1) A + "_" + C else A + C })
        .toLowerCase
    }
  }

  /**
    * [[UnderScoreToCamelCase]] defines the resolution strategy to convert a string from underscore separated to camelcase.
    * @example
    *        a string like `camel_case` is transformed to 'camelCase'
    *
    */
  case class UnderScoreToCamelCase() extends ResolutionStrategy {

    /**
      * The resolve method translates a field name from its scala convention to the hocon conventin
      * @param s          : The name of the field in the case class
      * @return output string : the configuration key,
      *         which should be used to extract the value from the config object
      */
    override def resolve(s: String): String = {
      val strings = s.split("_")
      val (str, tail) =
        if (strings.head.length == 0) ("_" + strings.tail.head.toLowerCase, strings.tail.drop(1))
        else (strings.head.toLowerCase, strings.tail)
      tail.foldLeft(str)((A, B) => A + (B.head.toUpper) + B.tail.toLowerCase)
    }
  }

  /**
    * [[CamelCaseToDotCase]] defines the resolution strategy to convert a string from camelcase to dotcase .
    * @example
    *        a string like 'camelCase' is transformed to `camel_case`
    *
   */
  case class CamelCaseToDotCase() extends ResolutionStrategy {

    /**
      * The resolve method translates a field name from its scala convention to the hocon convention
      * @param str          : The name of the field in the case class
      * @return output string : the configuration key,
      *         which should be used to extract the value from the config object
      */
    override def resolve(str: String): String = {
      str.flatMap(char => if (char.isUpper) Seq('.', char.toLower) else Seq(char)).mkString
    }
  }

}
