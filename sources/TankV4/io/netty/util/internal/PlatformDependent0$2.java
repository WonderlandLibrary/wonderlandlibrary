package io.netty.util.internal;

import java.security.PrivilegedAction;

final class PlatformDependent0$2 implements PrivilegedAction {
   public ClassLoader run() {
      return Thread.currentThread().getContextClassLoader();
   }

   public Object run() {
      return this.run();
   }
}
