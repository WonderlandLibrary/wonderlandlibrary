package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.util.ReferenceCounted;

final class ComposedLastHttpContent implements LastHttpContent {
  private final HttpHeaders trailingHeaders;
  
  private DecoderResult result;
  
  ComposedLastHttpContent(HttpHeaders trailingHeaders) {
    this.trailingHeaders = trailingHeaders;
  }
  
  public HttpHeaders trailingHeaders() {
    return this.trailingHeaders;
  }
  
  public LastHttpContent copy() {
    LastHttpContent content = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
    content.trailingHeaders().set(trailingHeaders());
    return content;
  }
  
  public LastHttpContent retain(int increment) {
    return this;
  }
  
  public LastHttpContent retain() {
    return this;
  }
  
  public HttpContent duplicate() {
    return copy();
  }
  
  public ByteBuf content() {
    return Unpooled.EMPTY_BUFFER;
  }
  
  public DecoderResult getDecoderResult() {
    return this.result;
  }
  
  public void setDecoderResult(DecoderResult result) {
    this.result = result;
  }
  
  public int refCnt() {
    return 1;
  }
  
  public boolean release() {
    return false;
  }
  
  public boolean release(int decrement) {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\ComposedLastHttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */