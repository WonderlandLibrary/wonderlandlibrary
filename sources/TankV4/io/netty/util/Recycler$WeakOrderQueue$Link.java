package io.netty.util;

import java.util.concurrent.atomic.AtomicInteger;

final class Recycler$WeakOrderQueue$Link extends AtomicInteger {
   private final Recycler$DefaultHandle[] elements;
   private int readIndex;
   private Recycler$WeakOrderQueue$Link next;

   private Recycler$WeakOrderQueue$Link() {
      this.elements = new Recycler$DefaultHandle[16];
   }

   Recycler$WeakOrderQueue$Link(Object var1) {
      this();
   }

   static Recycler$WeakOrderQueue$Link access$802(Recycler$WeakOrderQueue$Link var0, Recycler$WeakOrderQueue$Link var1) {
      return var0.next = var1;
   }

   static Recycler$DefaultHandle[] access$900(Recycler$WeakOrderQueue$Link var0) {
      return var0.elements;
   }

   static int access$1000(Recycler$WeakOrderQueue$Link var0) {
      return var0.readIndex;
   }

   static Recycler$WeakOrderQueue$Link access$800(Recycler$WeakOrderQueue$Link var0) {
      return var0.next;
   }

   static int access$1002(Recycler$WeakOrderQueue$Link var0, int var1) {
      return var0.readIndex = var1;
   }
}
