package io.netty.handler.codec.serialization;

class ClassLoaderClassResolver implements ClassResolver {
   private final ClassLoader classLoader;

   ClassLoaderClassResolver(ClassLoader var1) {
      this.classLoader = var1;
   }

   public Class resolve(String var1) throws ClassNotFoundException {
      try {
         return this.classLoader.loadClass(var1);
      } catch (ClassNotFoundException var3) {
         return Class.forName(var1, false, this.classLoader);
      }
   }
}
