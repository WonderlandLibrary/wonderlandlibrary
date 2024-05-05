/*
 * Copyright 2020 Hippo
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
 *
 */

package rip.hippo.lwjeb.configuration.config.impl;

import rip.hippo.lwjeb.configuration.config.Configuration;
import rip.hippo.lwjeb.listener.ListenerClassLoader;


/**
 * @author Hippo
 * @version 5.0.0, 10/27/19
 * @since 5.0.0
 */
public final class BusConfiguration implements Configuration<BusConfiguration> {

  /**
   * The identifier, or name, of the bus.
   */
  private String identifier;

  /**
   * The class loader it will use to load dynamic invocation classes.
   */
  private ListenerClassLoader listenerClassLoader;

  /**
   * Static wrapper to provide the default bus configuration.
   *
   * @return The bus configuration.
   */
  public static BusConfiguration getDefault() {
    return new BusConfiguration().provideDefault();
  }

  /**
   * Provides the default bus configuration.
   *
   * @return The bus configuration.
   */
  @Override
  public BusConfiguration provideDefault() {
    this.setIdentifier("LWJEB");
    this.setListenerClassLoader(new ListenerClassLoader(this.getClass().getClassLoader()));
    return this;
  }

  /**
   * Gets the bus identifier.
   *
   * @return The identifier.
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Sets the bus identifier.
   *
   * @param identifier The new identifier.
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Gets the listener class loader
   *
   * @return The listener class loader
   */
  public ListenerClassLoader getListenerClassLoader() {
    return listenerClassLoader;
  }

  /**
   * Sets the listener class loader
   *
   * @param listenerClassLoader Listener class loader
   */
  public void setListenerClassLoader(ListenerClassLoader listenerClassLoader) {
    this.listenerClassLoader = listenerClassLoader;
  }

}
