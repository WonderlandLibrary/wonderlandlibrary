package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public abstract class HttpContentEncoder extends MessageToMessageCodec<HttpRequest, HttpObject> {
  private enum State {
    PASS_THROUGH, AWAIT_HEADERS, AWAIT_CONTENT;
  }
  
  private final Queue<String> acceptEncodingQueue = new ArrayDeque<String>();
  
  private String acceptEncoding;
  
  private EmbeddedChannel encoder;
  
  private State state = State.AWAIT_HEADERS;
  
  public boolean acceptOutboundMessage(Object msg) throws Exception {
    return (msg instanceof HttpContent || msg instanceof HttpResponse);
  }
  
  protected void decode(ChannelHandlerContext ctx, HttpRequest msg, List<Object> out) throws Exception {
    String acceptedEncoding = msg.headers().get("Accept-Encoding");
    if (acceptedEncoding == null)
      acceptedEncoding = "identity"; 
    this.acceptEncodingQueue.add(acceptedEncoding);
    out.add(ReferenceCountUtil.retain(msg));
  }
  
  protected void encode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
    HttpResponse res;
    Result result;
    boolean isFull = (msg instanceof HttpResponse && msg instanceof LastHttpContent);
    switch (this.state) {
      case AWAIT_HEADERS:
        ensureHeaders(msg);
        assert this.encoder == null;
        res = (HttpResponse)msg;
        if (res.getStatus().code() == 100) {
          if (isFull) {
            out.add(ReferenceCountUtil.retain(res));
            break;
          } 
          out.add(res);
          this.state = State.PASS_THROUGH;
          break;
        } 
        this.acceptEncoding = this.acceptEncodingQueue.poll();
        if (this.acceptEncoding == null)
          throw new IllegalStateException("cannot send more responses than requests"); 
        if (isFull)
          if (!((ByteBufHolder)res).content().isReadable()) {
            out.add(ReferenceCountUtil.retain(res));
            break;
          }  
        result = beginEncode(res, this.acceptEncoding);
        if (result == null) {
          if (isFull) {
            out.add(ReferenceCountUtil.retain(res));
            break;
          } 
          out.add(res);
          this.state = State.PASS_THROUGH;
          break;
        } 
        this.encoder = result.contentEncoder();
        res.headers().set("Content-Encoding", result.targetContentEncoding());
        res.headers().remove("Content-Length");
        res.headers().set("Transfer-Encoding", "chunked");
        if (isFull) {
          HttpResponse newRes = new DefaultHttpResponse(res.getProtocolVersion(), res.getStatus());
          newRes.headers().set(res.headers());
          out.add(newRes);
        } else {
          out.add(res);
          this.state = State.AWAIT_CONTENT;
          if (!(msg instanceof HttpContent))
            break; 
        } 
      case AWAIT_CONTENT:
        ensureContent(msg);
        if (encodeContent((HttpContent)msg, out))
          this.state = State.AWAIT_HEADERS; 
        break;
      case PASS_THROUGH:
        ensureContent(msg);
        out.add(ReferenceCountUtil.retain(msg));
        if (msg instanceof LastHttpContent)
          this.state = State.AWAIT_HEADERS; 
        break;
    } 
  }
  
  private static void ensureHeaders(HttpObject msg) {
    if (!(msg instanceof HttpResponse))
      throw new IllegalStateException("unexpected message type: " + msg.getClass().getName() + " (expected: " + HttpResponse.class.getSimpleName() + ')'); 
  }
  
  private static void ensureContent(HttpObject msg) {
    if (!(msg instanceof HttpContent))
      throw new IllegalStateException("unexpected message type: " + msg.getClass().getName() + " (expected: " + HttpContent.class.getSimpleName() + ')'); 
  }
  
  private boolean encodeContent(HttpContent c, List<Object> out) {
    ByteBuf content = c.content();
    encode(content, out);
    if (c instanceof LastHttpContent) {
      finishEncode(out);
      LastHttpContent last = (LastHttpContent)c;
      HttpHeaders headers = last.trailingHeaders();
      if (headers.isEmpty()) {
        out.add(LastHttpContent.EMPTY_LAST_CONTENT);
      } else {
        out.add(new ComposedLastHttpContent(headers));
      } 
      return true;
    } 
    return false;
  }
  
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    cleanup();
    super.handlerRemoved(ctx);
  }
  
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    cleanup();
    super.channelInactive(ctx);
  }
  
  private void cleanup() {
    if (this.encoder != null) {
      if (this.encoder.finish())
        while (true) {
          ByteBuf buf = (ByteBuf)this.encoder.readOutbound();
          if (buf == null)
            break; 
          buf.release();
        }  
      this.encoder = null;
    } 
  }
  
  private void encode(ByteBuf in, List<Object> out) {
    this.encoder.writeOutbound(new Object[] { in.retain() });
    fetchEncoderOutput(out);
  }
  
  private void finishEncode(List<Object> out) {
    if (this.encoder.finish())
      fetchEncoderOutput(out); 
    this.encoder = null;
  }
  
  private void fetchEncoderOutput(List<Object> out) {
    while (true) {
      ByteBuf buf = (ByteBuf)this.encoder.readOutbound();
      if (buf == null)
        break; 
      if (!buf.isReadable()) {
        buf.release();
        continue;
      } 
      out.add(new DefaultHttpContent(buf));
    } 
  }
  
  protected abstract Result beginEncode(HttpResponse paramHttpResponse, String paramString) throws Exception;
  
  public static final class Result {
    private final String targetContentEncoding;
    
    private final EmbeddedChannel contentEncoder;
    
    public Result(String targetContentEncoding, EmbeddedChannel contentEncoder) {
      if (targetContentEncoding == null)
        throw new NullPointerException("targetContentEncoding"); 
      if (contentEncoder == null)
        throw new NullPointerException("contentEncoder"); 
      this.targetContentEncoding = targetContentEncoding;
      this.contentEncoder = contentEncoder;
    }
    
    public String targetContentEncoding() {
      return this.targetContentEncoding;
    }
    
    public EmbeddedChannel contentEncoder() {
      return this.contentEncoder;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\HttpContentEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */