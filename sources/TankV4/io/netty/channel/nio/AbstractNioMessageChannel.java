package io.netty.channel.nio;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ServerChannel;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNioMessageChannel extends AbstractNioChannel {
   protected AbstractNioMessageChannel(Channel var1, SelectableChannel var2, int var3) {
      super(var1, var2, var3);
   }

   protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() {
      return new AbstractNioMessageChannel.NioMessageUnsafe(this);
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      SelectionKey var2 = this.selectionKey();
      int var3 = var2.interestOps();

      while(true) {
         Object var4 = var1.current();
         if (var4 == null) {
            if ((var3 & 4) != 0) {
               var2.interestOps(var3 & -5);
            }
            break;
         }

         boolean var5 = false;

         for(int var6 = this.config().getWriteSpinCount() - 1; var6 >= 0; --var6) {
            if (this.doWriteMessage(var4, var1)) {
               var5 = true;
               break;
            }
         }

         if (!var5) {
            if ((var3 & 4) == 0) {
               var2.interestOps(var3 | 4);
            }
            break;
         }

         var1.remove();
      }

   }

   protected abstract int doReadMessages(List var1) throws Exception;

   protected abstract boolean doWriteMessage(Object var1, ChannelOutboundBuffer var2) throws Exception;

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return this.newUnsafe();
   }

   private final class NioMessageUnsafe extends AbstractNioChannel.AbstractNioUnsafe {
      private final List readBuf;
      static final boolean $assertionsDisabled = !AbstractNioMessageChannel.class.desiredAssertionStatus();
      final AbstractNioMessageChannel this$0;

      private NioMessageUnsafe(AbstractNioMessageChannel var1) {
         super();
         this.this$0 = var1;
         this.readBuf = new ArrayList();
      }

      private void removeReadOp() {
         SelectionKey var1 = this.this$0.selectionKey();
         int var2 = var1.interestOps();
         if ((var2 & this.this$0.readInterestOp) != 0) {
            var1.interestOps(var2 & ~this.this$0.readInterestOp);
         }

      }

      public void read() {
         if (!$assertionsDisabled && !this.this$0.eventLoop().inEventLoop()) {
            throw new AssertionError();
         } else {
            if (!this.this$0.config().isAutoRead()) {
               this.removeReadOp();
            }

            ChannelConfig var1 = this.this$0.config();
            int var2 = var1.getMaxMessagesPerRead();
            boolean var3 = var1.isAutoRead();
            ChannelPipeline var4 = this.this$0.pipeline();
            boolean var5 = false;
            Throwable var6 = null;

            int var7;
            try {
               do {
                  var7 = this.this$0.doReadMessages(this.readBuf);
                  if (var7 == 0) {
                     break;
                  }

                  if (var7 < 0) {
                     var5 = true;
                     break;
                  }
               } while(!(this.readBuf.size() >= var2 | !var3));
            } catch (Throwable var9) {
               var6 = var9;
            }

            var7 = this.readBuf.size();

            for(int var8 = 0; var8 < var7; ++var8) {
               var4.fireChannelRead(this.readBuf.get(var8));
            }

            this.readBuf.clear();
            var4.fireChannelReadComplete();
            if (var6 != null) {
               if (var6 instanceof IOException) {
                  var5 = !(this.this$0 instanceof ServerChannel);
               }

               var4.fireExceptionCaught(var6);
            }

            if (var5 && this.this$0.isOpen()) {
               this.close(this.voidPromise());
            }

         }
      }

      NioMessageUnsafe(AbstractNioMessageChannel var1, Object var2) {
         this(var1);
      }
   }
}
