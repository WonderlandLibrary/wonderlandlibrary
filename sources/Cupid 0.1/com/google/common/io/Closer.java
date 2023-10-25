package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import javax.annotation.Nullable;

@Beta
public final class Closer implements Closeable {
  private static final Suppressor SUPPRESSOR = SuppressingSuppressor.isAvailable() ? SuppressingSuppressor.INSTANCE : LoggingSuppressor.INSTANCE;
  
  @VisibleForTesting
  final Suppressor suppressor;
  
  public static Closer create() {
    return new Closer(SUPPRESSOR);
  }
  
  private final Deque<Closeable> stack = new ArrayDeque<Closeable>(4);
  
  private Throwable thrown;
  
  @VisibleForTesting
  Closer(Suppressor suppressor) {
    this.suppressor = (Suppressor)Preconditions.checkNotNull(suppressor);
  }
  
  public <C extends Closeable> C register(@Nullable C closeable) {
    if (closeable != null)
      this.stack.addFirst((Closeable)closeable); 
    return closeable;
  }
  
  public RuntimeException rethrow(Throwable e) throws IOException {
    Preconditions.checkNotNull(e);
    this.thrown = e;
    Throwables.propagateIfPossible(e, IOException.class);
    throw new RuntimeException(e);
  }
  
  public <X extends Exception> RuntimeException rethrow(Throwable e, Class<X> declaredType) throws IOException, X {
    Preconditions.checkNotNull(e);
    this.thrown = e;
    Throwables.propagateIfPossible(e, IOException.class);
    Throwables.propagateIfPossible(e, declaredType);
    throw new RuntimeException(e);
  }
  
  public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable e, Class<X1> declaredType1, Class<X2> declaredType2) throws IOException, X1, X2 {
    Preconditions.checkNotNull(e);
    this.thrown = e;
    Throwables.propagateIfPossible(e, IOException.class);
    Throwables.propagateIfPossible(e, declaredType1, declaredType2);
    throw new RuntimeException(e);
  }
  
  public void close() throws IOException {
    Throwable throwable = this.thrown;
    while (!this.stack.isEmpty()) {
      Closeable closeable = this.stack.removeFirst();
      try {
        closeable.close();
      } catch (Throwable e) {
        if (throwable == null) {
          throwable = e;
          continue;
        } 
        this.suppressor.suppress(closeable, throwable, e);
      } 
    } 
    if (this.thrown == null && throwable != null) {
      Throwables.propagateIfPossible(throwable, IOException.class);
      throw new AssertionError(throwable);
    } 
  }
  
  @VisibleForTesting
  static interface Suppressor {
    void suppress(Closeable param1Closeable, Throwable param1Throwable1, Throwable param1Throwable2);
  }
  
  @VisibleForTesting
  static final class LoggingSuppressor implements Suppressor {
    static final LoggingSuppressor INSTANCE = new LoggingSuppressor();
    
    public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
      Closeables.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, suppressed);
    }
  }
  
  @VisibleForTesting
  static final class SuppressingSuppressor implements Suppressor {
    static final SuppressingSuppressor INSTANCE = new SuppressingSuppressor();
    
    static boolean isAvailable() {
      return (addSuppressed != null);
    }
    
    static final Method addSuppressed = getAddSuppressed();
    
    private static Method getAddSuppressed() {
      try {
        return Throwable.class.getMethod("addSuppressed", new Class[] { Throwable.class });
      } catch (Throwable e) {
        return null;
      } 
    }
    
    public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
      if (thrown == suppressed)
        return; 
      try {
        addSuppressed.invoke(thrown, new Object[] { suppressed });
      } catch (Throwable e) {
        Closer.LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\Closer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */