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

/**
 * @author Hippo
 * @version 5.0.0, 10/26/19
 * @since 5.0.0
 * <p>
 * The asynchronous publishing configuration configures information on how asynchronous handlers are handled.
 * </p>
 */
public final class AsynchronousPublicationConfiguration implements Configuration<AsynchronousPublicationConfiguration> {

  /**
   * The dispatcher count; how many threads.
   */
  private int dispatcherCount;

  /**
   * Weather to suppress {@link InterruptedException}s.
   */
  private boolean suppressDispatcherInterrupt;

  /**
   * Weather to use daemon threads.
   */
  private boolean daemonThreads;

  /**
   * Gets the default configuration.
   *
   * @return The default configuration.
   */
  public static AsynchronousPublicationConfiguration getDefault() {
    return new AsynchronousPublicationConfiguration().provideDefault();
  }

  /**
   * @inheritDoc
   */
  @Override
  public AsynchronousPublicationConfiguration provideDefault() {
    this.setDispatcherCount(3);
    this.setSuppressDispatcherInterrupt(true);
    this.setDaemonThreads(false);
    return this;
  }

  /**
   * Gets the dispatcher count.
   *
   * @return The dispatcher count.
   */
  public int getDispatcherCount() {
    return dispatcherCount;
  }

  /**
   * Sets the dispatcher count.
   *
   * <p>
   * Whatever value {@code dispatcherCount} will be is how many threads the bus will have.
   * </p>
   *
   * @param dispatcherCount The dispatcher count.
   */
  public void setDispatcherCount(int dispatcherCount) {
    this.dispatcherCount = dispatcherCount;
  }

  /**
   * Gets weather {@link InterruptedException}s should be suppressed.
   *
   * @return If they should be suppressed.
   */
  public boolean isSuppressDispatcherInterrupt() {
    return suppressDispatcherInterrupt;
  }

  /**
   * Gets weather {@link InterruptedException}s should be suppressed.
   *
   * @param suppressDispatcherInterrupt If they should be suppressed.
   */
  public void setSuppressDispatcherInterrupt(boolean suppressDispatcherInterrupt) {
    this.suppressDispatcherInterrupt = suppressDispatcherInterrupt;
  }

  /**
   * Gets weather to use daemon threads.
   *
   * @return If the threads should be daemon.
   */
  public boolean useDaemonThreads() {
    return daemonThreads;
  }

  /**
   * Sets weather the bus should be use daemon threads.
   *
   * @param daemonThreads If they should use daemon threads.
   */
  public void setDaemonThreads(boolean daemonThreads) {
    this.daemonThreads = daemonThreads;
  }
}
