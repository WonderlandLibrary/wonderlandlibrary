package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelPromise;
import io.netty.util.internal.PlatformDependent;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

final class SpdySession {
   private final AtomicInteger activeLocalStreams = new AtomicInteger();
   private final AtomicInteger activeRemoteStreams = new AtomicInteger();
   private final Map activeStreams = PlatformDependent.newConcurrentHashMap();
   private final AtomicInteger sendWindowSize;
   private final AtomicInteger receiveWindowSize;

   public SpdySession(int var1, int var2) {
      this.sendWindowSize = new AtomicInteger(var1);
      this.receiveWindowSize = new AtomicInteger(var2);
   }

   int numActiveStreams(boolean var1) {
      return var1 ? this.activeRemoteStreams.get() : this.activeLocalStreams.get();
   }

   boolean noActiveStreams() {
      return this.activeStreams.isEmpty();
   }

   boolean isActiveStream(int var1) {
      return this.activeStreams.containsKey(var1);
   }

   Set getActiveStreams() {
      TreeSet var1 = new TreeSet(new SpdySession.PriorityComparator(this));
      var1.addAll(this.activeStreams.keySet());
      return var1;
   }

   void acceptStream(int var1, byte var2, boolean var3, boolean var4, int var5, int var6, boolean var7) {
      if (!var3 || !var4) {
         SpdySession.StreamState var8 = (SpdySession.StreamState)this.activeStreams.put(var1, new SpdySession.StreamState(var2, var3, var4, var5, var6));
         if (var8 == null) {
            if (var7) {
               this.activeRemoteStreams.incrementAndGet();
            } else {
               this.activeLocalStreams.incrementAndGet();
            }
         }
      }

   }

   private SpdySession.StreamState removeActiveStream(int var1, boolean var2) {
      SpdySession.StreamState var3 = (SpdySession.StreamState)this.activeStreams.remove(var1);
      if (var3 != null) {
         if (var2) {
            this.activeRemoteStreams.decrementAndGet();
         } else {
            this.activeLocalStreams.decrementAndGet();
         }
      }

      return var3;
   }

   void removeStream(int var1, Throwable var2, boolean var3) {
      SpdySession.StreamState var4 = this.removeActiveStream(var1, var3);
      if (var4 != null) {
         var4.clearPendingWrites(var2);
      }

   }

   boolean isRemoteSideClosed(int var1) {
      SpdySession.StreamState var2 = (SpdySession.StreamState)this.activeStreams.get(var1);
      return var2 == null || var2.isRemoteSideClosed();
   }

   void closeRemoteSide(int var1, boolean var2) {
      SpdySession.StreamState var3 = (SpdySession.StreamState)this.activeStreams.get(var1);
      if (var3 != null) {
         var3.closeRemoteSide();
         if (var3.isLocalSideClosed()) {
            this.removeActiveStream(var1, var2);
         }
      }

   }

   boolean isLocalSideClosed(int var1) {
      SpdySession.StreamState var2 = (SpdySession.StreamState)this.activeStreams.get(var1);
      return var2 == null || var2.isLocalSideClosed();
   }

   void closeLocalSide(int var1, boolean var2) {
      SpdySession.StreamState var3 = (SpdySession.StreamState)this.activeStreams.get(var1);
      if (var3 != null) {
         var3.closeLocalSide();
         if (var3.isRemoteSideClosed()) {
            this.removeActiveStream(var1, var2);
         }
      }

   }

   boolean hasReceivedReply(int var1) {
      SpdySession.StreamState var2 = (SpdySession.StreamState)this.activeStreams.get(var1);
      return var2 != null && var2.hasReceivedReply();
   }

   void receivedReply(int var1) {
      SpdySession.StreamState var2 = (SpdySession.StreamState)this.activeStreams.get(var1);
      if (var2 != null) {
         var2.receivedReply();
      }

   }

   int getSendWindowSize(int var1) {
      if (var1 == 0) {
         return this.sendWindowSize.get();
      } else {
         SpdySession.StreamState var2 = (SpdySession.StreamState)this.activeStreams.get(var1);
         return var2 != null ? var2.getSendWindowSize() : -1;
      }
   }

   int updateSendWindowSize(int var1, int var2) {
      if (var1 == 0) {
         return this.sendWindowSize.addAndGet(var2);
      } else {
         SpdySession.StreamState var3 = (SpdySession.StreamState)this.activeStreams.get(var1);
         return var3 != null ? var3.updateSendWindowSize(var2) : -1;
      }
   }

   int updateReceiveWindowSize(int var1, int var2) {
      if (var1 == 0) {
         return this.receiveWindowSize.addAndGet(var2);
      } else {
         SpdySession.StreamState var3 = (SpdySession.StreamState)this.activeStreams.get(var1);
         if (var2 > 0) {
            var3.setReceiveWindowSizeLowerBound(0);
         }

         return var3 != null ? var3.updateReceiveWindowSize(var2) : -1;
      }
   }

   int getReceiveWindowSizeLowerBound(int var1) {
      if (var1 == 0) {
         return 0;
      } else {
         SpdySession.StreamState var2 = (SpdySession.StreamState)this.activeStreams.get(var1);
         return var2 != null ? var2.getReceiveWindowSizeLowerBound() : 0;
      }
   }

   void updateAllSendWindowSizes(int var1) {
      Iterator var2 = this.activeStreams.values().iterator();

      while(var2.hasNext()) {
         SpdySession.StreamState var3 = (SpdySession.StreamState)var2.next();
         var3.updateSendWindowSize(var1);
      }

   }

   void updateAllReceiveWindowSizes(int var1) {
      Iterator var2 = this.activeStreams.values().iterator();

      while(var2.hasNext()) {
         SpdySession.StreamState var3 = (SpdySession.StreamState)var2.next();
         var3.updateReceiveWindowSize(var1);
         if (var1 < 0) {
            var3.setReceiveWindowSizeLowerBound(var1);
         }
      }

   }

   boolean putPendingWrite(int var1, SpdySession.PendingWrite var2) {
      SpdySession.StreamState var3 = (SpdySession.StreamState)this.activeStreams.get(var1);
      return var3 != null && var3.putPendingWrite(var2);
   }

   SpdySession.PendingWrite getPendingWrite(int var1) {
      if (var1 == 0) {
         Iterator var6 = this.getActiveStreams().iterator();

         while(var6.hasNext()) {
            Integer var3 = (Integer)var6.next();
            SpdySession.StreamState var4 = (SpdySession.StreamState)this.activeStreams.get(var3);
            if (var4.getSendWindowSize() > 0) {
               SpdySession.PendingWrite var5 = var4.getPendingWrite();
               if (var5 != null) {
                  return var5;
               }
            }
         }

         return null;
      } else {
         SpdySession.StreamState var2 = (SpdySession.StreamState)this.activeStreams.get(var1);
         return var2 != null ? var2.getPendingWrite() : null;
      }
   }

   SpdySession.PendingWrite removePendingWrite(int var1) {
      SpdySession.StreamState var2 = (SpdySession.StreamState)this.activeStreams.get(var1);
      return var2 != null ? var2.removePendingWrite() : null;
   }

   static Map access$100(SpdySession var0) {
      return var0.activeStreams;
   }

   public static final class PendingWrite {
      final SpdyDataFrame spdyDataFrame;
      final ChannelPromise promise;

      PendingWrite(SpdyDataFrame var1, ChannelPromise var2) {
         this.spdyDataFrame = var1;
         this.promise = var2;
      }

      void fail(Throwable var1) {
         this.spdyDataFrame.release();
         this.promise.setFailure(var1);
      }
   }

   private final class PriorityComparator implements Comparator {
      final SpdySession this$0;

      private PriorityComparator(SpdySession var1) {
         this.this$0 = var1;
      }

      public int compare(Integer var1, Integer var2) {
         SpdySession.StreamState var3 = (SpdySession.StreamState)SpdySession.access$100(this.this$0).get(var1);
         SpdySession.StreamState var4 = (SpdySession.StreamState)SpdySession.access$100(this.this$0).get(var2);
         return var3.getPriority() - var4.getPriority();
      }

      public int compare(Object var1, Object var2) {
         return this.compare((Integer)var1, (Integer)var2);
      }

      PriorityComparator(SpdySession var1, Object var2) {
         this(var1);
      }
   }

   private static final class StreamState {
      private final byte priority;
      private boolean remoteSideClosed;
      private boolean localSideClosed;
      private boolean receivedReply;
      private final AtomicInteger sendWindowSize;
      private final AtomicInteger receiveWindowSize;
      private int receiveWindowSizeLowerBound;
      private final Queue pendingWriteQueue = new ConcurrentLinkedQueue();

      StreamState(byte var1, boolean var2, boolean var3, int var4, int var5) {
         this.priority = var1;
         this.remoteSideClosed = var2;
         this.localSideClosed = var3;
         this.sendWindowSize = new AtomicInteger(var4);
         this.receiveWindowSize = new AtomicInteger(var5);
      }

      byte getPriority() {
         return this.priority;
      }

      boolean isRemoteSideClosed() {
         return this.remoteSideClosed;
      }

      void closeRemoteSide() {
         this.remoteSideClosed = true;
      }

      boolean isLocalSideClosed() {
         return this.localSideClosed;
      }

      void closeLocalSide() {
         this.localSideClosed = true;
      }

      boolean hasReceivedReply() {
         return this.receivedReply;
      }

      void receivedReply() {
         this.receivedReply = true;
      }

      int getSendWindowSize() {
         return this.sendWindowSize.get();
      }

      int updateSendWindowSize(int var1) {
         return this.sendWindowSize.addAndGet(var1);
      }

      int updateReceiveWindowSize(int var1) {
         return this.receiveWindowSize.addAndGet(var1);
      }

      int getReceiveWindowSizeLowerBound() {
         return this.receiveWindowSizeLowerBound;
      }

      void setReceiveWindowSizeLowerBound(int var1) {
         this.receiveWindowSizeLowerBound = var1;
      }

      boolean putPendingWrite(SpdySession.PendingWrite var1) {
         return this.pendingWriteQueue.offer(var1);
      }

      SpdySession.PendingWrite getPendingWrite() {
         return (SpdySession.PendingWrite)this.pendingWriteQueue.peek();
      }

      SpdySession.PendingWrite removePendingWrite() {
         return (SpdySession.PendingWrite)this.pendingWriteQueue.poll();
      }

      void clearPendingWrites(Throwable var1) {
         while(true) {
            SpdySession.PendingWrite var2 = (SpdySession.PendingWrite)this.pendingWriteQueue.poll();
            if (var2 == null) {
               return;
            }

            var2.fail(var1);
         }
      }
   }
}
