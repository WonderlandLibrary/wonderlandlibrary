package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class NettyCompressionDecoder extends ByteToMessageDecoder {
   private int treshold;
   private final Inflater inflater;

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception, DataFormatException {
      if (var2.readableBytes() != 0) {
         PacketBuffer var4 = new PacketBuffer(var2);
         int var5 = var4.readVarIntFromBuffer();
         if (var5 == 0) {
            var3.add(var4.readBytes(var4.readableBytes()));
         } else {
            if (var5 < this.treshold) {
               throw new DecoderException("Badly compressed packet - size of " + var5 + " is below server threshold of " + this.treshold);
            }

            if (var5 > 2097152) {
               throw new DecoderException("Badly compressed packet - size of " + var5 + " is larger than protocol maximum of " + 2097152);
            }

            byte[] var6 = new byte[var4.readableBytes()];
            var4.readBytes(var6);
            this.inflater.setInput(var6);
            byte[] var7 = new byte[var5];
            this.inflater.inflate(var7);
            var3.add(Unpooled.wrappedBuffer(var7));
            this.inflater.reset();
         }
      }

   }

   public NettyCompressionDecoder(int var1) {
      this.treshold = var1;
      this.inflater = new Inflater();
   }

   public void setCompressionTreshold(int var1) {
      this.treshold = var1;
   }
}
