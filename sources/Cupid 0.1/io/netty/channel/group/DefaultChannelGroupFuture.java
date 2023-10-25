package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.Promise;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class DefaultChannelGroupFuture extends DefaultPromise<Void> implements ChannelGroupFuture {
  private final ChannelGroup group;
  
  private final Map<Channel, ChannelFuture> futures;
  
  private int successCount;
  
  private int failureCount;
  
  private final ChannelFutureListener childListener = new ChannelFutureListener() {
      public void operationComplete(ChannelFuture future) throws Exception {
        boolean callSetDone, success = future.isSuccess();
        synchronized (DefaultChannelGroupFuture.this) {
          if (success) {
            DefaultChannelGroupFuture.this.successCount++;
          } else {
            DefaultChannelGroupFuture.this.failureCount++;
          } 
          callSetDone = (DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount == DefaultChannelGroupFuture.this.futures.size());
          assert DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount <= DefaultChannelGroupFuture.this.futures.size();
        } 
        if (callSetDone)
          if (DefaultChannelGroupFuture.this.failureCount > 0) {
            List<Map.Entry<Channel, Throwable>> failed = new ArrayList<Map.Entry<Channel, Throwable>>(DefaultChannelGroupFuture.this.failureCount);
            for (ChannelFuture f : DefaultChannelGroupFuture.this.futures.values()) {
              if (!f.isSuccess())
                failed.add(new DefaultChannelGroupFuture.DefaultEntry<Channel, Throwable>(f.channel(), f.cause())); 
            } 
            DefaultChannelGroupFuture.this.setFailure0(new ChannelGroupException(failed));
          } else {
            DefaultChannelGroupFuture.this.setSuccess0();
          }  
      }
    };
  
  DefaultChannelGroupFuture(ChannelGroup group, Collection<ChannelFuture> futures, EventExecutor executor) {
    super(executor);
    if (group == null)
      throw new NullPointerException("group"); 
    if (futures == null)
      throw new NullPointerException("futures"); 
    this.group = group;
    Map<Channel, ChannelFuture> futureMap = new LinkedHashMap<Channel, ChannelFuture>();
    for (ChannelFuture f : futures)
      futureMap.put(f.channel(), f); 
    this.futures = Collections.unmodifiableMap(futureMap);
    for (ChannelFuture f : this.futures.values())
      f.addListener((GenericFutureListener)this.childListener); 
    if (this.futures.isEmpty())
      setSuccess0(); 
  }
  
  DefaultChannelGroupFuture(ChannelGroup group, Map<Channel, ChannelFuture> futures, EventExecutor executor) {
    super(executor);
    this.group = group;
    this.futures = Collections.unmodifiableMap(futures);
    for (ChannelFuture f : this.futures.values())
      f.addListener((GenericFutureListener)this.childListener); 
    if (this.futures.isEmpty())
      setSuccess0(); 
  }
  
  public ChannelGroup group() {
    return this.group;
  }
  
  public ChannelFuture find(Channel channel) {
    return this.futures.get(channel);
  }
  
  public Iterator<ChannelFuture> iterator() {
    return this.futures.values().iterator();
  }
  
  public synchronized boolean isPartialSuccess() {
    return (this.successCount != 0 && this.successCount != this.futures.size());
  }
  
  public synchronized boolean isPartialFailure() {
    return (this.failureCount != 0 && this.failureCount != this.futures.size());
  }
  
  public DefaultChannelGroupFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
    super.addListener(listener);
    return this;
  }
  
  public DefaultChannelGroupFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
    super.addListeners((GenericFutureListener[])listeners);
    return this;
  }
  
  public DefaultChannelGroupFuture removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
    super.removeListener(listener);
    return this;
  }
  
  public DefaultChannelGroupFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
    super.removeListeners((GenericFutureListener[])listeners);
    return this;
  }
  
  public DefaultChannelGroupFuture await() throws InterruptedException {
    super.await();
    return this;
  }
  
  public DefaultChannelGroupFuture awaitUninterruptibly() {
    super.awaitUninterruptibly();
    return this;
  }
  
  public DefaultChannelGroupFuture syncUninterruptibly() {
    super.syncUninterruptibly();
    return this;
  }
  
  public DefaultChannelGroupFuture sync() throws InterruptedException {
    super.sync();
    return this;
  }
  
  public ChannelGroupException cause() {
    return (ChannelGroupException)super.cause();
  }
  
  private void setSuccess0() {
    super.setSuccess(null);
  }
  
  private void setFailure0(ChannelGroupException cause) {
    super.setFailure((Throwable)cause);
  }
  
  public DefaultChannelGroupFuture setSuccess(Void result) {
    throw new IllegalStateException();
  }
  
  public boolean trySuccess(Void result) {
    throw new IllegalStateException();
  }
  
  public DefaultChannelGroupFuture setFailure(Throwable cause) {
    throw new IllegalStateException();
  }
  
  public boolean tryFailure(Throwable cause) {
    throw new IllegalStateException();
  }
  
  protected void checkDeadLock() {
    EventExecutor e = executor();
    if (e != null && e != ImmediateEventExecutor.INSTANCE && e.inEventLoop())
      throw new BlockingOperationException(); 
  }
  
  private static final class DefaultEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    
    private final V value;
    
    DefaultEntry(K key, V value) {
      this.key = key;
      this.value = value;
    }
    
    public K getKey() {
      return this.key;
    }
    
    public V getValue() {
      return this.value;
    }
    
    public V setValue(V value) {
      throw new UnsupportedOperationException("read-only");
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\group\DefaultChannelGroupFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */