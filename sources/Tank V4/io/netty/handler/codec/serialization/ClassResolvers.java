package io.netty.handler.codec.serialization;

import io.netty.util.internal.PlatformDependent;
import java.util.HashMap;

public final class ClassResolvers {
   public static ClassResolver cacheDisabled(ClassLoader var0) {
      return new ClassLoaderClassResolver(defaultClassLoader(var0));
   }

   public static ClassResolver weakCachingResolver(ClassLoader var0) {
      return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(var0)), new WeakReferenceMap(new HashMap()));
   }

   public static ClassResolver softCachingResolver(ClassLoader var0) {
      return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(var0)), new SoftReferenceMap(new HashMap()));
   }

   public static ClassResolver weakCachingConcurrentResolver(ClassLoader var0) {
      return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(var0)), new WeakReferenceMap(PlatformDependent.newConcurrentHashMap()));
   }

   public static ClassResolver softCachingConcurrentResolver(ClassLoader var0) {
      return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(var0)), new SoftReferenceMap(PlatformDependent.newConcurrentHashMap()));
   }

   static ClassLoader defaultClassLoader(ClassLoader var0) {
      if (var0 != null) {
         return var0;
      } else {
         ClassLoader var1 = Thread.currentThread().getContextClassLoader();
         return var1 != null ? var1 : ClassResolvers.class.getClassLoader();
      }
   }

   private ClassResolvers() {
   }
}
