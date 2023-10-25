package io.netty.handler.codec.http;

import io.netty.util.internal.StringUtil;

public class DefaultHttpRequest extends DefaultHttpMessage implements HttpRequest {
  private HttpMethod method;
  
  private String uri;
  
  public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri) {
    this(httpVersion, method, uri, true);
  }
  
  public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, boolean validateHeaders) {
    super(httpVersion, validateHeaders);
    if (method == null)
      throw new NullPointerException("method"); 
    if (uri == null)
      throw new NullPointerException("uri"); 
    this.method = method;
    this.uri = uri;
  }
  
  public HttpMethod getMethod() {
    return this.method;
  }
  
  public String getUri() {
    return this.uri;
  }
  
  public HttpRequest setMethod(HttpMethod method) {
    if (method == null)
      throw new NullPointerException("method"); 
    this.method = method;
    return this;
  }
  
  public HttpRequest setUri(String uri) {
    if (uri == null)
      throw new NullPointerException("uri"); 
    this.uri = uri;
    return this;
  }
  
  public HttpRequest setProtocolVersion(HttpVersion version) {
    super.setProtocolVersion(version);
    return this;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(StringUtil.simpleClassName(this));
    buf.append("(decodeResult: ");
    buf.append(getDecoderResult());
    buf.append(')');
    buf.append(StringUtil.NEWLINE);
    buf.append(getMethod());
    buf.append(' ');
    buf.append(getUri());
    buf.append(' ');
    buf.append(getProtocolVersion().text());
    buf.append(StringUtil.NEWLINE);
    appendHeaders(buf);
    buf.setLength(buf.length() - StringUtil.NEWLINE.length());
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\DefaultHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */