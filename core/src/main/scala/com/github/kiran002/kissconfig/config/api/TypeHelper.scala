package com.github.kiran002.kissconfig.config.api

import com.github.kiran002.kissconfig.config.models.Input

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe.{Type, typeOf}

/**
  * Interface used to define a [[TypeHelper]].
  *
  * Given a type (T), the TypeHelper is expected to be able
  *
  *      1. Extract configuration value from the config object
  *      2. Parse the value to the target type
  *
  * @example
  *   {{{
  *   class StringTypeHelper extends TypeHelper {
  *       private val stringExtractor: Input => Any  = x => x.config.getString(x.configKey.get)
  *
  *        override def canHandle(objType: universe.Type): Boolean =
  *                      objType.typeSymbol==universe.typeOf[String].typeSymbol
  *
  *       override def get(objType: universe.Type): Input => Any = stringExtractor
  *
  *    }
  *    }}}
  *
  * @note More example are available under [[com.github.kiran002.kissconfig.config.impl]]
  */
trait TypeHelper {

  protected val StringType: universe.Type  = typeOf[String]
  protected val IntegerType: universe.Type = typeOf[Int]
  protected val BooleanType: universe.Type = typeOf[Boolean]
  protected val DoubleType: universe.Type  = typeOf[Double]

  /**
    * Is the typehelper able to handle this particular type ([[objType]])
    * @param objType: type of the object
    * @return : true if it can handle [[objType]] false otherwise
    */
  def canHandle(objType: Type): Boolean

  /**
    * Returns a function that can be used to extract values compatible with objType
    * @param objType  type of the object
    * @return : function, that takes config object and config key as input and returns the extracted value
    */
  def get(objType: Type): Input => Any

}

/**
  *  Static Wrapper for [[TypeHelper]] that can used to register new implementation of
  *  TypeHelper during runtime
  *
  *  Any Number of TypeHelper can be registered, the helper registered latest has the highest precedence when executing
  *
  */
object TypeHelper {

  private val listBuffer: ListBuffer[TypeHelper] = ListBuffer()

  /**
    * Register new [[TypeHelper]]
    * @param typeHelper: TypeHelper to register
    */
  def register(typeHelper: TypeHelper): Unit = {
    listBuffer += typeHelper
  }

  /**
    * Get all registered TypeHelpers, every new registered type helper has higher precedence to all the older typehelpers.
    * @example
    *          If TypeHelperA and TypeHelperA1 can both handle type A and TypeHelperA is registered before TypeHelperA1
    *          TypeHelperA1 will always be chosen over TypeHelperA for the type A
    *
    *
    * @note This is not a bug, but a design decision to allow for users to override the type helpers provided by KissConfig.
    *
    *
    * @return : list of all Registered [[TypeHelper]]'s in reverse order
    */
  def get: List[TypeHelper] = listBuffer.toList.reverse

}
