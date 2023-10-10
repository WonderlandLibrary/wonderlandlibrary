package io.netty.handler.codec.rtsp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

public class RtspRequestEncoder extends RtspObjectEncoder {
   private static final byte[] CRLF = new byte[]{13, 10};

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return var1 instanceof FullHttpRequest;
   }

   protected void encodeInitialLine(ByteBuf var1, HttpRequest var2) throws Exception {
      encodeAscii(var2.getMethod().toString(), var1);
      var1.writeByte(32);
      var1.writeBytes(var2.getUri().getBytes(CharsetUtil.UTF_8));
      var1.writeByte(32);
      encodeAscii(var2.getProtocolVersion().toString(), var1);
      var1.writeBytes(CRLF);
   }

   protected void encodeInitialLine(ByteBuf var1, HttpMessage var2) throws Exception {
      this.encodeInitialLine(var1, (HttpRequest)var2);
   }
}
