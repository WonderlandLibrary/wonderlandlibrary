package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class HttpRequestEncoder extends HttpObjectEncoder {
   private static final char SLASH = '/';
   private static final byte[] CRLF = new byte[]{13, 10};

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return super.acceptOutboundMessage(var1) && !(var1 instanceof HttpResponse);
   }

   protected void encodeInitialLine(ByteBuf var1, HttpRequest var2) throws Exception {
      var2.getMethod().encode(var1);
      var1.writeByte(32);
      String var3 = var2.getUri();
      if (var3.length() == 0) {
         var3 = var3 + '/';
      } else {
         int var4 = var3.indexOf("://");
         if (var4 != -1 && var3.charAt(0) != '/') {
            int var5 = var4 + 3;
            if (var3.lastIndexOf(47) <= var5) {
               var3 = var3 + '/';
            }
         }
      }

      var1.writeBytes(var3.getBytes(CharsetUtil.UTF_8));
      var1.writeByte(32);
      var2.getProtocolVersion().encode(var1);
      var1.writeBytes(CRLF);
   }

   protected void encodeInitialLine(ByteBuf var1, HttpMessage var2) throws Exception {
      this.encodeInitialLine(var1, (HttpRequest)var2);
   }
}
