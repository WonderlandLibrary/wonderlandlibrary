package io.netty.handler.codec.http;

import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class DefaultHttpMessage extends DefaultHttpObject implements HttpMessage {
   private HttpVersion version;
   private final HttpHeaders headers;

   protected DefaultHttpMessage(HttpVersion var1) {
      this(var1, true);
   }

   protected DefaultHttpMessage(HttpVersion var1, boolean var2) {
      if (var1 == null) {
         throw new NullPointerException("version");
      } else {
         this.version = var1;
         this.headers = new DefaultHttpHeaders(var2);
      }
   }

   public HttpHeaders headers() {
      return this.headers;
   }

   public HttpVersion getProtocolVersion() {
      return this.version;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append("(version: ");
      var1.append(this.getProtocolVersion().text());
      var1.append(", keepAlive: ");
      var1.append(HttpHeaders.isKeepAlive(this));
      var1.append(')');
      var1.append(StringUtil.NEWLINE);
      this.appendHeaders(var1);
      var1.setLength(var1.length() - StringUtil.NEWLINE.length());
      return var1.toString();
   }

   public HttpMessage setProtocolVersion(HttpVersion var1) {
      if (var1 == null) {
         throw new NullPointerException("version");
      } else {
         this.version = var1;
         return this;
      }
   }

   void appendHeaders(StringBuilder var1) {
      Iterator var2 = this.headers().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append((String)var3.getKey());
         var1.append(": ");
         var1.append((String)var3.getValue());
         var1.append(StringUtil.NEWLINE);
      }

   }
}
