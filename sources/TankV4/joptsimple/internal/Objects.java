package joptsimple.internal;

public final class Objects {
   private Objects() {
      throw new UnsupportedOperationException();
   }

   public static void ensureNotNull(Object var0) {
      if (var0 == null) {
         throw new NullPointerException();
      }
   }
}
