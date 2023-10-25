package io.netty.util.internal;

import io.netty.util.Recycler;

public abstract class RecyclableMpscLinkedQueueNode<T> extends MpscLinkedQueueNode<T> {
  private final Recycler.Handle handle;
  
  protected RecyclableMpscLinkedQueueNode(Recycler.Handle handle) {
    if (handle == null)
      throw new NullPointerException("handle"); 
    this.handle = handle;
  }
  
  final void unlink() {
    super.unlink();
    recycle(this.handle);
  }
  
  protected abstract void recycle(Recycler.Handle paramHandle);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\RecyclableMpscLinkedQueueNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */