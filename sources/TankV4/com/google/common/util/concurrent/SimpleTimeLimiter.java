package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ObjectArrays;
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

   public SimpleTimeLimiter(ExecutorService var1) {
      this.executor = (ExecutorService)Preconditions.checkNotNull(var1);
   }

   public SimpleTimeLimiter() {
      this(Executors.newCachedThreadPool());
   }

   public Object newProxy(Object var1, Class var2, long var3, TimeUnit var5) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var5);
      Preconditions.checkArgument(var3 > 0L, "bad timeout: %s", var3);
      Preconditions.checkArgument(var2.isInterface(), "interfaceType must be an interface type");
      Set var6 = findInterruptibleMethods(var2);
      InvocationHandler var7 = new InvocationHandler(this, var1, var3, var5, var6) {
         final Object val$target;
         final long val$timeoutDuration;
         final TimeUnit val$timeoutUnit;
         final Set val$interruptibleMethods;
         final SimpleTimeLimiter this$0;

         {
            this.this$0 = var1;
            this.val$target = var2;
            this.val$timeoutDuration = var3;
            this.val$timeoutUnit = var5;
            this.val$interruptibleMethods = var6;
         }

         public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
            Callable var4 = new Callable(this, var2, var3) {
               final Method val$method;
               final Object[] val$args;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.val$method = var2;
                  this.val$args = var3;
               }

               public Object call() throws Exception {
                  try {
                     return this.val$method.invoke(this.this$1.val$target, this.val$args);
                  } catch (InvocationTargetException var2) {
                     SimpleTimeLimiter.access$000(var2, false);
                     throw new AssertionError("can't get here");
                  }
               }
            };
            return this.this$0.callWithTimeout(var4, this.val$timeoutDuration, this.val$timeoutUnit, this.val$interruptibleMethods.contains(var2));
         }
      };
      return newProxy(var2, var7);
   }

   public Object callWithTimeout(Callable var1, long var2, TimeUnit var4, boolean var5) throws Exception {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var4);
      Preconditions.checkArgument(var2 > 0L, "timeout must be positive: %s", var2);
      Future var6 = this.executor.submit(var1);

      try {
         if (var5) {
            try {
               return var6.get(var2, var4);
            } catch (InterruptedException var8) {
               var6.cancel(true);
               throw var8;
            }
         } else {
            return Uninterruptibles.getUninterruptibly(var6, var2, var4);
         }
      } catch (ExecutionException var9) {
         throw throwCause(var9, true);
      } catch (TimeoutException var10) {
         var6.cancel(true);
         throw new UncheckedTimeoutException(var10);
      }
   }

   private static Exception throwCause(Exception var0, boolean var1) throws Exception {
      Throwable var2 = var0.getCause();
      if (var2 == null) {
         throw var0;
      } else {
         if (var1) {
            StackTraceElement[] var3 = (StackTraceElement[])ObjectArrays.concat(var2.getStackTrace(), var0.getStackTrace(), StackTraceElement.class);
            var2.setStackTrace(var3);
         }

         if (var2 instanceof Exception) {
            throw (Exception)var2;
         } else if (var2 instanceof Error) {
            throw (Error)var2;
         } else {
            throw var0;
         }
      }
   }

   private static Set findInterruptibleMethods(Class param0) {
      // $FF: Couldn't be decompiled
   }

   private static Object newProxy(Class var0, InvocationHandler var1) {
      Object var2 = Proxy.newProxyInstance(var0.getClassLoader(), new Class[]{var0}, var1);
      return var0.cast(var2);
   }

   static Exception access$000(Exception var0, boolean var1) throws Exception {
      return throwCause(var0, var1);
   }
}
