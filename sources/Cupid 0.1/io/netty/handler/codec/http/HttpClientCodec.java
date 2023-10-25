package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.PrematureChannelClosureException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public final class HttpClientCodec extends CombinedChannelDuplexHandler<HttpResponseDecoder, HttpRequestEncoder> {
  private final Queue<HttpMethod> queue = new ArrayDeque<HttpMethod>();
  
  private boolean done;
  
  private final AtomicLong requestResponseCounter = new AtomicLong();
  
  private final boolean failOnMissingResponse;
  
  public HttpClientCodec() {
    this(4096, 8192, 8192, false);
  }
  
  public void setSingleDecode(boolean singleDecode) {
    ((HttpResponseDecoder)inboundHandler()).setSingleDecode(singleDecode);
  }
  
  public boolean isSingleDecode() {
    return ((HttpResponseDecoder)inboundHandler()).isSingleDecode();
  }
  
  public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
    this(maxInitialLineLength, maxHeaderSize, maxChunkSize, false);
  }
  
  public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse) {
    this(maxInitialLineLength, maxHeaderSize, maxChunkSize, failOnMissingResponse, true);
  }
  
  public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders) {
    init((ChannelInboundHandler)new Decoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders), (ChannelOutboundHandler)new Encoder());
    this.failOnMissingResponse = failOnMissingResponse;
  }
  
  private final class Encoder extends HttpRequestEncoder {
    private Encoder() {}
    
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
      if (msg instanceof HttpRequest && !HttpClientCodec.this.done)
        HttpClientCodec.this.queue.offer(((HttpRequest)msg).getMethod()); 
      super.encode(ctx, msg, out);
      if (HttpClientCodec.this.failOnMissingResponse)
        if (msg instanceof LastHttpContent)
          HttpClientCodec.this.requestResponseCounter.incrementAndGet();  
    }
  }
  
  private final class Decoder extends HttpResponseDecoder {
    Decoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders) {
      super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders);
    }
    
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
      if (HttpClientCodec.this.done) {
        int readable = actualReadableBytes();
        if (readable == 0)
          return; 
        out.add(buffer.readBytes(readable));
      } else {
        int oldSize = out.size();
        super.decode(ctx, buffer, out);
        if (HttpClientCodec.this.failOnMissingResponse) {
          int size = out.size();
          for (int i = oldSize; i < size; i++)
            decrement(out.get(i)); 
        } 
      } 
    }
    
    private void decrement(Object msg) {
      if (msg == null)
        return; 
      if (msg instanceof LastHttpContent)
        HttpClientCodec.this.requestResponseCounter.decrementAndGet(); 
    }
    
    protected boolean isContentAlwaysEmpty(HttpMessage msg) {
      int statusCode = ((HttpResponse)msg).getStatus().code();
      if (statusCode == 100)
        return true; 
      HttpMethod method = HttpClientCodec.this.queue.poll();
      char firstChar = method.name().charAt(0);
      switch (firstChar) {
        case 'H':
          if (HttpMethod.HEAD.equals(method))
            return true; 
          break;
        case 'C':
          if (statusCode == 200 && 
            HttpMethod.CONNECT.equals(method)) {
            HttpClientCodec.this.done = true;
            HttpClientCodec.this.queue.clear();
            return true;
          } 
          break;
      } 
      return super.isContentAlwaysEmpty(msg);
    }
    
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      super.channelInactive(ctx);
      if (HttpClientCodec.this.failOnMissingResponse) {
        long missingResponses = HttpClientCodec.this.requestResponseCounter.get();
        if (missingResponses > 0L)
          ctx.fireExceptionCaught((Throwable)new PrematureChannelClosureException("channel gone inactive with " + missingResponses + " missing response(s)")); 
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\HttpClientCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */