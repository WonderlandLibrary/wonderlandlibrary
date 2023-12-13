package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.lang.ref.WeakReference;

@GwtCompatible(
   emulated = true
)
final class Platform {
   private Platform() {
   }

   static long systemNanoTime() {
      return System.nanoTime();
   }

   static CharMatcher precomputeCharMatcher(CharMatcher var0) {
      return var0.precomputedInternal();
   }

   static Optional getEnumIfPresent(Class var0, String var1) {
      WeakReference var2 = (WeakReference)Enums.getEnumConstants(var0).get(var1);
      return var2 == null ? Optional.absent() : Optional.of(var0.cast(var2.get()));
   }
}
