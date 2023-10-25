package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Signal;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder {
  static final Signal REPLAY = Signal.valueOf(ReplayingDecoder.class.getName() + ".REPLAY");
  
  private final ReplayingDecoderBuffer replayable = new ReplayingDecoderBuffer();
  
  private S state;
  
  private int checkpoint = -1;
  
  protected ReplayingDecoder() {
    this((S)null);
  }
  
  protected ReplayingDecoder(S initialState) {
    this.state = initialState;
  }
  
  protected void checkpoint() {
    this.checkpoint = internalBuffer().readerIndex();
  }
  
  protected void checkpoint(S state) {
    checkpoint();
    state(state);
  }
  
  protected S state() {
    return this.state;
  }
  
  protected S state(S newState) {
    S oldState = this.state;
    this.state = newState;
    return oldState;
  }
  
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    RecyclableArrayList out = RecyclableArrayList.newInstance();
    try {
      this.replayable.terminate();
      callDecode(ctx, internalBuffer(), (List<Object>)out);
      decodeLast(ctx, this.replayable, (List<Object>)out);
    } catch (Signal replay) {
      replay.expect(REPLAY);
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
    this.replayable.setCumulation(in);
    try {
      while (in.isReadable()) {
        int oldReaderIndex = this.checkpoint = in.readerIndex();
        int outSize = out.size();
        S oldState = this.state;
        int oldInputLength = in.readableBytes();
        try {
          decode(ctx, this.replayable, out);
          if (ctx.isRemoved())
            break; 
          if (outSize == out.size()) {
            if (oldInputLength == in.readableBytes() && oldState == this.state)
              throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() must consume the inbound " + "data or change its state if it did not decode anything."); 
            continue;
          } 
        } catch (Signal replay) {
          replay.expect(REPLAY);
          if (ctx.isRemoved())
            break; 
          int checkpoint = this.checkpoint;
          if (checkpoint >= 0)
            in.readerIndex(checkpoint); 
          break;
        } 
        if (oldReaderIndex == in.readerIndex() && oldState == this.state)
          throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() method must consume the inbound data " + "or change its state if it decoded something."); 
        if (isSingleDecode())
          break; 
      } 
    } catch (DecoderException e) {
      throw e;
    } catch (Throwable cause) {
      throw new DecoderException(cause);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\ReplayingDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */