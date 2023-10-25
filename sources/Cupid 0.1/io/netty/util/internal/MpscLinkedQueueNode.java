package io.netty.util.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class MpscLinkedQueueNode<T> {
  private static final AtomicReferenceFieldUpdater<MpscLinkedQueueNode, MpscLinkedQueueNode> nextUpdater;
  
  private volatile MpscLinkedQueueNode<T> next;
  
  static {
    AtomicReferenceFieldUpdater<MpscLinkedQueueNode, MpscLinkedQueueNode> u = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueNode.class, "next");
    if (u == null)
      u = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueNode.class, MpscLinkedQueueNode.class, "next"); 
    nextUpdater = u;
  }
  
  final MpscLinkedQueueNode<T> next() {
    return this.next;
  }
  
  final void setNext(MpscLinkedQueueNode<T> newNext) {
    nextUpdater.lazySet(this, newNext);
  }
  
  protected T clearMaybe() {
    return value();
  }
  
  void unlink() {
    setNext(null);
  }
  
  public abstract T value();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\MpscLinkedQueueNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */