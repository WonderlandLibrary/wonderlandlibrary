package io.netty.channel.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public abstract class AbstractNioByteChannel extends AbstractNioChannel {
   private Runnable flushTask;

   protected AbstractNioByteChannel(Channel var1, SelectableChannel var2) {
      super(var1, var2, 1);
   }

   protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() {
      return new AbstractNioByteChannel.NioByteUnsafe(this);
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      int var2 = -1;

      while(true) {
         Object var3 = var1.current();
         if (var3 == null) {
            this.clearOpWrite();
            break;
         }

         boolean var6;
         if (var3 instanceof ByteBuf) {
            ByteBuf var12 = (ByteBuf)var3;
            int var13 = var12.readableBytes();
            if (var13 == 0) {
               var1.remove();
            } else {
               if (!var12.isDirect()) {
                  ByteBufAllocator var14 = this.alloc();
                  if (var14.isDirectBufferPooled()) {
                     var12 = var14.directBuffer(var13).writeBytes(var12);
                     var1.current(var12);
                  }
               }

               var6 = false;
               boolean var15 = false;
               long var8 = 0L;
               if (var2 == -1) {
                  var2 = this.config().getWriteSpinCount();
               }

               for(int var16 = var2 - 1; var16 >= 0; --var16) {
                  int var11 = this.doWriteBytes(var12);
                  if (var11 == 0) {
                     var6 = true;
                     break;
                  }

                  var8 += (long)var11;
                  if (!var12.isReadable()) {
                     var15 = true;
                     break;
                  }
               }

               var1.progress(var8);
               if (!var15) {
                  this.incompleteWrite(var6);
                  break;
               }

               var1.remove();
            }
         } else {
            if (!(var3 instanceof FileRegion)) {
               throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var3));
            }

            FileRegion var4 = (FileRegion)var3;
            boolean var5 = false;
            var6 = false;
            long var7 = 0L;
            if (var2 == -1) {
               var2 = this.config().getWriteSpinCount();
            }

            for(int var9 = var2 - 1; var9 >= 0; --var9) {
               long var10 = this.doWriteFileRegion(var4);
               if (var10 == 0L) {
                  var5 = true;
                  break;
               }

               var7 += var10;
               if (var4.transfered() >= var4.count()) {
                  var6 = true;
                  break;
               }
            }

            var1.progress(var7);
            if (!var6) {
               this.incompleteWrite(var5);
               break;
            }

            var1.remove();
         }
      }

   }

   protected final void incompleteWrite(boolean var1) {
      if (var1) {
         this.setOpWrite();
      } else {
         Runnable var2 = this.flushTask;
         if (var2 == null) {
            var2 = this.flushTask = new Runnable(this) {
               final AbstractNioByteChannel this$0;

               {
                  this.this$0 = var1;
               }

               public void run() {
                  this.this$0.flush();
               }
            };
         }

         this.eventLoop().execute(var2);
      }

   }

   protected abstract long doWriteFileRegion(FileRegion var1) throws Exception;

   protected abstract int doReadBytes(ByteBuf var1) throws Exception;

   protected abstract int doWriteBytes(ByteBuf var1) throws Exception;

   protected final void setOpWrite() {
      SelectionKey var1 = this.selectionKey();
      if (var1.isValid()) {
         int var2 = var1.interestOps();
         if ((var2 & 4) == 0) {
            var1.interestOps(var2 | 4);
         }

      }
   }

   protected final void clearOpWrite() {
      SelectionKey var1 = this.selectionKey();
      if (var1.isValid()) {
         int var2 = var1.interestOps();
         if ((var2 & 4) != 0) {
            var1.interestOps(var2 & -5);
         }

      }
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return this.newUnsafe();
   }

   private final class NioByteUnsafe extends AbstractNioChannel.AbstractNioUnsafe {
      private RecvByteBufAllocator.Handle allocHandle;
      final AbstractNioByteChannel this$0;

      private NioByteUnsafe(AbstractNioByteChannel var1) {
         super();
         this.this$0 = var1;
      }

      private void removeReadOp() {
         SelectionKey var1 = this.this$0.selectionKey();
         if (var1.isValid()) {
            int var2 = var1.interestOps();
            if ((var2 & this.this$0.readInterestOp) != 0) {
               var1.interestOps(var2 & ~this.this$0.readInterestOp);
            }

         }
      }

      private void closeOnRead(ChannelPipeline var1) {
         SelectionKey var2 = this.this$0.selectionKey();
         this.this$0.setInputShutdown();
         if (this.this$0.isOpen()) {
            if (Boolean.TRUE.equals(this.this$0.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
               var2.interestOps(var2.interestOps() & ~this.this$0.readInterestOp);
               var1.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
            } else {
               this.close(this.voidPromise());
            }
         }

      }

      private void handleReadException(ChannelPipeline var1, ByteBuf var2, Throwable var3, boolean var4) {
         if (var2 != null) {
            if (var2.isReadable()) {
               var1.fireChannelRead(var2);
            } else {
               var2.release();
            }
         }

         var1.fireChannelReadComplete();
         var1.fireExceptionCaught(var3);
         if (var4 || var3 instanceof IOException) {
            this.closeOnRead(var1);
         }

      }

      public void read() {
         ChannelConfig var1 = this.this$0.config();
         ChannelPipeline var2 = this.this$0.pipeline();
         ByteBufAllocator var3 = var1.getAllocator();
         int var4 = var1.getMaxMessagesPerRead();
         RecvByteBufAllocator.Handle var5 = this.allocHandle;
         if (var5 == null) {
            this.allocHandle = var5 = var1.getRecvByteBufAllocator().newHandle();
         }

         if (!var1.isAutoRead()) {
            this.removeReadOp();
         }

         ByteBuf var6 = null;
         int var7 = 0;
         boolean var8 = false;

         try {
            int var9 = var5.guess();
            int var10 = 0;

            do {
               var6 = var3.ioBuffer(var9);
               int var11 = var6.writableBytes();
               int var12 = this.this$0.doReadBytes(var6);
               if (var12 <= 0) {
                  var6.release();
                  var8 = var12 < 0;
                  break;
               }

               var2.fireChannelRead(var6);
               var6 = null;
               if (var10 >= Integer.MAX_VALUE - var12) {
                  var10 = Integer.MAX_VALUE;
                  break;
               }

               var10 += var12;
               if (var12 < var11) {
                  break;
               }

               ++var7;
            } while(var7 < var4);

            var2.fireChannelReadComplete();
            var5.record(var10);
            if (var8) {
               this.closeOnRead(var2);
               var8 = false;
            }
         } catch (Throwable var13) {
            this.handleReadException(var2, var6, var13, var8);
         }

      }

      NioByteUnsafe(AbstractNioByteChannel var1, Object var2) {
         this(var1);
      }
   }
}
