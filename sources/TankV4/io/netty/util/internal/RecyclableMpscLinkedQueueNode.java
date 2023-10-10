package io.netty.util.internal;

import io.netty.util.Recycler;

public abstract class RecyclableMpscLinkedQueueNode extends MpscLinkedQueueNode {
   private final Recycler.Handle handle;

   protected RecyclableMpscLinkedQueueNode(Recycler.Handle var1) {
      if (var1 == null) {
         throw new NullPointerException("handle");
      } else {
         this.handle = var1;
      }
   }

   final void unlink() {
      super.unlink();
      this.recycle(this.handle);
   }

   protected abstract void recycle(Recycler.Handle var1);
}
