package io.netty.util.internal;

import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;

public final class PendingWrite {
  private static final Recycler<PendingWrite> RECYCLER = new Recycler<PendingWrite>() {
      protected PendingWrite newObject(Recycler.Handle handle) {
        return new PendingWrite(handle);
      }
    };
  
  private final Recycler.Handle handle;
  
  private Object msg;
  
  private Promise<Void> promise;
  
  public static PendingWrite newInstance(Object msg, Promise<Void> promise) {
    PendingWrite pending = (PendingWrite)RECYCLER.get();
    pending.msg = msg;
    pending.promise = promise;
    return pending;
  }
  
  private PendingWrite(Recycler.Handle handle) {
    this.handle = handle;
  }
  
  public boolean recycle() {
    this.msg = null;
    this.promise = null;
    return RECYCLER.recycle(this, this.handle);
  }
  
  public boolean failAndRecycle(Throwable cause) {
    ReferenceCountUtil.release(this.msg);
    if (this.promise != null)
      this.promise.setFailure(cause); 
    return recycle();
  }
  
  public boolean successAndRecycle() {
    if (this.promise != null)
      this.promise.setSuccess(null); 
    return recycle();
  }
  
  public Object msg() {
    return this.msg;
  }
  
  public Promise<Void> promise() {
    return this.promise;
  }
  
  public Promise<Void> recycleAndGet() {
    Promise<Void> promise = this.promise;
    recycle();
    return promise;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\PendingWrite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */