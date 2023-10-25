package io.netty.util.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueTailRef<E> extends MpscLinkedQueuePad1<E> {
  private static final long serialVersionUID = 8717072462993327429L;
  
  private static final AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode> UPDATER;
  
  private volatile transient MpscLinkedQueueNode<E> tailRef;
  
  static {
    AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode> updater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueTailRef.class, "tailRef");
    if (updater == null)
      updater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueTailRef.class, MpscLinkedQueueNode.class, "tailRef"); 
    UPDATER = updater;
  }
  
  protected final MpscLinkedQueueNode<E> tailRef() {
    return this.tailRef;
  }
  
  protected final void setTailRef(MpscLinkedQueueNode<E> tailRef) {
    this.tailRef = tailRef;
  }
  
  protected final MpscLinkedQueueNode<E> getAndSetTailRef(MpscLinkedQueueNode<E> tailRef) {
    return UPDATER.getAndSet(this, tailRef);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\MpscLinkedQueueTailRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */