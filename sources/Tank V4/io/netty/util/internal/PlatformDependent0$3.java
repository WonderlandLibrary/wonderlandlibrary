package io.netty.util.internal;

import java.security.PrivilegedAction;

final class PlatformDependent0$3 implements PrivilegedAction {
   public ClassLoader run() {
      return ClassLoader.getSystemClassLoader();
   }

   public Object run() {
      return this.run();
   }
}
