package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.util.Map;

public class DefaultLastHttpContent extends DefaultHttpContent implements LastHttpContent {
  private final HttpHeaders trailingHeaders;
  
  private final boolean validateHeaders;
  
  public DefaultLastHttpContent() {
    this(Unpooled.buffer(0));
  }
  
  public DefaultLastHttpContent(ByteBuf content) {
    this(content, true);
  }
  
  public DefaultLastHttpContent(ByteBuf content, boolean validateHeaders) {
    super(content);
    this.trailingHeaders = new TrailingHeaders(validateHeaders);
    this.validateHeaders = validateHeaders;
  }
  
  public LastHttpContent copy() {
    DefaultLastHttpContent copy = new DefaultLastHttpContent(content().copy(), this.validateHeaders);
    copy.trailingHeaders().set(trailingHeaders());
    return copy;
  }
  
  public LastHttpContent duplicate() {
    DefaultLastHttpContent copy = new DefaultLastHttpContent(content().duplicate(), this.validateHeaders);
    copy.trailingHeaders().set(trailingHeaders());
    return copy;
  }
  
  public LastHttpContent retain(int increment) {
    super.retain(increment);
    return this;
  }
  
  public LastHttpContent retain() {
    super.retain();
    return this;
  }
  
  public HttpHeaders trailingHeaders() {
    return this.trailingHeaders;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder(super.toString());
    buf.append(StringUtil.NEWLINE);
    appendHeaders(buf);
    buf.setLength(buf.length() - StringUtil.NEWLINE.length());
    return buf.toString();
  }
  
  private void appendHeaders(StringBuilder buf) {
    for (Map.Entry<String, String> e : (Iterable<Map.Entry<String, String>>)trailingHeaders()) {
      buf.append(e.getKey());
      buf.append(": ");
      buf.append(e.getValue());
      buf.append(StringUtil.NEWLINE);
    } 
  }
  
  private static final class TrailingHeaders extends DefaultHttpHeaders {
    TrailingHeaders(boolean validate) {
      super(validate);
    }
    
    void validateHeaderName0(CharSequence name) {
      super.validateHeaderName0(name);
      if (HttpHeaders.equalsIgnoreCase("Content-Length", name) || HttpHeaders.equalsIgnoreCase("Transfer-Encoding", name) || HttpHeaders.equalsIgnoreCase("Trailer", name))
        throw new IllegalArgumentException("prohibited trailing header: " + name); 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\DefaultLastHttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */