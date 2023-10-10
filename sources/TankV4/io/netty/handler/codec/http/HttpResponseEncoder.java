package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public class HttpResponseEncoder extends HttpObjectEncoder {
   private static final byte[] CRLF = new byte[]{13, 10};

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return super.acceptOutboundMessage(var1) && !(var1 instanceof HttpRequest);
   }

   protected void encodeInitialLine(ByteBuf var1, HttpResponse var2) throws Exception {
      var2.getProtocolVersion().encode(var1);
      var1.writeByte(32);
      var2.getStatus().encode(var1);
      var1.writeBytes(CRLF);
   }

   protected void encodeInitialLine(ByteBuf var1, HttpMessage var2) throws Exception {
      this.encodeInitialLine(var1, (HttpResponse)var2);
   }
}
