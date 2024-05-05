package rip.hippo.lwjeb.configuration.invocation.impl;

import rip.hippo.lwjeb.configuration.config.impl.BusConfiguration;
import rip.hippo.lwjeb.configuration.invocation.ListenerFactory;
import rip.hippo.lwjeb.listener.Listener;
import rip.hippo.lwjeb.listener.classfile.ListenerClassFile;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Hippo
 * @version 1.0.0, 3/17/21
 * @since 5.2.0
 * <p>
 * A direct listener factory will dynamically create a listener proxy class to invoke its message handlers directly.
 * </p>
 */
public final class DirectListenerFactory implements ListenerFactory {

  /**
   * Gets a unique method name from a method instance.
   *
   * @param method The method.
   * @return The unique name.
   */
  static String getUniqueMethodName(Method method) {
    StringBuilder parameters = new StringBuilder();
    for (Parameter parameter : method.getParameters()) {
      parameters.append(parameter.getType().getName().replace('.', '_'));
    }
    return method.getName() + parameters;
  }

  /**
   * Dynamically generates a listener to invoke {@code method}.
   *
   * @param parent The parent.
   * @param method The method.
   * @param topic  The topic.
   * @param config The bus configuration.
   * @return The dynamic listener.
   */
  @Override
  public Listener create(Class<?> parent, Method method, Class<?> topic, BusConfiguration config) {
    String name = "lwjeb/generated/" + parent.getName().replace('.', '/') + "/" + getUniqueMethodName(method);
    ListenerClassFile listenerClassFile = new ListenerClassFile(parent, topic, method, name);

    try {
      Class<?> compiledClass = config.getListenerClassLoader().createClass(name.replace('/', '.'), listenerClassFile.toByteArray());

      return (Listener) compiledClass.getConstructor()
          .newInstance();
    } catch (ReflectiveOperationException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
