package io.netty.handler.codec.http;

import io.netty.util.internal.StringUtil;

public class DefaultHttpResponse extends DefaultHttpMessage implements HttpResponse {
  private HttpResponseStatus status;
  
  public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status) {
    this(version, status, true);
  }
  
  public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders) {
    super(version, validateHeaders);
    if (status == null)
      throw new NullPointerException("status"); 
    this.status = status;
  }
  
  public HttpResponseStatus getStatus() {
    return this.status;
  }
  
  public HttpResponse setStatus(HttpResponseStatus status) {
    if (status == null)
      throw new NullPointerException("status"); 
    this.status = status;
    return this;
  }
  
  public HttpResponse setProtocolVersion(HttpVersion version) {
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
    buf.append(getProtocolVersion().text());
    buf.append(' ');
    buf.append(getStatus());
    buf.append(StringUtil.NEWLINE);
    appendHeaders(buf);
    buf.setLength(buf.length() - StringUtil.NEWLINE.length());
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\DefaultHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */