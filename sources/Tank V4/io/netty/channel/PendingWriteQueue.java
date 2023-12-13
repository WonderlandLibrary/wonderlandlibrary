package io.netty.channel;

import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class PendingWriteQueue {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PendingWriteQueue.class);
   private final ChannelHandlerContext ctx;
   private final ChannelOutboundBuffer buffer;
   private final MessageSizeEstimator.Handle estimatorHandle;
   private PendingWriteQueue.PendingWrite head;
   private PendingWriteQueue.PendingWrite tail;
   private int size;
   static final boolean $assertionsDisabled = !PendingWriteQueue.class.desiredAssertionStatus();

   public PendingWriteQueue(ChannelHandlerContext var1) {
      if (var1 == null) {
         throw new NullPointerException("ctx");
      } else {
         this.ctx = var1;
         this.buffer = var1.channel().unsafe().outboundBuffer();
         this.estimatorHandle = var1.channel().config().getMessageSizeEstimator().newHandle();
      }
   }

   public boolean isEmpty() {
      if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
         throw new AssertionError();
      } else {
         return this.head == null;
      }
   }

   public int size() {
      if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
         throw new AssertionError();
      } else {
         return this.size;
      }
   }

   public void add(Object var1, ChannelPromise var2) {
      if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
         throw new AssertionError();
      } else if (var1 == null) {
         throw new NullPointerException("msg");
      } else if (var2 == null) {
         throw new NullPointerException("promise");
      } else {
         int var3 = this.estimatorHandle.size(var1);
         if (var3 < 0) {
            var3 = 0;
         }

         PendingWriteQueue.PendingWrite var4 = PendingWriteQueue.PendingWrite.newInstance(var1, var3, var2);
         PendingWriteQueue.PendingWrite var5 = this.tail;
         if (var5 == null) {
            this.tail = this.head = var4;
         } else {
            PendingWriteQueue.PendingWrite.access$002(var5, var4);
            this.tail = var4;
         }

         ++this.size;
         this.buffer.incrementPendingOutboundBytes(PendingWriteQueue.PendingWrite.access$100(var4));
      }
   }

   public void removeAndFailAll(Throwable var1) {
      if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
         throw new AssertionError();
      } else if (var1 == null) {
         throw new NullPointerException("cause");
      } else {
         PendingWriteQueue.PendingWrite var3;
         for(PendingWriteQueue.PendingWrite var2 = this.head; var2 != null; var2 = var3) {
            var3 = PendingWriteQueue.PendingWrite.access$000(var2);
            ReferenceCountUtil.safeRelease(PendingWriteQueue.PendingWrite.access$200(var2));
            ChannelPromise var4 = PendingWriteQueue.PendingWrite.access$300(var2);
            this.recycle(var2);
            safeFail(var4, var1);
         }

         this.assertEmpty();
      }
   }

   public void removeAndFail(Throwable var1) {
      if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
         throw new AssertionError();
      } else if (var1 == null) {
         throw new NullPointerException("cause");
      } else {
         PendingWriteQueue.PendingWrite var2 = this.head;
         if (var2 != null) {
            ReferenceCountUtil.safeRelease(PendingWriteQueue.PendingWrite.access$200(var2));
            ChannelPromise var3 = PendingWriteQueue.PendingWrite.access$300(var2);
            safeFail(var3, var1);
            this.recycle(var2);
         }
      }
   }

   public ChannelFuture removeAndWriteAll() {
      if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
         throw new AssertionError();
      } else {
         PendingWriteQueue.PendingWrite var1 = this.head;
         if (var1 == null) {
            return null;
         } else if (this.size == 1) {
            return this.removeAndWrite();
         } else {
            ChannelPromise var2 = this.ctx.newPromise();

            PendingWriteQueue.PendingWrite var4;
            for(ChannelPromiseAggregator var3 = new ChannelPromiseAggregator(var2); var1 != null; var1 = var4) {
               var4 = PendingWriteQueue.PendingWrite.access$000(var1);
               Object var5 = PendingWriteQueue.PendingWrite.access$200(var1);
               ChannelPromise var6 = PendingWriteQueue.PendingWrite.access$300(var1);
               this.recycle(var1);
               this.ctx.write(var5, var6);
               var3.add(var6);
            }

            this.assertEmpty();
            return var2;
         }
      }
   }

   private void assertEmpty() {
      if (!$assertionsDisabled && (this.tail != null || this.head != null || this.size != 0)) {
         throw new AssertionError();
      }
   }

   public ChannelFuture removeAndWrite() {
      if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
         throw new AssertionError();
      } else {
         PendingWriteQueue.PendingWrite var1 = this.head;
         if (var1 == null) {
            return null;
         } else {
            Object var2 = PendingWriteQueue.PendingWrite.access$200(var1);
            ChannelPromise var3 = PendingWriteQueue.PendingWrite.access$300(var1);
            this.recycle(var1);
            return this.ctx.write(var2, var3);
         }
      }
   }

   public ChannelPromise remove() {
      if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
         throw new AssertionError();
      } else {
         PendingWriteQueue.PendingWrite var1 = this.head;
         if (var1 == null) {
            return null;
         } else {
            ChannelPromise var2 = PendingWriteQueue.PendingWrite.access$300(var1);
            ReferenceCountUtil.safeRelease(PendingWriteQueue.PendingWrite.access$200(var1));
            this.recycle(var1);
            return var2;
         }
      }
   }

   public Object current() {
      if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
         throw new AssertionError();
      } else {
         PendingWriteQueue.PendingWrite var1 = this.head;
         return var1 == null ? null : PendingWriteQueue.PendingWrite.access$200(var1);
      }
   }

   private void recycle(PendingWriteQueue.PendingWrite var1) {
      PendingWriteQueue.PendingWrite var2 = PendingWriteQueue.PendingWrite.access$000(var1);
      this.buffer.decrementPendingOutboundBytes(PendingWriteQueue.PendingWrite.access$100(var1));
      PendingWriteQueue.PendingWrite.access$400(var1);
      --this.size;
      if (var2 == null) {
         this.head = this.tail = null;
         if (!$assertionsDisabled && this.size != 0) {
            throw new AssertionError();
         }
      } else {
         this.head = var2;
         if (!$assertionsDisabled && this.size <= 0) {
            throw new AssertionError();
         }
      }

   }

   private static void safeFail(ChannelPromise var0, Throwable var1) {
      if (!(var0 instanceof VoidChannelPromise) && !var0.tryFailure(var1)) {
         logger.warn("Failed to mark a promise as failure because it's done already: {}", var0, var1);
      }

   }

   static final class PendingWrite {
      private static final Recycler RECYCLER = new Recycler() {
         protected PendingWriteQueue.PendingWrite newObject(Recycler.Handle var1) {
            return new PendingWriteQueue.PendingWrite(var1);
         }

         protected Object newObject(Recycler.Handle var1) {
            return this.newObject(var1);
         }
      };
      private final Recycler.Handle handle;
      private PendingWriteQueue.PendingWrite next;
      private long size;
      private ChannelPromise promise;
      private Object msg;

      private PendingWrite(Recycler.Handle var1) {
         this.handle = var1;
      }

      static PendingWriteQueue.PendingWrite newInstance(Object var0, int var1, ChannelPromise var2) {
         PendingWriteQueue.PendingWrite var3 = (PendingWriteQueue.PendingWrite)RECYCLER.get();
         var3.size = (long)var1;
         var3.msg = var0;
         var3.promise = var2;
         return var3;
      }

      private void recycle() {
         this.size = 0L;
         this.next = null;
         this.msg = null;
         this.promise = null;
         RECYCLER.recycle(this, this.handle);
      }

      static PendingWriteQueue.PendingWrite access$002(PendingWriteQueue.PendingWrite var0, PendingWriteQueue.PendingWrite var1) {
         return var0.next = var1;
      }

      static long access$100(PendingWriteQueue.PendingWrite var0) {
         return var0.size;
      }

      static PendingWriteQueue.PendingWrite access$000(PendingWriteQueue.PendingWrite var0) {
         return var0.next;
      }

      static Object access$200(PendingWriteQueue.PendingWrite var0) {
         return var0.msg;
      }

      static ChannelPromise access$300(PendingWriteQueue.PendingWrite var0) {
         return var0.promise;
      }

      static void access$400(PendingWriteQueue.PendingWrite var0) {
         var0.recycle();
      }

      PendingWrite(Recycler.Handle var1, Object var2) {
         this(var1);
      }
   }
}
