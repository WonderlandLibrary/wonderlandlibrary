package io.netty.util.internal;

import java.security.PrivilegedAction;

final class PlatformDependent0$1 implements PrivilegedAction {
   final Class val$clazz;

   PlatformDependent0$1(Class var1) {
      this.val$clazz = var1;
   }

   public ClassLoader run() {
      return this.val$clazz.getClassLoader();
   }

   public Object run() {
      return this.run();
   }
}
