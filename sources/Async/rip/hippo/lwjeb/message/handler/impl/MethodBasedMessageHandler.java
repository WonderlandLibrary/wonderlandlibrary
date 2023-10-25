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

package rip.hippo.lwjeb.message.handler.impl;

import rip.hippo.lwjeb.configuration.config.impl.BusConfiguration;
import rip.hippo.lwjeb.configuration.config.impl.ExceptionHandlingConfiguration;
import rip.hippo.lwjeb.configuration.config.impl.ListenerFactoryConfiguration;
import rip.hippo.lwjeb.filter.MessageFilter;
import rip.hippo.lwjeb.listener.Listener;
import rip.hippo.lwjeb.message.handler.MessageHandler;
import rip.hippo.lwjeb.wrapped.WrappedType;

import java.lang.reflect.Method;

/**
 * @author Hippo
 * @version 5.0.0, 11/2/19
 * @since 5.0.0
 * <p>
 * This is a method based implementation of the message handler.
 * </p>
 */
public final class MethodBasedMessageHandler<T> implements MessageHandler<T> {

  /**
   * The parent of the method.
   */
  private final Object parent;

  /**
   * The topic.
   */
  private final Class<T> topic;

  /**
   * The listener used for invocation.
   */
  private final Listener listener;

  /**
   * The filters.
   */
  private final MessageFilter<T>[] filters;

  /**
   * The exception handling strategy, used to handle errors that may happen during invocation.
   */
  private final ExceptionHandlingConfiguration exceptionHandlingConfiguration;

  /**
   * Weather the handler is for {@link WrappedType}s.
   */
  private final boolean wrapped;

  /**
   * The priority of the handler.
   */
  private final int priority;

  /**
   * Creates a new {@link MethodBasedMessageHandler}.
   *
   * @param parent                         The parent.
   * @param topic                          The topic.
   * @param method                         The method.
   * @param filters                        The filters.
   * @param exceptionHandlingConfiguration The exception handling configuration.
   * @param wrapped                        Weather if its wrapped.
   */
  public MethodBasedMessageHandler(Object parent,
                                   Class<T> topic,
                                   Method method,
                                   MessageFilter<T>[] filters,
                                   BusConfiguration config,
                                   ListenerFactoryConfiguration listenerFactoryConfiguration,
                                   ExceptionHandlingConfiguration exceptionHandlingConfiguration,
                                   boolean wrapped,
                                   int priority) {
    this.parent = parent;
    this.topic = topic;
    this.listener = listenerFactoryConfiguration.getListenerFactory().create(parent.getClass(), method, wrapped ? WrappedType.class : topic, config);
    this.filters = filters;
    this.exceptionHandlingConfiguration = exceptionHandlingConfiguration;
    this.wrapped = wrapped;
    this.priority = priority;
  }

  /**
   * @inheritDoc
   */
  @Override
  public void handle(T topic) {
    try {
      listener.invoke(parent, wrapped ? new WrappedType(topic) : topic);
    } catch (Throwable t) {
      exceptionHandlingConfiguration.getExceptionHandler().handleException(t);
    }
  }

  /**
   * @inheritDoc
   */
  @Override
  public Class<T> getTopic() {
    return topic;
  }

  /**
   * @inheritDoc
   */
  @Override
  public MessageFilter<T>[] getFilters() {
    return filters;
  }

  /**
   * @inheritDoc
   */
  @Override
  public int getPriority() {
    return priority;
  }
}
