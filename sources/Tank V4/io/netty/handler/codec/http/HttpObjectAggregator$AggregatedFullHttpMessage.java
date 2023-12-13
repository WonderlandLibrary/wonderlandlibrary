package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.handler.codec.DecoderResult;
import io.netty.util.ReferenceCounted;

abstract class HttpObjectAggregator$AggregatedFullHttpMessage extends DefaultByteBufHolder implements FullHttpMessage {
   protected final HttpMessage message;
   private HttpHeaders trailingHeaders;

   private HttpObjectAggregator$AggregatedFullHttpMessage(HttpMessage var1, ByteBuf var2, HttpHeaders var3) {
      super(var2);
      this.message = var1;
      this.trailingHeaders = var3;
   }

   public HttpHeaders trailingHeaders() {
      return this.trailingHeaders;
   }

   public void setTrailingHeaders(HttpHeaders var1) {
      this.trailingHeaders = var1;
   }

   public HttpVersion getProtocolVersion() {
      return this.message.getProtocolVersion();
   }

   public FullHttpMessage setProtocolVersion(HttpVersion var1) {
      this.message.setProtocolVersion(var1);
      return this;
   }

   public HttpHeaders headers() {
      return this.message.headers();
   }

   public DecoderResult getDecoderResult() {
      return this.message.getDecoderResult();
   }

   public void setDecoderResult(DecoderResult var1) {
      this.message.setDecoderResult(var1);
   }

   public FullHttpMessage retain(int var1) {
      super.retain(var1);
      return this;
   }

   public FullHttpMessage retain() {
      super.retain();
      return this;
   }

   public abstract FullHttpMessage copy();

   public abstract FullHttpMessage duplicate();

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

   HttpObjectAggregator$AggregatedFullHttpMessage(HttpMessage var1, ByteBuf var2, HttpHeaders var3, Object var4) {
      this(var1, var2, var3);
   }
}
