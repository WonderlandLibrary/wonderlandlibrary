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

package rip.hippo.lwjeb.message.scan.impl;

import rip.hippo.lwjeb.annotation.Filter;
import rip.hippo.lwjeb.annotation.Handler;
import rip.hippo.lwjeb.annotation.Priority;
import rip.hippo.lwjeb.annotation.Wrapped;
import rip.hippo.lwjeb.bus.subscribe.SubscribeMessageBus;
import rip.hippo.lwjeb.configuration.config.impl.BusConfiguration;
import rip.hippo.lwjeb.configuration.config.impl.ExceptionHandlingConfiguration;
import rip.hippo.lwjeb.configuration.config.impl.ListenerFactoryConfiguration;
import rip.hippo.lwjeb.filter.MessageFilter;
import rip.hippo.lwjeb.message.handler.MessageHandler;
import rip.hippo.lwjeb.message.handler.impl.MethodBasedMessageHandler;
import rip.hippo.lwjeb.message.scan.MessageScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hippo
 * @version 5.0.2, 11/2/19
 * @since 5.0.0
 * <p>
 * This is a method based implementation of the message scanner, it just searches for methods.
 * </p>
 */
public final class MethodBasedMessageScanner<T> implements MessageScanner<T> {

  /**
   * @inheritDoc
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<MessageHandler<T>> scan(Object parent, SubscribeMessageBus<T> messageBus) {
    List<MessageHandler<T>> handlers = new ArrayList<>(parent.getClass().getMethods().length);

    BusConfiguration config = messageBus.getConfigurations().get(BusConfiguration.class);
    ExceptionHandlingConfiguration exceptionHandlingConfiguration = messageBus.getConfigurations().get(ExceptionHandlingConfiguration.class);
    ListenerFactoryConfiguration listenerFactoryConfiguration = messageBus.getConfigurations().get(ListenerFactoryConfiguration.class);

    for (Method method : parent.getClass().getDeclaredMethods()) {
      try {
        if (method.isAnnotationPresent(Handler.class) && method.getParameterCount() == 1) {
          Class<?> type = method.getParameters()[0].getType();
          Filter handlerFilter = method.getDeclaredAnnotation(Filter.class);
          Wrapped wrappedType = method.getDeclaredAnnotation(Wrapped.class);
          Priority priority = method.getDeclaredAnnotation(Priority.class);
          int priorityValue = priority == null ? 1 : priority.value();
          MessageFilter<T>[] filter;


          if (handlerFilter != null) {
            filter = new MessageFilter[handlerFilter.value().length];
            for (int i = 0; i < filter.length; i++) {

              Constructor<MessageFilter<T>> constructor = (Constructor<MessageFilter<T>>) handlerFilter.value()[i].getDeclaredConstructor();
              constructor.setAccessible(true);
              filter[i] = constructor.newInstance();
            }
          } else {
            filter = new MessageFilter[0];
          }
          if (wrappedType != null) {
            for (Class<?> acceptedType : wrappedType.value()) {

              handlers.add(new MethodBasedMessageHandler<T>(parent,
                  (Class<T>) acceptedType,
                  method,
                  filter,
                  config,
                  listenerFactoryConfiguration,
                  exceptionHandlingConfiguration,
                  true,
                  priorityValue));
            }
          } else {
            handlers.add(new MethodBasedMessageHandler<T>(parent,
                (Class<T>) type,
                method,
                filter,
                config,
                listenerFactoryConfiguration,
                exceptionHandlingConfiguration,
                false,
                priorityValue));
          }
        }
      } catch (ReflectiveOperationException e) {
        exceptionHandlingConfiguration.getExceptionHandler().handleException(e);
      }
    }
    return handlers;
  }
}
