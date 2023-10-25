package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class ByteToMessageCodec<I> extends ChannelDuplexHandler {
  private final TypeParameterMatcher outboundMsgMatcher;
  
  private final MessageToByteEncoder<I> encoder;
  
  private final ByteToMessageDecoder decoder = new ByteToMessageDecoder() {
      public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteToMessageCodec.this.decode(ctx, in, out);
      }
      
      protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteToMessageCodec.this.decodeLast(ctx, in, out);
      }
    };
  
  protected ByteToMessageCodec() {
    this(true);
  }
  
  protected ByteToMessageCodec(Class<? extends I> outboundMessageType) {
    this(outboundMessageType, true);
  }
  
  protected ByteToMessageCodec(boolean preferDirect) {
    this.outboundMsgMatcher = TypeParameterMatcher.find(this, ByteToMessageCodec.class, "I");
    this.encoder = new Encoder(preferDirect);
  }
  
  protected ByteToMessageCodec(Class<? extends I> outboundMessageType, boolean preferDirect) {
    checkForSharableAnnotation();
    this.outboundMsgMatcher = TypeParameterMatcher.get(outboundMessageType);
    this.encoder = new Encoder(preferDirect);
  }
  
  private void checkForSharableAnnotation() {
    if (isSharable())
      throw new IllegalStateException("@Sharable annotation is not allowed"); 
  }
  
  public boolean acceptOutboundMessage(Object msg) throws Exception {
    return this.outboundMsgMatcher.match(msg);
  }
  
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    this.decoder.channelRead(ctx, msg);
  }
  
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    this.encoder.write(ctx, msg, promise);
  }
  
  protected abstract void encode(ChannelHandlerContext paramChannelHandlerContext, I paramI, ByteBuf paramByteBuf) throws Exception;
  
  protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList) throws Exception;
  
  protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    decode(ctx, in, out);
  }
  
  private final class Encoder extends MessageToByteEncoder<I> {
    Encoder(boolean preferDirect) {
      super(preferDirect);
    }
    
    public boolean acceptOutboundMessage(Object msg) throws Exception {
      return ByteToMessageCodec.this.acceptOutboundMessage(msg);
    }
    
    protected void encode(ChannelHandlerContext ctx, I msg, ByteBuf out) throws Exception {
      ByteToMessageCodec.this.encode(ctx, msg, out);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\ByteToMessageCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */