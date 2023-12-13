package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;

public final class Signal extends Error {
   private static final long serialVersionUID = -221145131122459977L;
   private static final ConcurrentMap map = PlatformDependent.newConcurrentHashMap();
   private final UniqueName uname;

   public static Signal valueOf(String var0) {
      return new Signal(var0);
   }

   /** @deprecated */
   @Deprecated
   public Signal(String var1) {
      super(var1);
      this.uname = new UniqueName(map, var1, new Object[0]);
   }

   public void expect(Signal var1) {
      if (this != var1) {
         throw new IllegalStateException("unexpected signal: " + var1);
      }
   }

   public Throwable initCause(Throwable var1) {
      return this;
   }

   public Throwable fillInStackTrace() {
      return this;
   }

   public String toString() {
      return this.uname.name();
   }
}
