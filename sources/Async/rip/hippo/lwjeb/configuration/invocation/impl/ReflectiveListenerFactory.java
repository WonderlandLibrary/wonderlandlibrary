package rip.hippo.lwjeb.configuration.invocation.impl;

import rip.hippo.lwjeb.configuration.config.impl.BusConfiguration;
import rip.hippo.lwjeb.configuration.invocation.ListenerFactory;
import rip.hippo.lwjeb.listener.Listener;

import java.lang.reflect.Method;

/**
 * @author Hippo
 * @version 1.0.0, 3/18/21
 * @since 5.2.0
 * <p>
 * A reflective listener factory makes a listener which invokes its method handler via reflection.
 * </p>
 */
public final class ReflectiveListenerFactory implements ListenerFactory {

  /**
   * Creates a reflective listener.
   *
   * @param parent The parent.
   * @param method The method.
   * @param topic  The topic.
   * @param config The bus configuration.
   * @return The listener.
   */
  @Override
  public Listener create(Class<?> parent, Method method, Class<?> topic, BusConfiguration config) {
    return (parentObject, topicObject) -> {
      try {
        method.invoke(parentObject, topicObject);
      } catch (ReflectiveOperationException e) {
        throw new RuntimeException(e);
      }
    };
  }
}
