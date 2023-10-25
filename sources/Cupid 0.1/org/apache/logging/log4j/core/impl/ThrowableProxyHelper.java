package org.apache.logging.log4j.core.impl;

import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;

class ThrowableProxyHelper {
  static final class CacheEntry {
    private final ExtendedClassInfo element;
    
    private final ClassLoader loader;
    
    private CacheEntry(ExtendedClassInfo element, ClassLoader loader) {
      this.element = element;
      this.loader = loader;
    }
  }
  
  static ExtendedStackTraceElement[] toExtendedStackTrace(ThrowableProxy src, Stack<Class<?>> stack, Map<String, CacheEntry> map, StackTraceElement[] rootTrace, StackTraceElement[] stackTrace) {
    int stackLength;
    if (rootTrace != null) {
      int rootIndex = rootTrace.length - 1;
      int stackIndex = stackTrace.length - 1;
      while (rootIndex >= 0 && stackIndex >= 0 && rootTrace[rootIndex].equals(stackTrace[stackIndex])) {
        rootIndex--;
        stackIndex--;
      } 
      src.setCommonElementCount(stackTrace.length - 1 - stackIndex);
      stackLength = stackIndex + 1;
    } else {
      src.setCommonElementCount(0);
      stackLength = stackTrace.length;
    } 
    ExtendedStackTraceElement[] extStackTrace = new ExtendedStackTraceElement[stackLength];
    Class<?> clazz = stack.isEmpty() ? null : stack.peek();
    ClassLoader lastLoader = null;
    for (int i = stackLength - 1; i >= 0; i--) {
      ExtendedClassInfo extClassInfo;
      StackTraceElement stackTraceElement = stackTrace[i];
      String className = stackTraceElement.getClassName();
      if (clazz != null && className.equals(clazz.getName())) {
        CacheEntry entry = toCacheEntry(clazz, true);
        extClassInfo = entry.element;
        lastLoader = entry.loader;
        stack.pop();
        clazz = stack.isEmpty() ? null : stack.peek();
      } else {
        CacheEntry cacheEntry = map.get(className);
        if (cacheEntry != null) {
          CacheEntry entry = cacheEntry;
          extClassInfo = entry.element;
          if (entry.loader != null)
            lastLoader = entry.loader; 
        } else {
          CacheEntry entry = toCacheEntry(loadClass(lastLoader, className), false);
          extClassInfo = entry.element;
          map.put(className, entry);
          if (entry.loader != null)
            lastLoader = entry.loader; 
        } 
      } 
      extStackTrace[i] = new ExtendedStackTraceElement(stackTraceElement, extClassInfo);
    } 
    return extStackTrace;
  }
  
  static ThrowableProxy[] toSuppressedProxies(Throwable thrown, Set<Throwable> suppressedVisited) {
    try {
      Throwable[] suppressed = thrown.getSuppressed();
      if (suppressed == null || suppressed.length == 0)
        return ThrowableProxy.EMPTY_ARRAY; 
      List<ThrowableProxy> proxies = new ArrayList<>(suppressed.length);
      if (suppressedVisited == null)
        suppressedVisited = new HashSet<>(suppressed.length); 
      for (int i = 0; i < suppressed.length; i++) {
        Throwable candidate = suppressed[i];
        if (suppressedVisited.add(candidate))
          proxies.add(new ThrowableProxy(candidate, suppressedVisited)); 
      } 
      return proxies.<ThrowableProxy>toArray(new ThrowableProxy[proxies.size()]);
    } catch (Exception e) {
      StatusLogger.getLogger().error(e);
      return null;
    } 
  }
  
  private static CacheEntry toCacheEntry(Class<?> callerClass, boolean exact) {
    String location = "?";
    String version = "?";
    ClassLoader lastLoader = null;
    if (callerClass != null) {
      try {
        CodeSource source = callerClass.getProtectionDomain().getCodeSource();
        if (source != null) {
          URL locationURL = source.getLocation();
          if (locationURL != null) {
            String str = locationURL.toString().replace('\\', '/');
            int index = str.lastIndexOf("/");
            if (index >= 0 && index == str.length() - 1)
              index = str.lastIndexOf("/", index - 1); 
            location = str.substring(index + 1);
          } 
        } 
      } catch (Exception exception) {}
      Package pkg = callerClass.getPackage();
      if (pkg != null) {
        String ver = pkg.getImplementationVersion();
        if (ver != null)
          version = ver; 
      } 
      try {
        lastLoader = callerClass.getClassLoader();
      } catch (SecurityException e) {
        lastLoader = null;
      } 
    } 
    return new CacheEntry(new ExtendedClassInfo(exact, location, version), lastLoader);
  }
  
  private static Class<?> loadClass(ClassLoader lastLoader, String className) {
    Class<?> clazz;
    if (lastLoader != null)
      try {
        clazz = lastLoader.loadClass(className);
        if (clazz != null)
          return clazz; 
      } catch (Throwable throwable) {} 
    try {
      clazz = LoaderUtil.loadClass(className);
    } catch (ClassNotFoundException|NoClassDefFoundError e) {
      return loadClass(className);
    } catch (SecurityException e) {
      return null;
    } 
    return clazz;
  }
  
  private static Class<?> loadClass(String className) {
    try {
      return Loader.loadClass(className, ThrowableProxyHelper.class.getClassLoader());
    } catch (ClassNotFoundException|NoClassDefFoundError|SecurityException e) {
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ThrowableProxyHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */