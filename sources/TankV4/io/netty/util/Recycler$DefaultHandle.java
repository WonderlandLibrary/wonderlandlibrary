package io.netty.util;

import java.util.Map;

final class Recycler$DefaultHandle implements Recycler.Handle {
   private int lastRecycledId;
   private int recycleId;
   private Recycler.Stack stack;
   private Object value;

   Recycler$DefaultHandle(Recycler.Stack var1) {
      this.stack = var1;
   }

   public void recycle() {
      Thread var1 = Thread.currentThread();
      if (var1 == this.stack.thread) {
         this.stack.push(this);
      } else {
         Map var2 = (Map)Recycler.access$300().get();
         Recycler$WeakOrderQueue var3 = (Recycler$WeakOrderQueue)var2.get(this.stack);
         if (var3 == null) {
            var2.put(this.stack, var3 = new Recycler$WeakOrderQueue(this.stack, var1));
         }

         var3.add(this);
      }
   }

   static Object access$102(Recycler$DefaultHandle var0, Object var1) {
      return var0.value = var1;
   }

   static Object access$100(Recycler$DefaultHandle var0) {
      return var0.value;
   }

   static Recycler.Stack access$200(Recycler$DefaultHandle var0) {
      return var0.stack;
   }

   static int access$702(Recycler$DefaultHandle var0, int var1) {
      return var0.lastRecycledId = var1;
   }

   static Recycler.Stack access$202(Recycler$DefaultHandle var0, Recycler.Stack var1) {
      return var0.stack = var1;
   }

   static int access$1300(Recycler$DefaultHandle var0) {
      return var0.recycleId;
   }

   static int access$1302(Recycler$DefaultHandle var0, int var1) {
      return var0.recycleId = var1;
   }

   static int access$700(Recycler$DefaultHandle var0) {
      return var0.lastRecycledId;
   }
}
