package org.apache.logging.log4j.core.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class Loader {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
  
  public static ClassLoader getClassLoader() {
    return getClassLoader(Loader.class, null);
  }
  
  public static ClassLoader getThreadContextClassLoader() {
    return LoaderUtil.getThreadContextClassLoader();
  }
  
  public static ClassLoader getClassLoader(Class<?> class1, Class<?> class2) {
    ClassLoader threadContextClassLoader = getThreadContextClassLoader();
    ClassLoader loader1 = (class1 == null) ? null : class1.getClassLoader();
    ClassLoader loader2 = (class2 == null) ? null : class2.getClassLoader();
    if (isChild(threadContextClassLoader, loader1))
      return isChild(threadContextClassLoader, loader2) ? threadContextClassLoader : loader2; 
    return isChild(loader1, loader2) ? loader1 : loader2;
  }
  
  public static URL getResource(String resource, ClassLoader defaultLoader) {
    try {
      ClassLoader classLoader = getThreadContextClassLoader();
      if (classLoader != null) {
        LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
        URL url = classLoader.getResource(resource);
        if (url != null)
          return url; 
      } 
      classLoader = Loader.class.getClassLoader();
      if (classLoader != null) {
        LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
        URL url = classLoader.getResource(resource);
        if (url != null)
          return url; 
      } 
      if (defaultLoader != null) {
        LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
        URL url = defaultLoader.getResource(resource);
        if (url != null)
          return url; 
      } 
    } catch (Throwable t) {
      LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
    } 
    LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
    return ClassLoader.getSystemResource(resource);
  }
  
  public static InputStream getResourceAsStream(String resource, ClassLoader defaultLoader) {
    try {
      ClassLoader classLoader = getThreadContextClassLoader();
      if (classLoader != null) {
        LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
        InputStream is = classLoader.getResourceAsStream(resource);
        if (is != null)
          return is; 
      } 
      classLoader = Loader.class.getClassLoader();
      if (classLoader != null) {
        LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
        InputStream is = classLoader.getResourceAsStream(resource);
        if (is != null)
          return is; 
      } 
      if (defaultLoader != null) {
        LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
        InputStream is = defaultLoader.getResourceAsStream(resource);
        if (is != null)
          return is; 
      } 
    } catch (Throwable t) {
      LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
    } 
    LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
    return ClassLoader.getSystemResourceAsStream(resource);
  }
  
  private static boolean isChild(ClassLoader loader1, ClassLoader loader2) {
    if (loader1 != null && loader2 != null) {
      ClassLoader parent = loader1.getParent();
      while (parent != null && parent != loader2)
        parent = parent.getParent(); 
      return (parent != null);
    } 
    return (loader1 != null);
  }
  
  public static Class<?> initializeClass(String className, ClassLoader loader) throws ClassNotFoundException {
    return Class.forName(className, true, loader);
  }
  
  public static Class<?> loadClass(String className, ClassLoader loader) throws ClassNotFoundException {
    return (loader != null) ? loader.loadClass(className) : null;
  }
  
  public static Class<?> loadSystemClass(String className) throws ClassNotFoundException {
    try {
      return Class.forName(className, true, ClassLoader.getSystemClassLoader());
    } catch (Throwable t) {
      LOGGER.trace("Couldn't use SystemClassLoader. Trying Class.forName({}).", className, t);
      return Class.forName(className);
    } 
  }
  
  public static <T> T newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClassLoader());
      return (T)LoaderUtil.newInstanceOf(className);
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    } 
  }
  
  public static <T> T newCheckedInstanceOf(String className, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClassLoader());
      return (T)LoaderUtil.newCheckedInstanceOf(className, clazz);
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    } 
  }
  
  public static <T> T newCheckedInstanceOfProperty(String propertyName, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    String className = PropertiesUtil.getProperties().getStringProperty(propertyName);
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClassLoader());
      return (T)LoaderUtil.newCheckedInstanceOfProperty(propertyName, clazz);
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    } 
  }
  
  public static boolean isClassAvailable(String className) {
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClassLoader());
      return LoaderUtil.isClassAvailable(className);
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    } 
  }
  
  public static boolean isJansiAvailable() {
    return isClassAvailable("org.fusesource.jansi.AnsiRenderer");
  }
  
  public static Class<?> loadClass(String className) throws ClassNotFoundException {
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClassLoader());
      return LoaderUtil.loadClass(className);
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\Loader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */