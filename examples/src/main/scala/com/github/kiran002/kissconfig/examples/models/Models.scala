package com.github.kiran002.kissconfig.examples.models

case class PrimaryTypes(myInt: Int, myString: String, myBoolean: Boolean)

case class ListsMaps(lists: List[String], map: Map[String, String])

case class CustomTypes(pt: PrimaryTypes, lm: ListsMaps)

case class Lists(listsInt: List[Int], listsDouble: List[Double])

case class BooleanMaps(ma: Map[String, Boolean])
