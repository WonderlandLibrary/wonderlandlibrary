package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCounted;

public class TextWebSocketFrame extends WebSocketFrame {
   public TextWebSocketFrame() {
      super(Unpooled.buffer(0));
   }

   public TextWebSocketFrame(String var1) {
      super(fromText(var1));
   }

   public TextWebSocketFrame(ByteBuf var1) {
      super(var1);
   }

   public TextWebSocketFrame(boolean var1, int var2, String var3) {
      super(var1, var2, fromText(var3));
   }

   private static ByteBuf fromText(String var0) {
      return var0 != null && !var0.isEmpty() ? Unpooled.copiedBuffer((CharSequence)var0, CharsetUtil.UTF_8) : Unpooled.EMPTY_BUFFER;
   }

   public TextWebSocketFrame(boolean var1, int var2, ByteBuf var3) {
      super(var1, var2, var3);
   }

   public String text() {
      return this.content().toString(CharsetUtil.UTF_8);
   }

   public TextWebSocketFrame copy() {
      return new TextWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy());
   }

   public TextWebSocketFrame duplicate() {
      return new TextWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate());
   }

   public TextWebSocketFrame retain() {
      super.retain();
      return this;
   }

   public TextWebSocketFrame retain(int var1) {
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
