package io.netty.util.internal;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueHeadRef extends MpscLinkedQueuePad0 implements Serializable {
   private static final long serialVersionUID = 8467054865577874285L;
   private static final AtomicReferenceFieldUpdater UPDATER;
   private transient volatile MpscLinkedQueueNode headRef;

   protected final MpscLinkedQueueNode headRef() {
      return this.headRef;
   }

   protected final void setHeadRef(MpscLinkedQueueNode var1) {
      this.headRef = var1;
   }

   protected final void lazySetHeadRef(MpscLinkedQueueNode var1) {
      UPDATER.lazySet(this, var1);
   }

   static {
      AtomicReferenceFieldUpdater var0 = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueHeadRef.class, "headRef");
      if (var0 == null) {
         var0 = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueHeadRef.class, MpscLinkedQueueNode.class, "headRef");
      }

      UPDATER = var0;
   }
}
