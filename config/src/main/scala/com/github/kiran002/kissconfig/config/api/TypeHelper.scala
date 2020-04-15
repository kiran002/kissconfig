package com.github.kiran002.kissconfig.config.api

import com.github.kiran002.kissconfig.config.models.Input

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.Type

/**
  * Interface used to define a [[TypeHelper]].
  *
  *  Given a type (T), the TypeHelper is expected to be able
  *
  *      1. Extract configuration value from the config object
  *      2. Parse the value to the target type
  *
  */
trait TypeHelper {

  /**
    *  Is the typehelper able to handle this particular type ([[objType]])
    * @param objType: type of the object
    * @return : true if it can handle [[objType]] false otherwise
    */
  def canHandle(objType: Type): Boolean

  /**
    *
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
    * @param typeHelper
    */
  def register(typeHelper: TypeHelper): Unit = {
    listBuffer += typeHelper
  }

  /**
    * Get all registered TypeHelpers
    * @return
    */
  def get: List[TypeHelper] = listBuffer.toList.reverse

}
