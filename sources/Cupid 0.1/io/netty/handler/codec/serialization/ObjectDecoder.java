package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ObjectDecoder extends LengthFieldBasedFrameDecoder {
  private final ClassResolver classResolver;
  
  public ObjectDecoder(ClassResolver classResolver) {
    this(1048576, classResolver);
  }
  
  public ObjectDecoder(int maxObjectSize, ClassResolver classResolver) {
    super(maxObjectSize, 0, 4, 0, 4);
    this.classResolver = classResolver;
  }
  
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    ByteBuf frame = (ByteBuf)super.decode(ctx, in);
    if (frame == null)
      return null; 
    ObjectInputStream is = new CompactObjectInputStream((InputStream)new ByteBufInputStream(frame), this.classResolver);
    Object result = is.readObject();
    is.close();
    return result;
  }
  
  protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
    return buffer.slice(index, length);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\serialization\ObjectDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */