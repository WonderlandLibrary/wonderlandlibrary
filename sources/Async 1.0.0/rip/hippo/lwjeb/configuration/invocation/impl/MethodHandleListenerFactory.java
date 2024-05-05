package rip.hippo.lwjeb.configuration.invocation.impl;

import rip.hippo.lwjeb.configuration.config.impl.BusConfiguration;
import rip.hippo.lwjeb.configuration.invocation.ListenerFactory;
import rip.hippo.lwjeb.listener.Listener;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * @author Hippo
 * @version 1.0.0, 3/18/21
 * @since 5.2.0
 * <p>
 * A method handle listener factory creates listeners that are invoked via {@link MethodHandle}s.
 * </p>
 */
public final class MethodHandleListenerFactory implements ListenerFactory {

  /**
   * Creates a listener that is invoked with {@link MethodHandle}.
   *
   * @param parent The parent.
   * @param method The method.
   * @param topic  The topic.
   * @param config The bus configuration.
   * @return The listener.
   */
  @Override
  public Listener create(Class<?> parent, Method method, Class<?> topic, BusConfiguration config) {
    try {
      MethodType methodType = MethodType.methodType(void.class, topic);
      MethodHandle methodHandle = MethodHandles.lookup().findVirtual(parent, method.getName(), methodType);

      return (parentObject, topicObject) -> {
        try {
          methodHandle.invoke(parentObject, topicObject);
        } catch (Throwable t) {
          throw new RuntimeException(t);
        }
      };
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
