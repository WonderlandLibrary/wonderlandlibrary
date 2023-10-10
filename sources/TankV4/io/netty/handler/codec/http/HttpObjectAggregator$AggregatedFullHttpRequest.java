package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;

final class HttpObjectAggregator$AggregatedFullHttpRequest extends HttpObjectAggregator$AggregatedFullHttpMessage implements FullHttpRequest {
   private HttpObjectAggregator$AggregatedFullHttpRequest(HttpRequest var1, ByteBuf var2, HttpHeaders var3) {
      super(var1, var2, var3, null);
   }

   public FullHttpRequest copy() {
      DefaultFullHttpRequest var1 = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().copy());
      var1.headers().set(this.headers());
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public FullHttpRequest duplicate() {
      DefaultFullHttpRequest var1 = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().duplicate());
      var1.headers().set(this.headers());
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public FullHttpRequest retain(int var1) {
      super.retain(var1);
      return this;
   }

   public FullHttpRequest retain() {
      super.retain();
      return this;
   }

   public FullHttpRequest setMethod(HttpMethod var1) {
      ((HttpRequest)this.message).setMethod(var1);
      return this;
   }

   public FullHttpRequest setUri(String var1) {
      ((HttpRequest)this.message).setUri(var1);
      return this;
   }

   public HttpMethod getMethod() {
      return ((HttpRequest)this.message).getMethod();
   }

   public String getUri() {
      return ((HttpRequest)this.message).getUri();
   }

   public FullHttpRequest setProtocolVersion(HttpVersion var1) {
      super.setProtocolVersion(var1);
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

   public HttpRequest setProtocolVersion(HttpVersion var1) {
      return this.setProtocolVersion(var1);
   }

   public HttpRequest setUri(String var1) {
      return this.setUri(var1);
   }

   public HttpRequest setMethod(HttpMethod var1) {
      return this.setMethod(var1);
   }

   HttpObjectAggregator$AggregatedFullHttpRequest(HttpRequest var1, ByteBuf var2, HttpHeaders var3, Object var4) {
      this(var1, var2, var3);
   }
}
