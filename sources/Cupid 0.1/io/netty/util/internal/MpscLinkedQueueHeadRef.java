package io.netty.util.internal;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueHeadRef<E> extends MpscLinkedQueuePad0<E> implements Serializable {
  private static final long serialVersionUID = 8467054865577874285L;
  
  private static final AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode> UPDATER;
  
  private volatile transient MpscLinkedQueueNode<E> headRef;
  
  static {
    AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode> updater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueHeadRef.class, "headRef");
    if (updater == null)
      updater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueHeadRef.class, MpscLinkedQueueNode.class, "headRef"); 
    UPDATER = updater;
  }
  
  protected final MpscLinkedQueueNode<E> headRef() {
    return this.headRef;
  }
  
  protected final void setHeadRef(MpscLinkedQueueNode<E> headRef) {
    this.headRef = headRef;
  }
  
  protected final void lazySetHeadRef(MpscLinkedQueueNode<E> headRef) {
    UPDATER.lazySet(this, headRef);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\MpscLinkedQueueHeadRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */