package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class HttpRequestEncoder extends HttpObjectEncoder<HttpRequest> {
  private static final char SLASH = '/';
  
  private static final char QUESTION_MARK = '?';
  
  private static final byte[] CRLF = new byte[] { 13, 10 };
  
  public boolean acceptOutboundMessage(Object msg) throws Exception {
    return (super.acceptOutboundMessage(msg) && !(msg instanceof HttpResponse));
  }
  
  protected void encodeInitialLine(ByteBuf buf, HttpRequest request) throws Exception {
    request.getMethod().encode(buf);
    buf.writeByte(32);
    String uri = request.getUri();
    if (uri.length() == 0) {
      uri = uri + '/';
    } else {
      int start = uri.indexOf("://");
      if (start != -1 && uri.charAt(0) != '/') {
        int startIndex = start + 3;
        int index = uri.indexOf('?', startIndex);
        if (index == -1) {
          if (uri.lastIndexOf('/') <= startIndex)
            uri = uri + '/'; 
        } else if (uri.lastIndexOf('/', index) <= startIndex) {
          int len = uri.length();
          StringBuilder sb = new StringBuilder(len + 1);
          sb.append(uri, 0, index);
          sb.append('/');
          sb.append(uri, index, len);
          uri = sb.toString();
        } 
      } 
    } 
    buf.writeBytes(uri.getBytes(CharsetUtil.UTF_8));
    buf.writeByte(32);
    request.getProtocolVersion().encode(buf);
    buf.writeBytes(CRLF);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\HttpRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */