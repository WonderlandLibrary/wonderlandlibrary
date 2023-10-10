package io.netty.handler.codec.http;

import io.netty.util.internal.StringUtil;

public class DefaultHttpResponse extends DefaultHttpMessage implements HttpResponse {
   private HttpResponseStatus status;

   public DefaultHttpResponse(HttpVersion var1, HttpResponseStatus var2) {
      this(var1, var2, true);
   }

   public DefaultHttpResponse(HttpVersion var1, HttpResponseStatus var2, boolean var3) {
      super(var1, var3);
      if (var2 == null) {
         throw new NullPointerException("status");
      } else {
         this.status = var2;
      }
   }

   public HttpResponseStatus getStatus() {
      return this.status;
   }

   public HttpResponse setStatus(HttpResponseStatus var1) {
      if (var1 == null) {
         throw new NullPointerException("status");
      } else {
         this.status = var1;
         return this;
      }
   }

   public HttpResponse setProtocolVersion(HttpVersion var1) {
      super.setProtocolVersion(var1);
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append("(decodeResult: ");
      var1.append(this.getDecoderResult());
      var1.append(')');
      var1.append(StringUtil.NEWLINE);
      var1.append(this.getProtocolVersion().text());
      var1.append(' ');
      var1.append(this.getStatus().toString());
      var1.append(StringUtil.NEWLINE);
      this.appendHeaders(var1);
      var1.setLength(var1.length() - StringUtil.NEWLINE.length());
      return var1.toString();
   }

   public HttpMessage setProtocolVersion(HttpVersion var1) {
      return this.setProtocolVersion(var1);
   }
}
