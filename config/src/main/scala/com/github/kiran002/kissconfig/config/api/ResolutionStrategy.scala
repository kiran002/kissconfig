package com.github.kiran002.kissconfig.config.api

/**
  * Interface used to define a [[ResolutionStrategy]].
  *
  *  Naming conventions can be different going from scala to hocon config.
  *  [[ResolutionStrategy]] defines the api that is used for the translation of scala field names to
  *  hocon config keys
  *
  */
trait ResolutionStrategy {

  /**
    *  The resolve method translates a field name from its scala convention to the hocon conventin
    * @param input          : The name of the field in the case class
    * @return output string : the configuration key,
    *         which should be used to extract the value from the config object
    */
  def resolve(input: String): String

}

/**
  *  Static Wrapper for [[ResolutionStrategy]] that can used to register new implementation of
  *  Resolution Strategy during runtime
  *
  *  Maximum one resolution strategy is supported at a time
  *  If no Resolution Strategy is provided the fieldname is used as the config key
  */
object ResolutionStrategy {

  private var rs: Option[ResolutionStrategy] = None

  /**
    *  Register a new Resolution strategy
    * @param maybeResolutionStrategy : Optional resolution strategy (if not provide None will be registered)
    */
  def register(maybeResolutionStrategy: Option[ResolutionStrategy]): Unit = {
    rs = maybeResolutionStrategy
  }

  /**
    *
    * @return resolution strategy
    */
  def get: Option[ResolutionStrategy] = rs
}
