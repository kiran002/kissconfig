package com.github.kiran002.kissconfig.config.api

trait ResolutionStrategy {

  def resolve(string: String): String

}

object ResolutionStrategy {

  private var rs: Option[ResolutionStrategy] = None

  def register(tt: Option[ResolutionStrategy]): Unit = {
    rs = tt
  }

  def get: Option[ResolutionStrategy] = rs
}
