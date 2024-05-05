package rip.hippo.lwjeb.configuration.invocation.impl;

import rip.hippo.lwjeb.configuration.config.impl.BusConfiguration;
import rip.hippo.lwjeb.configuration.invocation.ListenerFactory;
import rip.hippo.lwjeb.listener.Listener;

import java.lang.invoke.*;
import java.lang.reflect.Method;

/**
 * @author Hippo
 * @version 1.0.0, 3/18/21
 * @since 5.2.0
 * <p>
 * This uses {@link LambdaMetafactory} to create a {@link CallSite} to invoke the method.
 * </p>
 */
public final class LambdaMetaFactoryListenerFactory implements ListenerFactory {

  /**
   * Creates a listener that is invoked with {@link LambdaMetafactory}.
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
      MethodHandles.Lookup lookup = MethodHandles.lookup();
      MethodType invokedType = MethodType.methodType(Listener.class);
      MethodHandle implMethod = lookup.unreflect(method);
      MethodType instantiatedMethodType = implMethod.type();
      MethodType samMethodType = instantiatedMethodType.changeParameterType(0, Object.class).changeParameterType(1, Object.class);

      CallSite callSite = LambdaMetafactory.metafactory(lookup, "invoke", invokedType, samMethodType, implMethod, instantiatedMethodType);
      return (Listener) callSite.getTarget().invoke();
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
}
