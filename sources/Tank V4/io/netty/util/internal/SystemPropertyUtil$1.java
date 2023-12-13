package io.netty.util.internal;

import java.security.PrivilegedAction;

final class SystemPropertyUtil$1 implements PrivilegedAction {
   final String val$key;

   SystemPropertyUtil$1(String var1) {
      this.val$key = var1;
   }

   public String run() {
      return System.getProperty(this.val$key);
   }

   public Object run() {
      return this.run();
   }
}
