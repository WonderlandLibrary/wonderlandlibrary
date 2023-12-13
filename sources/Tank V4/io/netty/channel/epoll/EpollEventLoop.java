package io.netty.channel.epoll;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

final class EpollEventLoop extends SingleThreadEventLoop {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(EpollEventLoop.class);
   private static final AtomicIntegerFieldUpdater WAKEN_UP_UPDATER;
   private final int epollFd;
   private final int eventFd;
   private final IntObjectMap ids = new IntObjectHashMap();
   private final long[] events;
   private int id;
   private boolean overflown;
   private volatile int wakenUp;
   private volatile int ioRatio = 50;
   static final boolean $assertionsDisabled = !EpollEventLoop.class.desiredAssertionStatus();

   EpollEventLoop(EventLoopGroup var1, ThreadFactory var2, int var3) {
      super(var1, var2, false);
      this.events = new long[var3];
      boolean var4 = false;
      boolean var5 = true;
      boolean var6 = true;
      int var12;
      this.epollFd = var12 = Native.epollCreate();
      int var13;
      this.eventFd = var13 = Native.eventFd();
      Native.epollCtlAdd(var12, var13, 1, 0);
      var4 = true;
      if (!var4) {
         if (var12 != -1) {
            try {
               Native.close(var12);
            } catch (Exception var11) {
            }
         }

         if (var13 != -1) {
            try {
               Native.close(var13);
            } catch (Exception var10) {
            }
         }
      }

   }

   private int nextId() {
      int var1 = this.id;
      if (var1 == Integer.MAX_VALUE) {
         this.overflown = true;
         var1 = 0;
      }

      if (this.overflown) {
         do {
            ++var1;
         } while(this.ids.containsKey(var1));

         this.id = var1;
      } else {
         ++var1;
         this.id = var1;
      }

      return var1;
   }

   protected void wakeup(boolean var1) {
      if (!var1 && WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
         Native.eventFdWrite(this.eventFd, 1L);
      }

   }

   void add(AbstractEpollChannel var1) {
      if (!$assertionsDisabled && !this.inEventLoop()) {
         throw new AssertionError();
      } else {
         int var2 = this.nextId();
         Native.epollCtlAdd(this.epollFd, var1.fd, var1.flags, var2);
         var1.id = var2;
         this.ids.put(var2, var1);
      }
   }

   void modify(AbstractEpollChannel var1) {
      if (!$assertionsDisabled && !this.inEventLoop()) {
         throw new AssertionError();
      } else {
         Native.epollCtlMod(this.epollFd, var1.fd, var1.flags, var1.id);
      }
   }

   void remove(AbstractEpollChannel var1) {
      if (!$assertionsDisabled && !this.inEventLoop()) {
         throw new AssertionError();
      } else {
         if (this.ids.remove(var1.id) != null && var1.isOpen()) {
            Native.epollCtlDel(this.epollFd, var1.fd);
         }

      }
   }

   protected Queue newTaskQueue() {
      return PlatformDependent.newMpscQueue();
   }

   public int getIoRatio() {
      return this.ioRatio;
   }

   public void setIoRatio(int var1) {
      if (var1 > 0 && var1 <= 100) {
         this.ioRatio = var1;
      } else {
         throw new IllegalArgumentException("ioRatio: " + var1 + " (expected: 0 < ioRatio <= 100)");
      }
   }

   private int epollWait(boolean var1) {
      int var2 = 0;
      long var3 = System.nanoTime();
      long var5 = var3 + this.delayNanos(var3);

      while(true) {
         long var7 = (var5 - var3 + 500000L) / 1000000L;
         int var9;
         if (var7 <= 0L) {
            if (var2 == 0) {
               var9 = Native.epollWait(this.epollFd, this.events, 0);
               if (var9 > 0) {
                  return var9;
               }
            }

            return 0;
         }

         var9 = Native.epollWait(this.epollFd, this.events, (int)var7);
         ++var2;
         if (var9 != 0 || var1 || this.wakenUp == 1 || this.hasTasks() || this.hasScheduledTasks()) {
            return var9;
         }

         var3 = System.nanoTime();
      }
   }

   protected void run() {
      while(true) {
         boolean var1 = WAKEN_UP_UPDATER.getAndSet(this, 0) == 1;

         try {
            int var2;
            if (this.hasTasks()) {
               var2 = Native.epollWait(this.epollFd, this.events, 0);
            } else {
               var2 = this.epollWait(var1);
               if (this.wakenUp == 1) {
                  Native.eventFdWrite(this.eventFd, 1L);
               }
            }

            int var3 = this.ioRatio;
            if (var3 == 100) {
               if (var2 > 0) {
                  this.processReady(this.events, var2);
               }

               this.runAllTasks();
            } else {
               long var4 = System.nanoTime();
               if (var2 > 0) {
                  this.processReady(this.events, var2);
               }

               long var6 = System.nanoTime() - var4;
               this.runAllTasks(var6 * (long)(100 - var3) / (long)var3);
            }

            if (this.isShuttingDown()) {
               this.closeAll();
               if (this.confirmShutdown()) {
                  return;
               }
            }
         } catch (Throwable var9) {
            logger.warn("Unexpected exception in the selector loop.", var9);

            try {
               Thread.sleep(1000L);
            } catch (InterruptedException var8) {
            }
         }
      }
   }

   private void closeAll() {
      Native.epollWait(this.epollFd, this.events, 0);
      ArrayList var1 = new ArrayList(this.ids.size());
      Iterator var2 = this.ids.entries().iterator();

      while(var2.hasNext()) {
         IntObjectMap.Entry var3 = (IntObjectMap.Entry)var2.next();
         var1.add(var3.value());
      }

      var2 = var1.iterator();

      while(var2.hasNext()) {
         AbstractEpollChannel var4 = (AbstractEpollChannel)var2.next();
         var4.unsafe().close(var4.unsafe().voidPromise());
      }

   }

   private void processReady(long[] var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         long var4 = var1[var3];
         int var6 = (int)(var4 >> 32);
         if (var6 == 0) {
            Native.eventFdRead(this.eventFd);
         } else {
            boolean var7 = (var4 & 1L) != 0L;
            boolean var8 = (var4 & 2L) != 0L;
            boolean var9 = (var4 & 8L) != 0L;
            AbstractEpollChannel var10 = (AbstractEpollChannel)this.ids.get(var6);
            if (var10 != null) {
               AbstractEpollChannel.AbstractEpollUnsafe var11 = (AbstractEpollChannel.AbstractEpollUnsafe)var10.unsafe();
               if (var8 && var10.isOpen()) {
                  var11.epollOutReady();
               }

               if (var7 && var10.isOpen()) {
                  var11.epollInReady();
               }

               if (var9 && var10.isOpen()) {
                  var11.epollRdHupReady();
               }
            }
         }
      }

   }

   protected void cleanup() {
      try {
         Native.close(this.epollFd);
      } catch (IOException var3) {
         logger.warn("Failed to close the epoll fd.", (Throwable)var3);
      }

      try {
         Native.close(this.eventFd);
      } catch (IOException var2) {
         logger.warn("Failed to close the event fd.", (Throwable)var2);
      }

   }

   static {
      AtomicIntegerFieldUpdater var0 = PlatformDependent.newAtomicIntegerFieldUpdater(EpollEventLoop.class, "wakenUp");
      if (var0 == null) {
         var0 = AtomicIntegerFieldUpdater.newUpdater(EpollEventLoop.class, "wakenUp");
      }

      WAKEN_UP_UPDATER = var0;
   }
}
