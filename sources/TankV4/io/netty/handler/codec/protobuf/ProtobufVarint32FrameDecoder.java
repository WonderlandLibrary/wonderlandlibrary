package io.netty.handler.codec.protobuf;

import com.google.protobuf.CodedInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class ProtobufVarint32FrameDecoder extends ByteToMessageDecoder {
   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      var2.markReaderIndex();
      byte[] var4 = new byte[5];

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (!var2.isReadable()) {
            var2.resetReaderIndex();
            return;
         }

         var4[var5] = var2.readByte();
         if (var4[var5] >= 0) {
            int var6 = CodedInputStream.newInstance(var4, 0, var5 + 1).readRawVarint32();
            if (var6 < 0) {
               throw new CorruptedFrameException("negative length: " + var6);
            }

            if (var2.readableBytes() < var6) {
               var2.resetReaderIndex();
               return;
            }

            var3.add(var2.readBytes(var6));
            return;
         }
      }

      throw new CorruptedFrameException("length wider than 32-bit");
   }
}
