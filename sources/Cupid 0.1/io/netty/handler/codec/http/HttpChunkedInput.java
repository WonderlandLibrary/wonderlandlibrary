package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;

public class HttpChunkedInput implements ChunkedInput<HttpContent> {
  private final ChunkedInput<ByteBuf> input;
  
  private final LastHttpContent lastHttpContent;
  
  private boolean sentLastChunk;
  
  public HttpChunkedInput(ChunkedInput<ByteBuf> input) {
    this.input = input;
    this.lastHttpContent = LastHttpContent.EMPTY_LAST_CONTENT;
  }
  
  public HttpChunkedInput(ChunkedInput<ByteBuf> input, LastHttpContent lastHttpContent) {
    this.input = input;
    this.lastHttpContent = lastHttpContent;
  }
  
  public boolean isEndOfInput() throws Exception {
    if (this.input.isEndOfInput())
      return this.sentLastChunk; 
    return false;
  }
  
  public void close() throws Exception {
    this.input.close();
  }
  
  public HttpContent readChunk(ChannelHandlerContext ctx) throws Exception {
    if (this.input.isEndOfInput()) {
      if (this.sentLastChunk)
        return null; 
      this.sentLastChunk = true;
      return this.lastHttpContent;
    } 
    ByteBuf buf = (ByteBuf)this.input.readChunk(ctx);
    return new DefaultHttpContent(buf);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\HttpChunkedInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */