package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class ByteToMessageDecoder extends ChannelInboundHandlerAdapter {
  ByteBuf cumulation;
  
  private boolean singleDecode;
  
  private boolean decodeWasNull;
  
  private boolean first;
  
  protected ByteToMessageDecoder() {
    if (isSharable())
      throw new IllegalStateException("@Sharable annotation is not allowed"); 
  }
  
  public void setSingleDecode(boolean singleDecode) {
    this.singleDecode = singleDecode;
  }
  
  public boolean isSingleDecode() {
    return this.singleDecode;
  }
  
  protected int actualReadableBytes() {
    return internalBuffer().readableBytes();
  }
  
  protected ByteBuf internalBuffer() {
    if (this.cumulation != null)
      return this.cumulation; 
    return Unpooled.EMPTY_BUFFER;
  }
  
  public final void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    ByteBuf buf = internalBuffer();
    int readable = buf.readableBytes();
    if (buf.isReadable()) {
      ByteBuf bytes = buf.readBytes(readable);
      buf.release();
      ctx.fireChannelRead(bytes);
    } else {
      buf.release();
    } 
    this.cumulation = null;
    ctx.fireChannelReadComplete();
    handlerRemoved0(ctx);
  }
  
  protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {}
  
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof ByteBuf) {
      RecyclableArrayList out = RecyclableArrayList.newInstance();
      try {
        ByteBuf data = (ByteBuf)msg;
        this.first = (this.cumulation == null);
        if (this.first) {
          this.cumulation = data;
        } else {
          if (this.cumulation.writerIndex() > this.cumulation.maxCapacity() - data.readableBytes() || this.cumulation.refCnt() > 1)
            expandCumulation(ctx, data.readableBytes()); 
          this.cumulation.writeBytes(data);
          data.release();
        } 
        callDecode(ctx, this.cumulation, (List<Object>)out);
      } catch (DecoderException e) {
        throw e;
      } catch (Throwable t) {
        throw new DecoderException(t);
      } finally {
        if (this.cumulation != null && !this.cumulation.isReadable()) {
          this.cumulation.release();
          this.cumulation = null;
        } 
        int size = out.size();
        this.decodeWasNull = (size == 0);
        for (int i = 0; i < size; i++)
          ctx.fireChannelRead(out.get(i)); 
        out.recycle();
      } 
    } else {
      ctx.fireChannelRead(msg);
    } 
  }
  
  private void expandCumulation(ChannelHandlerContext ctx, int readable) {
    ByteBuf oldCumulation = this.cumulation;
    this.cumulation = ctx.alloc().buffer(oldCumulation.readableBytes() + readable);
    this.cumulation.writeBytes(oldCumulation);
    oldCumulation.release();
  }
  
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    if (this.cumulation != null && !this.first && this.cumulation.refCnt() == 1)
      this.cumulation.discardSomeReadBytes(); 
    if (this.decodeWasNull) {
      this.decodeWasNull = false;
      if (!ctx.channel().config().isAutoRead())
        ctx.read(); 
    } 
    ctx.fireChannelReadComplete();
  }
  
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    RecyclableArrayList out = RecyclableArrayList.newInstance();
    try {
      if (this.cumulation != null) {
        callDecode(ctx, this.cumulation, (List<Object>)out);
        decodeLast(ctx, this.cumulation, (List<Object>)out);
      } else {
        decodeLast(ctx, Unpooled.EMPTY_BUFFER, (List<Object>)out);
      } 
    } catch (DecoderException e) {
      throw e;
    } catch (Exception e) {
      throw new DecoderException(e);
    } finally {
      try {
        if (this.cumulation != null) {
          this.cumulation.release();
          this.cumulation = null;
        } 
        int size = out.size();
        for (int i = 0; i < size; i++)
          ctx.fireChannelRead(out.get(i)); 
        if (size > 0)
          ctx.fireChannelReadComplete(); 
        ctx.fireChannelInactive();
      } finally {
        out.recycle();
      } 
    } 
  }
  
  protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
    try {
      while (in.isReadable()) {
        int outSize = out.size();
        int oldInputLength = in.readableBytes();
        decode(ctx, in, out);
        if (ctx.isRemoved())
          break; 
        if (outSize == out.size()) {
          if (oldInputLength == in.readableBytes())
            break; 
          continue;
        } 
        if (oldInputLength == in.readableBytes())
          throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() did not read anything but decoded a message."); 
        if (isSingleDecode())
          break; 
      } 
    } catch (DecoderException e) {
      throw e;
    } catch (Throwable cause) {
      throw new DecoderException(cause);
    } 
  }
  
  protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList) throws Exception;
  
  protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    decode(ctx, in, out);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\ByteToMessageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */