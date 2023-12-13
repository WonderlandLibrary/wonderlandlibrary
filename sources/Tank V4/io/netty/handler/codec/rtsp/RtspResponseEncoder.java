package io.netty.handler.codec.rtsp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;

public class RtspResponseEncoder extends RtspObjectEncoder {
   private static final byte[] CRLF = new byte[]{13, 10};

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return var1 instanceof FullHttpResponse;
   }

   protected void encodeInitialLine(ByteBuf var1, HttpResponse var2) throws Exception {
      encodeAscii(var2.getProtocolVersion().toString(), var1);
      var1.writeByte(32);
      var1.writeBytes(String.valueOf(var2.getStatus().code()).getBytes(CharsetUtil.US_ASCII));
      var1.writeByte(32);
      encodeAscii(String.valueOf(var2.getStatus().reasonPhrase()), var1);
      var1.writeBytes(CRLF);
   }

   protected void encodeInitialLine(ByteBuf var1, HttpMessage var2) throws Exception {
      this.encodeInitialLine(var1, (HttpResponse)var2);
   }
}
