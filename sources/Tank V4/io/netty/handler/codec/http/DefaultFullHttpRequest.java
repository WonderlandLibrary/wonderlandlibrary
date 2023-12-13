package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;

public class DefaultFullHttpRequest extends DefaultHttpRequest implements FullHttpRequest {
   private final ByteBuf content;
   private final HttpHeaders trailingHeader;
   private final boolean validateHeaders;

   public DefaultFullHttpRequest(HttpVersion var1, HttpMethod var2, String var3) {
      this(var1, var2, var3, Unpooled.buffer(0));
   }

   public DefaultFullHttpRequest(HttpVersion var1, HttpMethod var2, String var3, ByteBuf var4) {
      this(var1, var2, var3, var4, true);
   }

   public DefaultFullHttpRequest(HttpVersion var1, HttpMethod var2, String var3, ByteBuf var4, boolean var5) {
      super(var1, var2, var3, var5);
      if (var4 == null) {
         throw new NullPointerException("content");
      } else {
         this.content = var4;
         this.trailingHeader = new DefaultHttpHeaders(var5);
         this.validateHeaders = var5;
      }
   }

   public HttpHeaders trailingHeaders() {
      return this.trailingHeader;
   }

   public ByteBuf content() {
      return this.content;
   }

   public int refCnt() {
      return this.content.refCnt();
   }

   public FullHttpRequest retain() {
      this.content.retain();
      return this;
   }

   public FullHttpRequest retain(int var1) {
      this.content.retain(var1);
      return this;
   }

   public boolean release() {
      return this.content.release();
   }

   public boolean release(int var1) {
      return this.content.release(var1);
   }

   public FullHttpRequest setProtocolVersion(HttpVersion var1) {
      super.setProtocolVersion(var1);
      return this;
   }

   public FullHttpRequest setMethod(HttpMethod var1) {
      super.setMethod(var1);
      return this;
   }

   public FullHttpRequest setUri(String var1) {
      super.setUri(var1);
      return this;
   }

   public FullHttpRequest copy() {
      DefaultFullHttpRequest var1 = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().copy(), this.validateHeaders);
      var1.headers().set(this.headers());
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public FullHttpRequest duplicate() {
      DefaultFullHttpRequest var1 = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().duplicate(), this.validateHeaders);
      var1.headers().set(this.headers());
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public HttpRequest setProtocolVersion(HttpVersion var1) {
      return this.setProtocolVersion(var1);
   }

   public HttpRequest setUri(String var1) {
      return this.setUri(var1);
   }

   public HttpRequest setMethod(HttpMethod var1) {
      return this.setMethod(var1);
   }

   public HttpMessage setProtocolVersion(HttpVersion var1) {
      return this.setProtocolVersion(var1);
   }

   public FullHttpMessage retain() {
      return this.retain();
   }

   public FullHttpMessage retain(int var1) {
      return this.retain(var1);
   }

   public FullHttpMessage copy() {
      return this.copy();
   }

   public LastHttpContent retain() {
      return this.retain();
   }

   public LastHttpContent retain(int var1) {
      return this.retain(var1);
   }

   public LastHttpContent copy() {
      return this.copy();
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
}
