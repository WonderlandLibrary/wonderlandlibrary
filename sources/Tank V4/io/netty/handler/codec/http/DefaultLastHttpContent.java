package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import java.util.Map.Entry;

public class DefaultLastHttpContent extends DefaultHttpContent implements LastHttpContent {
   private final HttpHeaders trailingHeaders;
   private final boolean validateHeaders;

   public DefaultLastHttpContent() {
      this(Unpooled.buffer(0));
   }

   public DefaultLastHttpContent(ByteBuf var1) {
      this(var1, true);
   }

   public DefaultLastHttpContent(ByteBuf var1, boolean var2) {
      super(var1);
      this.trailingHeaders = new DefaultLastHttpContent.TrailingHeaders(var2);
      this.validateHeaders = var2;
   }

   public LastHttpContent copy() {
      DefaultLastHttpContent var1 = new DefaultLastHttpContent(this.content().copy(), this.validateHeaders);
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public LastHttpContent duplicate() {
      DefaultLastHttpContent var1 = new DefaultLastHttpContent(this.content().duplicate(), this.validateHeaders);
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public LastHttpContent retain(int var1) {
      super.retain(var1);
      return this;
   }

   public LastHttpContent retain() {
      super.retain();
      return this;
   }

   public HttpHeaders trailingHeaders() {
      return this.trailingHeaders;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(super.toString());
      var1.append(StringUtil.NEWLINE);
      this.appendHeaders(var1);
      var1.setLength(var1.length() - StringUtil.NEWLINE.length());
      return var1.toString();
   }

   private void appendHeaders(StringBuilder var1) {
      Iterator var2 = this.trailingHeaders().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append((String)var3.getKey());
         var1.append(": ");
         var1.append((String)var3.getValue());
         var1.append(StringUtil.NEWLINE);
      }

   }

   public HttpContent retain(int var1) {
      return this.retain(var1);
   }

   public HttpContent retain() {
      return this.retain();
   }

   public HttpContent duplicate() {
      return this.duplicate();
   }

   public HttpContent copy() {
      return this.copy();
   }

   public ByteBufHolder retain(int var1) {
      return this.retain(var1);
   }

   public ByteBufHolder retain() {
      return this.retain();
   }

   public ByteBufHolder duplicate() {
      return this.duplicate();
   }

   public ByteBufHolder copy() {
      return this.copy();
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }

   private static final class TrailingHeaders extends DefaultHttpHeaders {
      TrailingHeaders(boolean var1) {
         super(var1);
      }

      void validateHeaderName0(CharSequence var1) {
         super.validateHeaderName0(var1);
         if (HttpHeaders.equalsIgnoreCase("Content-Length", var1) || HttpHeaders.equalsIgnoreCase("Transfer-Encoding", var1) || HttpHeaders.equalsIgnoreCase("Trailer", var1)) {
            throw new IllegalArgumentException("prohibited trailing header: " + var1);
         }
      }
   }
}
