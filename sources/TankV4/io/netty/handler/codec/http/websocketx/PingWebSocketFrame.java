package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;

public class PingWebSocketFrame extends WebSocketFrame {
   public PingWebSocketFrame() {
      super(true, 0, Unpooled.buffer(0));
   }

   public PingWebSocketFrame(ByteBuf var1) {
      super(var1);
   }

   public PingWebSocketFrame(boolean var1, int var2, ByteBuf var3) {
      super(var1, var2, var3);
   }

   public PingWebSocketFrame copy() {
      return new PingWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy());
   }

   public PingWebSocketFrame duplicate() {
      return new PingWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate());
   }

   public PingWebSocketFrame retain() {
      super.retain();
      return this;
   }

   public PingWebSocketFrame retain(int var1) {
      super.retain(var1);
      return this;
   }

   public WebSocketFrame retain(int var1) {
      return this.retain(var1);
   }

   public WebSocketFrame retain() {
      return this.retain();
   }

   public WebSocketFrame duplicate() {
      return this.duplicate();
   }

   public WebSocketFrame copy() {
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
