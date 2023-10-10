package io.netty.util.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class MpscLinkedQueueNode {
   private static final AtomicReferenceFieldUpdater nextUpdater;
   private volatile MpscLinkedQueueNode next;

   final MpscLinkedQueueNode next() {
      return this.next;
   }

   final void setNext(MpscLinkedQueueNode var1) {
      nextUpdater.lazySet(this, var1);
   }

   public abstract Object value();

   protected Object clearMaybe() {
      return this.value();
   }

   void unlink() {
      this.setNext((MpscLinkedQueueNode)null);
   }

   static {
      AtomicReferenceFieldUpdater var0 = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueNode.class, "next");
      if (var0 == null) {
         var0 = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueNode.class, MpscLinkedQueueNode.class, "next");
      }

      nextUpdater = var0;
   }
}
