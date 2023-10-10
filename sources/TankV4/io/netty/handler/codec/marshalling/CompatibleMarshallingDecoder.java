package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

public class CompatibleMarshallingDecoder extends ReplayingDecoder {
   protected final UnmarshallerProvider provider;
   protected final int maxObjectSize;
   private boolean discardingTooLongFrame;

   public CompatibleMarshallingDecoder(UnmarshallerProvider var1, int var2) {
      this.provider = var1;
      this.maxObjectSize = var2;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      if (this.discardingTooLongFrame) {
         var2.skipBytes(this.actualReadableBytes());
         this.checkpoint();
      } else {
         Unmarshaller var4 = this.provider.getUnmarshaller(var1);
         Object var5 = new ChannelBufferByteInput(var2);
         if (this.maxObjectSize != Integer.MAX_VALUE) {
            var5 = new LimitingByteInput((ByteInput)var5, (long)this.maxObjectSize);
         }

         try {
            var4.start((ByteInput)var5);
            Object var6 = var4.readObject();
            var4.finish();
            var3.add(var6);
         } catch (LimitingByteInput.TooBigObjectException var8) {
            this.discardingTooLongFrame = true;
            throw new TooLongFrameException();
         }

         var4.close();
      }
   }

   protected void decodeLast(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      switch(var2.readableBytes()) {
      case 0:
         return;
      case 1:
         if (var2.getByte(var2.readerIndex()) == 121) {
            var2.skipBytes(1);
            return;
         }
      default:
         this.decode(var1, var2, var3);
      }
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      if (var2 instanceof TooLongFrameException) {
         var1.close();
      } else {
         super.exceptionCaught(var1, var2);
      }

   }
}
