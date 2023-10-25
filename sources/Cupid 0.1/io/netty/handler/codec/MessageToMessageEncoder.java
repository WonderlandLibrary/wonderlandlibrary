package io.netty.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class MessageToMessageEncoder<I> extends ChannelOutboundHandlerAdapter {
  private final TypeParameterMatcher matcher;
  
  protected MessageToMessageEncoder() {
    this.matcher = TypeParameterMatcher.find(this, MessageToMessageEncoder.class, "I");
  }
  
  protected MessageToMessageEncoder(Class<? extends I> outboundMessageType) {
    this.matcher = TypeParameterMatcher.get(outboundMessageType);
  }
  
  public boolean acceptOutboundMessage(Object msg) throws Exception {
    return this.matcher.match(msg);
  }
  
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    RecyclableArrayList out = null;
    try {
      if (acceptOutboundMessage(msg)) {
        out = RecyclableArrayList.newInstance();
        I cast = (I)msg;
        try {
          encode(ctx, cast, (List<Object>)out);
        } finally {
          ReferenceCountUtil.release(cast);
        } 
        if (out.isEmpty()) {
          out.recycle();
          out = null;
          throw new EncoderException(StringUtil.simpleClassName(this) + " must produce at least one message.");
        } 
      } else {
        ctx.write(msg, promise);
      } 
    } catch (EncoderException e) {
      throw e;
    } catch (Throwable t) {
      throw new EncoderException(t);
    } finally {
      if (out != null) {
        int sizeMinusOne = out.size() - 1;
        if (sizeMinusOne == 0) {
          ctx.write(out.get(0), promise);
        } else if (sizeMinusOne > 0) {
          ChannelPromise voidPromise = ctx.voidPromise();
          boolean isVoidPromise = (promise == voidPromise);
          for (int i = 0; i < sizeMinusOne; i++) {
            ChannelPromise p;
            if (isVoidPromise) {
              p = voidPromise;
            } else {
              p = ctx.newPromise();
            } 
            ctx.write(out.get(i), p);
          } 
          ctx.write(out.get(sizeMinusOne), promise);
        } 
        out.recycle();
      } 
    } 
  }
  
  protected abstract void encode(ChannelHandlerContext paramChannelHandlerContext, I paramI, List<Object> paramList) throws Exception;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\MessageToMessageEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */