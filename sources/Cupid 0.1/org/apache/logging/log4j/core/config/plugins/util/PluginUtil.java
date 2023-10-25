package org.apache.logging.log4j.core.config.plugins.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

public final class PluginUtil {
  public static Map<String, PluginType<?>> collectPluginsByCategory(String category) {
    Objects.requireNonNull(category, "category");
    return collectPluginsByCategoryAndPackage(category, Collections.emptyList());
  }
  
  public static Map<String, PluginType<?>> collectPluginsByCategoryAndPackage(String category, List<String> packages) {
    Objects.requireNonNull(category, "category");
    Objects.requireNonNull(packages, "packages");
    PluginManager pluginManager = new PluginManager(category);
    pluginManager.collectPlugins(packages);
    return pluginManager.getPlugins();
  }
  
  public static <V> V instantiatePlugin(Class<V> pluginClass) {
    Objects.requireNonNull(pluginClass, "pluginClass");
    Method pluginFactoryMethod = findPluginFactoryMethod(pluginClass);
    try {
      V instance = (V)pluginFactoryMethod.invoke((Object)null, new Object[0]);
      return instance;
    } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException error) {
      String message = String.format("failed to instantiate plugin of type %s using the factory method %s", new Object[] { pluginClass, pluginFactoryMethod });
      throw new IllegalStateException(message, error);
    } 
  }
  
  public static Method findPluginFactoryMethod(Class<?> pluginClass) {
    Objects.requireNonNull(pluginClass, "pluginClass");
    for (Method method : pluginClass.getDeclaredMethods()) {
      boolean methodAnnotated = method.isAnnotationPresent((Class)PluginFactory.class);
      if (methodAnnotated) {
        boolean methodStatic = Modifier.isStatic(method.getModifiers());
        if (methodStatic)
          return method; 
      } 
    } 
    throw new IllegalStateException("no factory method found for class " + pluginClass);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */