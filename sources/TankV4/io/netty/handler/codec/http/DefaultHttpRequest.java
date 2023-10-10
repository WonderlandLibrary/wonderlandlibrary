package io.netty.handler.codec.http;

import io.netty.util.internal.StringUtil;

public class DefaultHttpRequest extends DefaultHttpMessage implements HttpRequest {
   private HttpMethod method;
   private String uri;

   public DefaultHttpRequest(HttpVersion var1, HttpMethod var2, String var3) {
      this(var1, var2, var3, true);
   }

   public DefaultHttpRequest(HttpVersion var1, HttpMethod var2, String var3, boolean var4) {
      super(var1, var4);
      if (var2 == null) {
         throw new NullPointerException("method");
      } else if (var3 == null) {
         throw new NullPointerException("uri");
      } else {
         this.method = var2;
         this.uri = var3;
      }
   }

   public HttpMethod getMethod() {
      return this.method;
   }

   public String getUri() {
      return this.uri;
   }

   public HttpRequest setMethod(HttpMethod var1) {
      if (var1 == null) {
         throw new NullPointerException("method");
      } else {
         this.method = var1;
         return this;
      }
   }

   public HttpRequest setUri(String var1) {
      if (var1 == null) {
         throw new NullPointerException("uri");
      } else {
         this.uri = var1;
         return this;
      }
   }

   public HttpRequest setProtocolVersion(HttpVersion var1) {
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
      var1.append(this.getMethod().toString());
      var1.append(' ');
      var1.append(this.getUri());
      var1.append(' ');
      var1.append(this.getProtocolVersion().text());
      var1.append(StringUtil.NEWLINE);
      this.appendHeaders(var1);
      var1.setLength(var1.length() - StringUtil.NEWLINE.length());
      return var1.toString();
   }

   public HttpMessage setProtocolVersion(HttpVersion var1) {
      return this.setProtocolVersion(var1);
   }
}
