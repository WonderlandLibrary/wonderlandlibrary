package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

@Beta
public final class JdkFutureAdapters {
   public static ListenableFuture listenInPoolThread(Future var0) {
      return (ListenableFuture)(var0 instanceof ListenableFuture ? (ListenableFuture)var0 : new JdkFutureAdapters.ListenableFutureAdapter(var0));
   }

   public static ListenableFuture listenInPoolThread(Future var0, Executor var1) {
      Preconditions.checkNotNull(var1);
      return (ListenableFuture)(var0 instanceof ListenableFuture ? (ListenableFuture)var0 : new JdkFutureAdapters.ListenableFutureAdapter(var0, var1));
   }

   private JdkFutureAdapters() {
   }

   private static class ListenableFutureAdapter extends ForwardingFuture implements ListenableFuture {
      private static final ThreadFactory threadFactory = (new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("ListenableFutureAdapter-thread-%d").build();
      private static final Executor defaultAdapterExecutor;
      private final Executor adapterExecutor;
      private final ExecutionList executionList;
      private final AtomicBoolean hasListeners;
      private final Future delegate;

      ListenableFutureAdapter(Future var1) {
         this(var1, defaultAdapterExecutor);
      }

      ListenableFutureAdapter(Future var1, Executor var2) {
         this.executionList = new ExecutionList();
         this.hasListeners = new AtomicBoolean(false);
         this.delegate = (Future)Preconditions.checkNotNull(var1);
         this.adapterExecutor = (Executor)Preconditions.checkNotNull(var2);
      }

      protected Future delegate() {
         return this.delegate;
      }

      public void addListener(Runnable var1, Executor var2) {
         this.executionList.add(var1, var2);
         if (this.hasListeners.compareAndSet(false, true)) {
            if (this.delegate.isDone()) {
               this.executionList.execute();
               return;
            }

            this.adapterExecutor.execute(new Runnable(this) {
               final JdkFutureAdapters.ListenableFutureAdapter this$0;

               {
                  this.this$0 = var1;
               }

               public void run() {
                  try {
                     Uninterruptibles.getUninterruptibly(JdkFutureAdapters.ListenableFutureAdapter.access$000(this.this$0));
                  } catch (Error var2) {
                     throw var2;
                  } catch (Throwable var3) {
                  }

                  JdkFutureAdapters.ListenableFutureAdapter.access$100(this.this$0).execute();
               }
            });
         }

      }

      protected Object delegate() {
         return this.delegate();
      }

      static Future access$000(JdkFutureAdapters.ListenableFutureAdapter var0) {
         return var0.delegate;
      }

      static ExecutionList access$100(JdkFutureAdapters.ListenableFutureAdapter var0) {
         return var0.executionList;
      }

      static {
         defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
      }
   }
}
