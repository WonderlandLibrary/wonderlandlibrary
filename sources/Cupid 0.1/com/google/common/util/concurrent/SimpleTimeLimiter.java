package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public final class SimpleTimeLimiter implements TimeLimiter {
  private final ExecutorService executor;
  
  public SimpleTimeLimiter(ExecutorService executor) {
    this.executor = (ExecutorService)Preconditions.checkNotNull(executor);
  }
  
  public SimpleTimeLimiter() {
    this(Executors.newCachedThreadPool());
  }
  
  public <T> T newProxy(final T target, Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
    Preconditions.checkNotNull(target);
    Preconditions.checkNotNull(interfaceType);
    Preconditions.checkNotNull(timeoutUnit);
    Preconditions.checkArgument((timeoutDuration > 0L), "bad timeout: %s", new Object[] { Long.valueOf(timeoutDuration) });
    Preconditions.checkArgument(interfaceType.isInterface(), "interfaceType must be an interface type");
    final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);
    InvocationHandler handler = new InvocationHandler() {
        public Object invoke(Object obj, final Method method, final Object[] args) throws Throwable {
          Callable<Object> callable = new Callable() {
              public Object call() throws Exception {
                try {
                  return method.invoke(target, args);
                } catch (InvocationTargetException e) {
                  SimpleTimeLimiter.throwCause(e, false);
                  throw new AssertionError("can't get here");
                } 
              }
            };
          return SimpleTimeLimiter.this.callWithTimeout(callable, timeoutDuration, timeoutUnit, interruptibleMethods.contains(method));
        }
      };
    return newProxy(interfaceType, handler);
  }
  
  public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible) throws Exception {
    Preconditions.checkNotNull(callable);
    Preconditions.checkNotNull(timeoutUnit);
    Preconditions.checkArgument((timeoutDuration > 0L), "timeout must be positive: %s", new Object[] { Long.valueOf(timeoutDuration) });
    Future<T> future = this.executor.submit(callable);
    try {
      if (amInterruptible)
        try {
          return future.get(timeoutDuration, timeoutUnit);
        } catch (InterruptedException e) {
          future.cancel(true);
          throw e;
        }  
      return Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
    } catch (ExecutionException e) {
      throw throwCause(e, true);
    } catch (TimeoutException e) {
      future.cancel(true);
      throw new UncheckedTimeoutException(e);
    } 
  }
  
  private static Exception throwCause(Exception e, boolean combineStackTraces) throws Exception {
    Throwable cause = e.getCause();
    if (cause == null)
      throw e; 
    if (combineStackTraces) {
      StackTraceElement[] combined = (StackTraceElement[])ObjectArrays.concat((Object[])cause.getStackTrace(), (Object[])e.getStackTrace(), StackTraceElement.class);
      cause.setStackTrace(combined);
    } 
    if (cause instanceof Exception)
      throw (Exception)cause; 
    if (cause instanceof Error)
      throw (Error)cause; 
    throw e;
  }
  
  private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
    Set<Method> set = Sets.newHashSet();
    for (Method m : interfaceType.getMethods()) {
      if (declaresInterruptedEx(m))
        set.add(m); 
    } 
    return set;
  }
  
  private static boolean declaresInterruptedEx(Method method) {
    for (Class<?> exType : method.getExceptionTypes()) {
      if (exType == InterruptedException.class)
        return true; 
    } 
    return false;
  }
  
  private static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
    Object object = Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
    return interfaceType.cast(object);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\SimpleTimeLimiter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */