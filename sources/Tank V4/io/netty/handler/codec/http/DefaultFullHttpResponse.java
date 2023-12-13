package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;

public class DefaultFullHttpResponse extends DefaultHttpResponse implements FullHttpResponse {
   private final ByteBuf content;
   private final HttpHeaders trailingHeaders;
   private final boolean validateHeaders;

   public DefaultFullHttpResponse(HttpVersion var1, HttpResponseStatus var2) {
      this(var1, var2, Unpooled.buffer(0));
   }

   public DefaultFullHttpResponse(HttpVersion var1, HttpResponseStatus var2, ByteBuf var3) {
      this(var1, var2, var3, true);
   }

   public DefaultFullHttpResponse(HttpVersion var1, HttpResponseStatus var2, ByteBuf var3, boolean var4) {
      super(var1, var2, var4);
      if (var3 == null) {
         throw new NullPointerException("content");
      } else {
         this.content = var3;
         this.trailingHeaders = new DefaultHttpHeaders(var4);
         this.validateHeaders = var4;
      }
   }

   public HttpHeaders trailingHeaders() {
      return this.trailingHeaders;
   }

   public ByteBuf content() {
      return this.content;
   }

   public int refCnt() {
      return this.content.refCnt();
   }

   public FullHttpResponse retain() {
      this.content.retain();
      return this;
   }

   public FullHttpResponse retain(int var1) {
      this.content.retain(var1);
      return this;
   }

   public boolean release() {
      return this.content.release();
   }

   public boolean release(int var1) {
      return this.content.release(var1);
   }

   public FullHttpResponse setProtocolVersion(HttpVersion var1) {
      super.setProtocolVersion(var1);
      return this;
   }

   public FullHttpResponse setStatus(HttpResponseStatus var1) {
      super.setStatus(var1);
      return this;
   }

   public FullHttpResponse copy() {
      DefaultFullHttpResponse var1 = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().copy(), this.validateHeaders);
      var1.headers().set(this.headers());
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public FullHttpResponse duplicate() {
      DefaultFullHttpResponse var1 = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().duplicate(), this.validateHeaders);
      var1.headers().set(this.headers());
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public HttpResponse setProtocolVersion(HttpVersion var1) {
      return this.setProtocolVersion(var1);
   }

   public HttpResponse setStatus(HttpResponseStatus var1) {
      return this.setStatus(var1);
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
