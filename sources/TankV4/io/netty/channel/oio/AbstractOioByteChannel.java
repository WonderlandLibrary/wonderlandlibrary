package io.netty.channel.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;

public abstract class AbstractOioByteChannel extends AbstractOioChannel {
   private volatile boolean inputShutdown;
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);

   protected AbstractOioByteChannel(Channel var1) {
      super(var1);
   }

   protected boolean isInputShutdown() {
      return this.inputShutdown;
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   protected void doRead() {
      if (this == false) {
         ChannelPipeline var1 = this.pipeline();
         ByteBuf var2 = this.alloc().buffer();
         boolean var3 = false;
         boolean var4 = false;
         Object var5 = null;

         try {
            do {
               int var6 = this.doReadBytes(var2);
               if (var6 > 0) {
                  var4 = true;
               } else if (var6 < 0) {
                  var3 = true;
               }

               int var7 = this.available();
               if (var7 <= 0) {
                  break;
               }

               if (!var2.isWritable()) {
                  int var8 = var2.capacity();
                  int var9 = var2.maxCapacity();
                  if (var8 == var9) {
                     if (var4) {
                        var4 = false;
                        var1.fireChannelRead(var2);
                        var2 = this.alloc().buffer();
                     }
                  } else {
                     int var10 = var2.writerIndex();
                     if (var10 + var7 > var9) {
                        var2.capacity(var9);
                     } else {
                        var2.ensureWritable(var7);
                     }
                  }
               }
            } while(this.config().isAutoRead());
         } catch (Throwable var12) {
            if (var4) {
               var1.fireChannelRead(var2);
            } else {
               var2.release();
            }

            var1.fireChannelReadComplete();
            if (var12 != null) {
               if (var12 instanceof IOException) {
                  var3 = true;
                  this.pipeline().fireExceptionCaught(var12);
               } else {
                  var1.fireExceptionCaught(var12);
                  this.unsafe().close(this.voidPromise());
               }
            }

            if (var3) {
               this.inputShutdown = true;
               if (this.isOpen()) {
                  if (Boolean.TRUE.equals(this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                     var1.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                  } else {
                     this.unsafe().close(this.unsafe().voidPromise());
                  }

                  return;
               }
            }

            return;
         }

         if (var4) {
            var1.fireChannelRead(var2);
         } else {
            var2.release();
         }

         var1.fireChannelReadComplete();
         if (var5 != null) {
            if (var5 instanceof IOException) {
               var3 = true;
               this.pipeline().fireExceptionCaught((Throwable)var5);
            } else {
               var1.fireExceptionCaught((Throwable)var5);
               this.unsafe().close(this.voidPromise());
            }
         }

         if (var3) {
            this.inputShutdown = true;
            if (this.isOpen()) {
               if (Boolean.TRUE.equals(this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                  var1.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
               } else {
                  this.unsafe().close(this.unsafe().voidPromise());
               }
            }
         }

      }
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         Object var2 = var1.current();
         if (var2 == null) {
            return;
         }

         if (!(var2 instanceof ByteBuf)) {
            if (var2 instanceof FileRegion) {
               this.doWriteFileRegion((FileRegion)var2);
               var1.remove();
            } else {
               var1.remove(new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var2)));
            }
         } else {
            ByteBuf var3 = (ByteBuf)var2;

            while(var3.isReadable()) {
               this.doWriteBytes(var3);
            }

            var1.remove();
         }
      }
   }

   protected abstract int available();

   protected abstract int doReadBytes(ByteBuf var1) throws Exception;

   protected abstract void doWriteBytes(ByteBuf var1) throws Exception;

   protected abstract void doWriteFileRegion(FileRegion var1) throws Exception;
}
