package io.netty.util;

import java.util.Arrays;

/** @deprecated */
@Deprecated
public class ResourceLeakException extends RuntimeException {
   private static final long serialVersionUID = 7186453858343358280L;
   private final StackTraceElement[] cachedStackTrace = this.getStackTrace();

   public ResourceLeakException() {
   }

   public ResourceLeakException(String var1) {
      super(var1);
   }

   public ResourceLeakException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ResourceLeakException(Throwable var1) {
      super(var1);
   }

   public int hashCode() {
      StackTraceElement[] var1 = this.cachedStackTrace;
      int var2 = 0;
      StackTraceElement[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StackTraceElement var6 = var3[var5];
         var2 = var2 * 31 + var6.hashCode();
      }

      return var2;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ResourceLeakException)) {
         return false;
      } else {
         return var1 == this ? true : Arrays.equals(this.cachedStackTrace, ((ResourceLeakException)var1).cachedStackTrace);
      }
   }
}
