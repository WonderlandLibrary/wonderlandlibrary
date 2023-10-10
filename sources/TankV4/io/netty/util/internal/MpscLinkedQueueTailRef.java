package io.netty.util.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueTailRef extends MpscLinkedQueuePad1 {
   private static final long serialVersionUID = 8717072462993327429L;
   private static final AtomicReferenceFieldUpdater UPDATER;
   private transient volatile MpscLinkedQueueNode tailRef;

   protected final MpscLinkedQueueNode tailRef() {
      return this.tailRef;
   }

   protected final void setTailRef(MpscLinkedQueueNode var1) {
      this.tailRef = var1;
   }

   protected final MpscLinkedQueueNode getAndSetTailRef(MpscLinkedQueueNode var1) {
      return (MpscLinkedQueueNode)UPDATER.getAndSet(this, var1);
   }

   static {
      AtomicReferenceFieldUpdater var0 = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueTailRef.class, "tailRef");
      if (var0 == null) {
         var0 = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueTailRef.class, MpscLinkedQueueNode.class, "tailRef");
      }

      UPDATER = var0;
   }
}
