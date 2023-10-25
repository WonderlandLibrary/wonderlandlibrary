package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;

public final class Callables {
  public static <T> Callable<T> returning(@Nullable final T value) {
    return new Callable<T>() {
        public T call() {
          return (T)value;
        }
      };
  }
  
  static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
    Preconditions.checkNotNull(nameSupplier);
    Preconditions.checkNotNull(callable);
    return new Callable<T>() {
        public T call() throws Exception {
          Thread currentThread = Thread.currentThread();
          String oldName = currentThread.getName();
          boolean restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
          try {
            return (T)callable.call();
          } finally {
            if (restoreName)
              Callables.trySetName(oldName, currentThread); 
          } 
        }
      };
  }
  
  static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
    Preconditions.checkNotNull(nameSupplier);
    Preconditions.checkNotNull(task);
    return new Runnable() {
        public void run() {
          Thread currentThread = Thread.currentThread();
          String oldName = currentThread.getName();
          boolean restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
          try {
            task.run();
          } finally {
            if (restoreName)
              Callables.trySetName(oldName, currentThread); 
          } 
        }
      };
  }
  
  private static boolean trySetName(String threadName, Thread currentThread) {
    try {
      currentThread.setName(threadName);
      return true;
    } catch (SecurityException e) {
      return false;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\Callables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */