package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

@Sharable
public class ObjectEncoder extends MessageToByteEncoder<Serializable> {
  private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
  
  protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
    int startIdx = out.writerIndex();
    ByteBufOutputStream bout = new ByteBufOutputStream(out);
    bout.write(LENGTH_PLACEHOLDER);
    ObjectOutputStream oout = new CompactObjectOutputStream((OutputStream)bout);
    oout.writeObject(msg);
    oout.flush();
    oout.close();
    int endIdx = out.writerIndex();
    out.setInt(startIdx, endIdx - startIdx - 4);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\serialization\ObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */