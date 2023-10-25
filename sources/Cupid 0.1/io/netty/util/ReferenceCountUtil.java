package io.netty.util;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class ReferenceCountUtil {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountUtil.class);
  
  public static <T> T retain(T msg) {
    if (msg instanceof ReferenceCounted)
      return (T)((ReferenceCounted)msg).retain(); 
    return msg;
  }
  
  public static <T> T retain(T msg, int increment) {
    if (msg instanceof ReferenceCounted)
      return (T)((ReferenceCounted)msg).retain(increment); 
    return msg;
  }
  
  public static boolean release(Object msg) {
    if (msg instanceof ReferenceCounted)
      return ((ReferenceCounted)msg).release(); 
    return false;
  }
  
  public static boolean release(Object msg, int decrement) {
    if (msg instanceof ReferenceCounted)
      return ((ReferenceCounted)msg).release(decrement); 
    return false;
  }
  
  public static void safeRelease(Object msg) {
    try {
      release(msg);
    } catch (Throwable t) {
      logger.warn("Failed to release a message: {}", msg, t);
    } 
  }
  
  public static void safeRelease(Object msg, int decrement) {
    try {
      release(msg, decrement);
    } catch (Throwable t) {
      if (logger.isWarnEnabled())
        logger.warn("Failed to release a message: {} (decrement: {})", new Object[] { msg, Integer.valueOf(decrement), t }); 
    } 
  }
  
  public static <T> T releaseLater(T msg) {
    return releaseLater(msg, 1);
  }
  
  public static <T> T releaseLater(T msg, int decrement) {
    if (msg instanceof ReferenceCounted)
      ThreadDeathWatcher.watch(Thread.currentThread(), new ReleasingTask((ReferenceCounted)msg, decrement)); 
    return msg;
  }
  
  private static final class ReleasingTask implements Runnable {
    private final ReferenceCounted obj;
    
    private final int decrement;
    
    ReleasingTask(ReferenceCounted obj, int decrement) {
      this.obj = obj;
      this.decrement = decrement;
    }
    
    public void run() {
      try {
        if (!this.obj.release(this.decrement)) {
          ReferenceCountUtil.logger.warn("Non-zero refCnt: {}", this);
        } else {
          ReferenceCountUtil.logger.debug("Released: {}", this);
        } 
      } catch (Exception ex) {
        ReferenceCountUtil.logger.warn("Failed to release an object: {}", this.obj, ex);
      } 
    }
    
    public String toString() {
      return StringUtil.simpleClassName(this.obj) + ".release(" + this.decrement + ") refCnt: " + this.obj.refCnt();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\ReferenceCountUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */