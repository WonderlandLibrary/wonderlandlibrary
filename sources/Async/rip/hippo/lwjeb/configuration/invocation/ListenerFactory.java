package rip.hippo.lwjeb.configuration.invocation;

import rip.hippo.lwjeb.configuration.config.impl.BusConfiguration;
import rip.hippo.lwjeb.listener.Listener;

import java.lang.reflect.Method;

/**
 * @author Hippo
 * @version 1.0.0, 3/17/21
 * @since 5.2.0
 * <p>
 * A Listener factory is a functional interface on which job is to create {@link Listener}s.
 * </p>
 */
@FunctionalInterface
public interface ListenerFactory {

  /**
   * Creates a listener.
   *
   * @param parent The parent method.
   * @param method The method to invoke.
   * @param topic  The event topic.
   * @param config The bus configuration.
   * @return The listener.
   */
  Listener create(Class<?> parent, Method method, Class<?> topic, BusConfiguration config);
}
