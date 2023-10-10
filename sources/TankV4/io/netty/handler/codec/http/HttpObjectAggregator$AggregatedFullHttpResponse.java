package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;

final class HttpObjectAggregator$AggregatedFullHttpResponse extends HttpObjectAggregator$AggregatedFullHttpMessage implements FullHttpResponse {
   private HttpObjectAggregator$AggregatedFullHttpResponse(HttpResponse var1, ByteBuf var2, HttpHeaders var3) {
      super(var1, var2, var3, null);
   }

   public FullHttpResponse copy() {
      DefaultFullHttpResponse var1 = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().copy());
      var1.headers().set(this.headers());
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public FullHttpResponse duplicate() {
      DefaultFullHttpResponse var1 = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().duplicate());
      var1.headers().set(this.headers());
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public FullHttpResponse setStatus(HttpResponseStatus var1) {
      ((HttpResponse)this.message).setStatus(var1);
      return this;
   }

   public HttpResponseStatus getStatus() {
      return ((HttpResponse)this.message).getStatus();
   }

   public FullHttpResponse setProtocolVersion(HttpVersion var1) {
      super.setProtocolVersion(var1);
      return this;
   }

   public FullHttpResponse retain(int var1) {
      super.retain(var1);
      return this;
   }

   public FullHttpResponse retain() {
      super.retain();
      return this;
   }

   public FullHttpMessage duplicate() {
      return this.duplicate();
   }

   public FullHttpMessage copy() {
      return this.copy();
   }

   public FullHttpMessage retain() {
      return this.retain();
   }

   public FullHttpMessage retain(int var1) {
      return this.retain(var1);
   }

   public FullHttpMessage setProtocolVersion(HttpVersion var1) {
      return this.setProtocolVersion(var1);
   }

   public HttpMessage setProtocolVersion(HttpVersion var1) {
      return this.setProtocolVersion(var1);
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

   public HttpResponse setProtocolVersion(HttpVersion var1) {
      return this.setProtocolVersion(var1);
   }

   public HttpResponse setStatus(HttpResponseStatus var1) {
      return this.setStatus(var1);
   }

   HttpObjectAggregator$AggregatedFullHttpResponse(HttpResponse var1, ByteBuf var2, HttpHeaders var3, Object var4) {
      this(var1, var2, var3);
   }
}
