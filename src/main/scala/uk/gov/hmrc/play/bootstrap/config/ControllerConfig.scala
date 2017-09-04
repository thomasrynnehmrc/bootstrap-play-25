/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.play.bootstrap.config

import play.api.Configuration

case class ControllerConfig(logging: Boolean = true, auditing: Boolean = true)

object ControllerConfig {

  def fromConfig(configuration: Configuration): ControllerConfig = {
    val logging = configuration.getBoolean("needsLogging").getOrElse(true)
    val auditing = configuration.getBoolean("needsAuditing").getOrElse(true)
    ControllerConfig(logging, auditing)
  }
}

case class ControllerConfigs(private val controllers: Map[String, ControllerConfig]) {

  def get(controllerName: String): ControllerConfig =
    controllers.getOrElse(controllerName, ControllerConfig())
}

object ControllerConfigs {

  def fromConfig(configuration: Configuration): ControllerConfigs = {

    val configMap = configuration.getConfig("controllers").map {
      config =>
        config.subKeys.foldLeft(Map.empty[String, ControllerConfig]) {
          case (map, key) =>
            config.getConfig(key)
              .map(ControllerConfig.fromConfig)
              .map(c => map + (key -> c))
              .getOrElse(map)
        }
    }.getOrElse(Map.empty)

    ControllerConfigs(configMap)
  }
}