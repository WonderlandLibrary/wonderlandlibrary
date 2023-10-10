package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Signal;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class ReplayingDecoder extends ByteToMessageDecoder {
   static final Signal REPLAY = Signal.valueOf(ReplayingDecoder.class.getName() + ".REPLAY");
   private final ReplayingDecoderBuffer replayable;
   private Object state;
   private int checkpoint;

   protected ReplayingDecoder() {
      this((Object)null);
   }

   protected ReplayingDecoder(Object var1) {
      this.replayable = new ReplayingDecoderBuffer();
      this.checkpoint = -1;
      this.state = var1;
   }

   protected void checkpoint() {
      this.checkpoint = this.internalBuffer().readerIndex();
   }

   protected void checkpoint(Object var1) {
      this.checkpoint();
      this.state(var1);
   }

   protected Object state() {
      return this.state;
   }

   protected Object state(Object var1) {
      Object var2 = this.state;
      this.state = var1;
      return var2;
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      RecyclableArrayList var2 = RecyclableArrayList.newInstance();

      int var3;
      int var4;
      try {
         this.replayable.terminate();
         this.callDecode(var1, this.internalBuffer(), var2);
         this.decodeLast(var1, this.replayable, var2);
      } catch (Signal var8) {
         var8.expect(REPLAY);
         if (this.cumulation != null) {
            this.cumulation.release();
            this.cumulation = null;
         }

         var3 = var2.size();

         for(var4 = 0; var4 < var3; ++var4) {
            var1.fireChannelRead(var2.get(var4));
         }

         var1.fireChannelInactive();
         var2.recycle();
         return;
      } catch (DecoderException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new DecoderException(var10);
      }

      if (this.cumulation != null) {
         this.cumulation.release();
         this.cumulation = null;
      }

      var3 = var2.size();

      for(var4 = 0; var4 < var3; ++var4) {
         var1.fireChannelRead(var2.get(var4));
      }

      var1.fireChannelInactive();
      var2.recycle();
   }

   protected void callDecode(ChannelHandlerContext var1, ByteBuf var2, List var3) {
      this.replayable.setCumulation(var2);

      try {
         while(var2.isReadable()) {
            int var4 = this.checkpoint = var2.readerIndex();
            int var5 = var3.size();
            Object var6 = this.state;
            int var7 = var2.readableBytes();

            try {
               this.decode(var1, this.replayable, var3);
               if (var1.isRemoved()) {
                  break;
               }

               if (var5 == var3.size()) {
                  if (var7 == var2.readableBytes() && var6 == this.state) {
                     throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() must consume the inbound " + "data or change its state if it did not decode anything.");
                  }
                  continue;
               }
            } catch (Signal var10) {
               var10.expect(REPLAY);
               if (!var1.isRemoved()) {
                  int var9 = this.checkpoint;
                  if (var9 >= 0) {
                     var2.readerIndex(var9);
                  }
               }
               break;
            }

            if (var4 == var2.readerIndex() && var6 == this.state) {
               throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() method must consume the inbound data " + "or change its state if it decoded something.");
            }

            if (this.isSingleDecode()) {
               break;
            }
         }

      } catch (DecoderException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new DecoderException(var12);
      }
   }
}
