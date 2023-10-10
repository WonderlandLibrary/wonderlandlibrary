package io.netty.handler.codec.protobuf;

import com.google.protobuf.CodedOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class ProtobufVarint32LengthFieldPrepender extends MessageToByteEncoder {
   protected void encode(ChannelHandlerContext var1, ByteBuf var2, ByteBuf var3) throws Exception {
      int var4 = var2.readableBytes();
      int var5 = CodedOutputStream.computeRawVarint32Size(var4);
      var3.ensureWritable(var5 + var4);
      CodedOutputStream var6 = CodedOutputStream.newInstance(new ByteBufOutputStream(var3));
      var6.writeRawVarint32(var4);
      var6.flush();
      var3.writeBytes(var2, var2.readerIndex(), var4);
   }

   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      this.encode(var1, (ByteBuf)var2, var3);
   }
}
