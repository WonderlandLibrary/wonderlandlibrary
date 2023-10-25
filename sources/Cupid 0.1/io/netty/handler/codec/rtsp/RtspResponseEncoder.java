package io.netty.handler.codec.rtsp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;

public class RtspResponseEncoder extends RtspObjectEncoder<HttpResponse> {
  private static final byte[] CRLF = new byte[] { 13, 10 };
  
  public boolean acceptOutboundMessage(Object msg) throws Exception {
    return msg instanceof io.netty.handler.codec.http.FullHttpResponse;
  }
  
  protected void encodeInitialLine(ByteBuf buf, HttpResponse response) throws Exception {
    HttpHeaders.encodeAscii(response.getProtocolVersion().toString(), buf);
    buf.writeByte(32);
    buf.writeBytes(String.valueOf(response.getStatus().code()).getBytes(CharsetUtil.US_ASCII));
    buf.writeByte(32);
    encodeAscii(String.valueOf(response.getStatus().reasonPhrase()), buf);
    buf.writeBytes(CRLF);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\rtsp\RtspResponseEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */