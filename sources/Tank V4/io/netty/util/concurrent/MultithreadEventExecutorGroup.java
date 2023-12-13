package io.netty.util.concurrent;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultithreadEventExecutorGroup extends AbstractEventExecutorGroup {
   private final EventExecutor[] children;
   private final AtomicInteger childIndex = new AtomicInteger();
   private final AtomicInteger terminatedChildren = new AtomicInteger();
   private final Promise terminationFuture;

   protected MultithreadEventExecutorGroup(int var1, ThreadFactory var2, Object... var3) {
      this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
      if (var1 <= 0) {
         throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", var1));
      } else {
         if (var2 == null) {
            var2 = this.newDefaultThreadFactory();
         }

         this.children = new SingleThreadEventExecutor[var1];

         int var6;
         for(int var4 = 0; var4 < var1; ++var4) {
            boolean var5 = false;

            try {
               this.children[var4] = this.newChild(var2, var3);
               var5 = true;
            } catch (Exception var13) {
               throw new IllegalStateException("failed to create a child event loop", var13);
            }

            if (!var5) {
               for(var6 = 0; var6 < var4; ++var6) {
                  this.children[var6].shutdownGracefully();
               }

               for(var6 = 0; var6 < var4; ++var6) {
                  EventExecutor var7 = this.children[var6];

                  try {
                     while(!var7.isTerminated()) {
                        var7.awaitTermination(2147483647L, TimeUnit.SECONDS);
                     }
                  } catch (InterruptedException var14) {
                     Thread.currentThread().interrupt();
                     break;
                  }
               }
            }
         }

         FutureListener var15 = new FutureListener(this) {
            final MultithreadEventExecutorGroup this$0;

            {
               this.this$0 = var1;
            }

            public void operationComplete(Future var1) throws Exception {
               if (MultithreadEventExecutorGroup.access$000(this.this$0).incrementAndGet() == MultithreadEventExecutorGroup.access$100(this.this$0).length) {
                  MultithreadEventExecutorGroup.access$200(this.this$0).setSuccess((Object)null);
               }

            }
         };
         EventExecutor[] var16 = this.children;
         var6 = var16.length;

         for(int var17 = 0; var17 < var6; ++var17) {
            EventExecutor var8 = var16[var17];
            var8.terminationFuture().addListener(var15);
         }

      }
   }

   protected ThreadFactory newDefaultThreadFactory() {
      return new DefaultThreadFactory(this.getClass());
   }

   public EventExecutor next() {
      return this.children[Math.abs(this.childIndex.getAndIncrement() % this.children.length)];
   }

   public Iterator iterator() {
      return this.children().iterator();
   }

   public final int executorCount() {
      return this.children.length;
   }

   protected Set children() {
      Set var1 = Collections.newSetFromMap(new LinkedHashMap());
      Collections.addAll(var1, this.children);
      return var1;
   }

   protected abstract EventExecutor newChild(ThreadFactory var1, Object... var2) throws Exception;

   public Future shutdownGracefully(long var1, long var3, TimeUnit var5) {
      EventExecutor[] var6 = this.children;
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         EventExecutor var9 = var6[var8];
         var9.shutdownGracefully(var1, var3, var5);
      }

      return this.terminationFuture();
   }

   public Future terminationFuture() {
      return this.terminationFuture;
   }

   /** @deprecated */
   @Deprecated
   public void shutdown() {
      EventExecutor[] var1 = this.children;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EventExecutor var4 = var1[var3];
         var4.shutdown();
      }

   }

   public boolean isShuttingDown() {
      EventExecutor[] var1 = this.children;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EventExecutor var4 = var1[var3];
         if (!var4.isShuttingDown()) {
            return false;
         }
      }

      return true;
   }

   public boolean isShutdown() {
      EventExecutor[] var1 = this.children;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EventExecutor var4 = var1[var3];
         if (!var4.isShutdown()) {
            return false;
         }
      }

      return true;
   }

   public boolean isTerminated() {
      EventExecutor[] var1 = this.children;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EventExecutor var4 = var1[var3];
         if (!var4.isTerminated()) {
            return false;
         }
      }

      return true;
   }

   public boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      long var4 = System.nanoTime() + var3.toNanos(var1);
      EventExecutor[] var6 = this.children;
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         EventExecutor var9 = var6[var8];

         long var10;
         do {
            var10 = var4 - System.nanoTime();
            if (var10 <= 0L) {
               return this.isTerminated();
            }
         } while(!var9.awaitTermination(var10, TimeUnit.NANOSECONDS));
      }

      return this.isTerminated();
   }

   static AtomicInteger access$000(MultithreadEventExecutorGroup var0) {
      return var0.terminatedChildren;
   }

   static EventExecutor[] access$100(MultithreadEventExecutorGroup var0) {
      return var0.children;
   }

   static Promise access$200(MultithreadEventExecutorGroup var0) {
      return var0.terminationFuture;
   }
}
