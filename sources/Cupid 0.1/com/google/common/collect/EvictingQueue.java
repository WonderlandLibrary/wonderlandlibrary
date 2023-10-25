package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

@Beta
@GwtIncompatible("java.util.ArrayDeque")
public final class EvictingQueue<E> extends ForwardingQueue<E> implements Serializable {
  private final Queue<E> delegate;
  
  @VisibleForTesting
  final int maxSize;
  
  private static final long serialVersionUID = 0L;
  
  private EvictingQueue(int maxSize) {
    Preconditions.checkArgument((maxSize >= 0), "maxSize (%s) must >= 0", new Object[] { Integer.valueOf(maxSize) });
    this.delegate = new ArrayDeque<E>(maxSize);
    this.maxSize = maxSize;
  }
  
  public static <E> EvictingQueue<E> create(int maxSize) {
    return new EvictingQueue<E>(maxSize);
  }
  
  public int remainingCapacity() {
    return this.maxSize - size();
  }
  
  protected Queue<E> delegate() {
    return this.delegate;
  }
  
  public boolean offer(E e) {
    return add(e);
  }
  
  public boolean add(E e) {
    Preconditions.checkNotNull(e);
    if (this.maxSize == 0)
      return true; 
    if (size() == this.maxSize)
      this.delegate.remove(); 
    this.delegate.add(e);
    return true;
  }
  
  public boolean addAll(Collection<? extends E> collection) {
    return standardAddAll(collection);
  }
  
  public boolean contains(Object object) {
    return delegate().contains(Preconditions.checkNotNull(object));
  }
  
  public boolean remove(Object object) {
    return delegate().remove(Preconditions.checkNotNull(object));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\EvictingQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */