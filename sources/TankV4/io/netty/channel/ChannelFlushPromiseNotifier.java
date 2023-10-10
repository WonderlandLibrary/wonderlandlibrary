package io.netty.channel;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public final class ChannelFlushPromiseNotifier {
   private long writeCounter;
   private final Queue flushCheckpoints;
   private final boolean tryNotify;

   public ChannelFlushPromiseNotifier(boolean var1) {
      this.flushCheckpoints = new ArrayDeque();
      this.tryNotify = var1;
   }

   public ChannelFlushPromiseNotifier() {
      this(false);
   }

   public ChannelFlushPromiseNotifier add(ChannelPromise var1, int var2) {
      if (var1 == null) {
         throw new NullPointerException("promise");
      } else if (var2 < 0) {
         throw new IllegalArgumentException("pendingDataSize must be >= 0 but was" + var2);
      } else {
         long var3 = this.writeCounter + (long)var2;
         if (var1 instanceof ChannelFlushPromiseNotifier.FlushCheckpoint) {
            ChannelFlushPromiseNotifier.FlushCheckpoint var5 = (ChannelFlushPromiseNotifier.FlushCheckpoint)var1;
            var5.flushCheckpoint(var3);
            this.flushCheckpoints.add(var5);
         } else {
            this.flushCheckpoints.add(new ChannelFlushPromiseNotifier.DefaultFlushCheckpoint(var3, var1));
         }

         return this;
      }
   }

   public ChannelFlushPromiseNotifier increaseWriteCounter(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("delta must be >= 0 but was" + var1);
      } else {
         this.writeCounter += var1;
         return this;
      }
   }

   public long writeCounter() {
      return this.writeCounter;
   }

   public ChannelFlushPromiseNotifier notifyFlushFutures() {
      this.notifyFlushFutures0((Throwable)null);
      return this;
   }

   public ChannelFlushPromiseNotifier notifyFlushFutures(Throwable var1) {
      this.notifyFlushFutures();

      while(true) {
         ChannelFlushPromiseNotifier.FlushCheckpoint var2 = (ChannelFlushPromiseNotifier.FlushCheckpoint)this.flushCheckpoints.poll();
         if (var2 == null) {
            return this;
         }

         if (this.tryNotify) {
            var2.promise().tryFailure(var1);
         } else {
            var2.promise().setFailure(var1);
         }
      }
   }

   public ChannelFlushPromiseNotifier notifyFlushFutures(Throwable var1, Throwable var2) {
      this.notifyFlushFutures0(var1);

      while(true) {
         ChannelFlushPromiseNotifier.FlushCheckpoint var3 = (ChannelFlushPromiseNotifier.FlushCheckpoint)this.flushCheckpoints.poll();
         if (var3 == null) {
            return this;
         }

         if (this.tryNotify) {
            var3.promise().tryFailure(var2);
         } else {
            var3.promise().setFailure(var2);
         }
      }
   }

   private void notifyFlushFutures0(Throwable var1) {
      if (this.flushCheckpoints.isEmpty()) {
         this.writeCounter = 0L;
      } else {
         long var2 = this.writeCounter;

         while(true) {
            ChannelFlushPromiseNotifier.FlushCheckpoint var4 = (ChannelFlushPromiseNotifier.FlushCheckpoint)this.flushCheckpoints.peek();
            if (var4 == null) {
               this.writeCounter = 0L;
               break;
            }

            if (var4.flushCheckpoint() > var2) {
               if (var2 > 0L && this.flushCheckpoints.size() == 1) {
                  this.writeCounter = 0L;
                  var4.flushCheckpoint(var4.flushCheckpoint() - var2);
               }
               break;
            }

            this.flushCheckpoints.remove();
            if (var1 == null) {
               if (this.tryNotify) {
                  var4.promise().trySuccess();
               } else {
                  var4.promise().setSuccess();
               }
            } else if (this.tryNotify) {
               var4.promise().tryFailure(var1);
            } else {
               var4.promise().setFailure(var1);
            }
         }

         long var8 = this.writeCounter;
         if (var8 >= 549755813888L) {
            this.writeCounter = 0L;
            Iterator var6 = this.flushCheckpoints.iterator();

            while(var6.hasNext()) {
               ChannelFlushPromiseNotifier.FlushCheckpoint var7 = (ChannelFlushPromiseNotifier.FlushCheckpoint)var6.next();
               var7.flushCheckpoint(var7.flushCheckpoint() - var8);
            }
         }

      }
   }

   private static class DefaultFlushCheckpoint implements ChannelFlushPromiseNotifier.FlushCheckpoint {
      private long checkpoint;
      private final ChannelPromise future;

      DefaultFlushCheckpoint(long var1, ChannelPromise var3) {
         this.checkpoint = var1;
         this.future = var3;
      }

      public long flushCheckpoint() {
         return this.checkpoint;
      }

      public void flushCheckpoint(long var1) {
         this.checkpoint = var1;
      }

      public ChannelPromise promise() {
         return this.future;
      }
   }

   interface FlushCheckpoint {
      long flushCheckpoint();

      void flushCheckpoint(long var1);

      ChannelPromise promise();
   }
}
