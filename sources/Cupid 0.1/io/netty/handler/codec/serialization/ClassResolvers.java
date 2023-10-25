package io.netty.handler.codec.serialization;

import io.netty.util.internal.PlatformDependent;
import java.lang.ref.Reference;
import java.util.HashMap;

public final class ClassResolvers {
  public static ClassResolver cacheDisabled(ClassLoader classLoader) {
    return new ClassLoaderClassResolver(defaultClassLoader(classLoader));
  }
  
  public static ClassResolver weakCachingResolver(ClassLoader classLoader) {
    return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), new WeakReferenceMap<String, Class<?>>(new HashMap<String, Reference<Class<?>>>()));
  }
  
  public static ClassResolver softCachingResolver(ClassLoader classLoader) {
    return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), new SoftReferenceMap<String, Class<?>>(new HashMap<String, Reference<Class<?>>>()));
  }
  
  public static ClassResolver weakCachingConcurrentResolver(ClassLoader classLoader) {
    return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), new WeakReferenceMap<String, Class<?>>(PlatformDependent.newConcurrentHashMap()));
  }
  
  public static ClassResolver softCachingConcurrentResolver(ClassLoader classLoader) {
    return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), new SoftReferenceMap<String, Class<?>>(PlatformDependent.newConcurrentHashMap()));
  }
  
  static ClassLoader defaultClassLoader(ClassLoader classLoader) {
    if (classLoader != null)
      return classLoader; 
    ClassLoader contextClassLoader = PlatformDependent.getContextClassLoader();
    if (contextClassLoader != null)
      return contextClassLoader; 
    return PlatformDependent.getClassLoader(ClassResolvers.class);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\serialization\ClassResolvers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */