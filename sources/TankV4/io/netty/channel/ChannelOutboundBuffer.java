package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public final class ChannelOutboundBuffer {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelOutboundBuffer.class);
   private static final int INITIAL_CAPACITY = 32;
   private static final Recycler RECYCLER = new Recycler() {
      protected ChannelOutboundBuffer newObject(Recycler.Handle var1) {
         return new ChannelOutboundBuffer(var1);
      }

      protected Object newObject(Recycler.Handle var1) {
         return this.newObject(var1);
      }
   };
   private final Recycler.Handle handle;
   private AbstractChannel channel;
   private ChannelOutboundBuffer.Entry[] buffer;
   private int flushed;
   private int unflushed;
   private int tail;
   private ByteBuffer[] nioBuffers;
   private int nioBufferCount;
   private long nioBufferSize;
   private boolean inFail;
   private static final AtomicLongFieldUpdater TOTAL_PENDING_SIZE_UPDATER = AtomicLongFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
   private volatile long totalPendingSize;
   private static final AtomicIntegerFieldUpdater WRITABLE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "writable");
   private volatile int writable;

   static ChannelOutboundBuffer newInstance(AbstractChannel var0) {
      ChannelOutboundBuffer var1 = (ChannelOutboundBuffer)RECYCLER.get();
      var1.channel = var0;
      var1.totalPendingSize = 0L;
      var1.writable = 1;
      return var1;
   }

   private ChannelOutboundBuffer(Recycler.Handle var1) {
      this.writable = 1;
      this.handle = var1;
      this.buffer = new ChannelOutboundBuffer.Entry[32];

      for(int var2 = 0; var2 < this.buffer.length; ++var2) {
         this.buffer[var2] = new ChannelOutboundBuffer.Entry();
      }

      this.nioBuffers = new ByteBuffer[32];
   }

   void addMessage(Object var1, ChannelPromise var2) {
      int var3 = this.channel.estimatorHandle().size(var1);
      if (var3 < 0) {
         var3 = 0;
      }

      ChannelOutboundBuffer.Entry var4 = this.buffer[this.tail++];
      var4.msg = var1;
      var4.pendingSize = var3;
      var4.promise = var2;
      var4.total = total(var1);
      this.tail &= this.buffer.length - 1;
      if (this.tail == this.flushed) {
         this.addCapacity();
      }

      this.incrementPendingOutboundBytes(var3);
   }

   private void addCapacity() {
      int var1 = this.flushed;
      int var2 = this.buffer.length;
      int var3 = var2 - var1;
      int var4 = this.size();
      int var5 = var2 << 1;
      if (var5 < 0) {
         throw new IllegalStateException();
      } else {
         ChannelOutboundBuffer.Entry[] var6 = new ChannelOutboundBuffer.Entry[var5];
         System.arraycopy(this.buffer, var1, var6, 0, var3);
         System.arraycopy(this.buffer, 0, var6, var3, var1);

         for(int var7 = var2; var7 < var6.length; ++var7) {
            var6[var7] = new ChannelOutboundBuffer.Entry();
         }

         this.buffer = var6;
         this.flushed = 0;
         this.unflushed = var4;
         this.tail = var2;
      }
   }

   void addFlush() {
      this.unflushed = this.tail;
   }

   void incrementPendingOutboundBytes(int var1) {
      AbstractChannel var2 = this.channel;
      if (var1 != 0 && var2 != null) {
         long var3 = this.totalPendingSize;

         long var5;
         for(var5 = var3 + (long)var1; !TOTAL_PENDING_SIZE_UPDATER.compareAndSet(this, var3, var5); var5 = var3 + (long)var1) {
            var3 = this.totalPendingSize;
         }

         int var7 = var2.config().getWriteBufferHighWaterMark();
         if (var5 > (long)var7 && WRITABLE_UPDATER.compareAndSet(this, 1, 0)) {
            var2.pipeline().fireChannelWritabilityChanged();
         }

      }
   }

   void decrementPendingOutboundBytes(int var1) {
      AbstractChannel var2 = this.channel;
      if (var1 != 0 && var2 != null) {
         long var3 = this.totalPendingSize;

         long var5;
         for(var5 = var3 - (long)var1; !TOTAL_PENDING_SIZE_UPDATER.compareAndSet(this, var3, var5); var5 = var3 - (long)var1) {
            var3 = this.totalPendingSize;
         }

         int var7 = var2.config().getWriteBufferLowWaterMark();
         if ((var5 == 0L || var5 < (long)var7) && WRITABLE_UPDATER.compareAndSet(this, 0, 1)) {
            var2.pipeline().fireChannelWritabilityChanged();
         }

      }
   }

   private static long total(Object var0) {
      if (var0 instanceof ByteBuf) {
         return (long)((ByteBuf)var0).readableBytes();
      } else if (var0 instanceof FileRegion) {
         return ((FileRegion)var0).count();
      } else {
         return var0 instanceof ByteBufHolder ? (long)((ByteBufHolder)var0).content().readableBytes() : -1L;
      }
   }

   public Object current() {
      // $FF: Couldn't be decompiled
   }

   public void current(Object var1) {
      ChannelOutboundBuffer.Entry var2 = this.buffer[this.flushed];
      safeRelease(var2.msg);
      var2.msg = var1;
   }

   public void progress(long var1) {
      ChannelOutboundBuffer.Entry var3 = this.buffer[this.flushed];
      ChannelPromise var4 = var3.promise;
      if (var4 instanceof ChannelProgressivePromise) {
         long var5 = var3.progress + var1;
         var3.progress = var5;
         ((ChannelProgressivePromise)var4).tryProgress(var5, var3.total);
      }

   }

   public boolean remove() {
      // $FF: Couldn't be decompiled
   }

   public ByteBuffer[] nioBuffers() {
      long var1 = 0L;
      int var3 = 0;
      int var4 = this.buffer.length - 1;
      ByteBufAllocator var5 = this.channel.alloc();
      ByteBuffer[] var6 = this.nioBuffers;

      Object var7;
      for(int var8 = this.flushed; var8 != this.unflushed && (var7 = this.buffer[var8].msg) != null; var8 = var8 + 1 & var4) {
         if (!(var7 instanceof ByteBuf)) {
            this.nioBufferCount = 0;
            this.nioBufferSize = 0L;
            return null;
         }

         ChannelOutboundBuffer.Entry var9 = this.buffer[var8];
         ByteBuf var10 = (ByteBuf)var7;
         int var11 = var10.readerIndex();
         int var12 = var10.writerIndex() - var11;
         if (var12 > 0) {
            var1 += (long)var12;
            int var13 = var9.count;
            if (var13 == -1) {
               var9.count = var13 = var10.nioBufferCount();
            }

            int var14 = var3 + var13;
            if (var14 > var6.length) {
               this.nioBuffers = var6 = expandNioBufferArray(var6, var14, var3);
            }

            if (!var10.isDirect() && var5.isDirectBufferPooled()) {
               var3 = fillBufferArrayNonDirect(var9, var10, var11, var12, var5, var6, var3);
            } else if (var13 == 1) {
               ByteBuffer var15 = var9.buf;
               if (var15 == null) {
                  var9.buf = var15 = var10.internalNioBuffer(var11, var12);
               }

               var6[var3++] = var15;
            } else {
               ByteBuffer[] var16 = var9.buffers;
               if (var16 == null) {
                  var9.buffers = var16 = var10.nioBuffers();
               }

               var3 = fillBufferArray(var16, var6, var3);
            }
         }
      }

      this.nioBufferCount = var3;
      this.nioBufferSize = var1;
      return var6;
   }

   private static int fillBufferArray(ByteBuffer[] var0, ByteBuffer[] var1, int var2) {
      ByteBuffer[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ByteBuffer var6 = var3[var5];
         if (var6 == null) {
            break;
         }

         var1[var2++] = var6;
      }

      return var2;
   }

   private static int fillBufferArrayNonDirect(ChannelOutboundBuffer.Entry var0, ByteBuf var1, int var2, int var3, ByteBufAllocator var4, ByteBuffer[] var5, int var6) {
      ByteBuf var7 = var4.directBuffer(var3);
      var7.writeBytes(var1, var2, var3);
      var1.release();
      var0.msg = var7;
      ByteBuffer var8 = var0.buf = var7.internalNioBuffer(0, var3);
      var0.count = 1;
      var5[var6++] = var8;
      return var6;
   }

   private static ByteBuffer[] expandNioBufferArray(ByteBuffer[] var0, int var1, int var2) {
      int var3 = var0.length;

      do {
         var3 <<= 1;
         if (var3 < 0) {
            throw new IllegalStateException();
         }
      } while(var1 > var3);

      ByteBuffer[] var4 = new ByteBuffer[var3];
      System.arraycopy(var0, 0, var4, 0, var2);
      return var4;
   }

   public int nioBufferCount() {
      return this.nioBufferCount;
   }

   public long nioBufferSize() {
      return this.nioBufferSize;
   }

   boolean getWritable() {
      return this.writable != 0;
   }

   public int size() {
      return this.unflushed - this.flushed & this.buffer.length - 1;
   }

   void failFlushed(Throwable var1) {
      if (!this.inFail) {
         this.inFail = true;

         while(this != var1) {
         }

         this.inFail = false;
      }
   }

   void close(ClosedChannelException param1) {
      // $FF: Couldn't be decompiled
   }

   private static void safeRelease(Object var0) {
      try {
         ReferenceCountUtil.release(var0);
      } catch (Throwable var2) {
         logger.warn("Failed to release a message.", var2);
      }

   }

   private static void safeFail(ChannelPromise var0, Throwable var1) {
      if (!(var0 instanceof VoidChannelPromise) && !var0.tryFailure(var1)) {
         logger.warn("Promise done already: {} - new exception is:", var0, var1);
      }

   }

   public void recycle() {
      if (this.buffer.length > 32) {
         ChannelOutboundBuffer.Entry[] var1 = new ChannelOutboundBuffer.Entry[32];
         System.arraycopy(this.buffer, 0, var1, 0, 32);
         this.buffer = var1;
      }

      if (this.nioBuffers.length > 32) {
         this.nioBuffers = new ByteBuffer[32];
      } else {
         Arrays.fill(this.nioBuffers, (Object)null);
      }

      this.flushed = 0;
      this.unflushed = 0;
      this.tail = 0;
      this.channel = null;
      RECYCLER.recycle(this, this.handle);
   }

   public long totalPendingWriteBytes() {
      return this.totalPendingSize;
   }

   ChannelOutboundBuffer(Recycler.Handle var1, Object var2) {
      this(var1);
   }

   private static final class Entry {
      Object msg;
      ByteBuffer[] buffers;
      ByteBuffer buf;
      ChannelPromise promise;
      long progress;
      long total;
      int pendingSize;
      int count;

      private Entry() {
         this.count = -1;
      }

      public void clear() {
         this.buffers = null;
         this.buf = null;
         this.msg = null;
         this.promise = null;
         this.progress = 0L;
         this.total = 0L;
         this.pendingSize = 0;
         this.count = -1;
      }

      Entry(Object var1) {
         this();
      }
   }
}
