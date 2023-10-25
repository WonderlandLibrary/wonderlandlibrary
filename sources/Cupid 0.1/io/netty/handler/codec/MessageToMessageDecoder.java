package io.netty.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class MessageToMessageDecoder<I> extends ChannelInboundHandlerAdapter {
  private final TypeParameterMatcher matcher;
  
  protected MessageToMessageDecoder() {
    this.matcher = TypeParameterMatcher.find(this, MessageToMessageDecoder.class, "I");
  }
  
  protected MessageToMessageDecoder(Class<? extends I> inboundMessageType) {
    this.matcher = TypeParameterMatcher.get(inboundMessageType);
  }
  
  public boolean acceptInboundMessage(Object msg) throws Exception {
    return this.matcher.match(msg);
  }
  
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    RecyclableArrayList out = RecyclableArrayList.newInstance();
    try {
      if (acceptInboundMessage(msg)) {
        I cast = (I)msg;
        try {
          decode(ctx, cast, (List<Object>)out);
        } finally {
          ReferenceCountUtil.release(cast);
        } 
      } else {
        out.add(msg);
      } 
    } catch (DecoderException e) {
      throw e;
    } catch (Exception e) {
      throw new DecoderException(e);
    } finally {
      int size = out.size();
      for (int i = 0; i < size; i++)
        ctx.fireChannelRead(out.get(i)); 
      out.recycle();
    } 
  }
  
  protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, I paramI, List<Object> paramList) throws Exception;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\MessageToMessageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */