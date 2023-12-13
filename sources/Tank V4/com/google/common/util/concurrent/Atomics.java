package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.Nullable;

public final class Atomics {
   private Atomics() {
   }

   public static AtomicReference newReference() {
      return new AtomicReference();
   }

   public static AtomicReference newReference(@Nullable Object var0) {
      return new AtomicReference(var0);
   }

   public static AtomicReferenceArray newReferenceArray(int var0) {
      return new AtomicReferenceArray(var0);
   }

   public static AtomicReferenceArray newReferenceArray(Object[] var0) {
      return new AtomicReferenceArray(var0);
   }
}
