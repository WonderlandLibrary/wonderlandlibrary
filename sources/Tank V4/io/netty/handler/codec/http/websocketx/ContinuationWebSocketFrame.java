package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCounted;

public class ContinuationWebSocketFrame extends WebSocketFrame {
   private String aggregatedText;

   public ContinuationWebSocketFrame() {
      super(Unpooled.buffer(0));
   }

   public ContinuationWebSocketFrame(ByteBuf var1) {
      super(var1);
   }

   public ContinuationWebSocketFrame(boolean var1, int var2, ByteBuf var3) {
      super(var1, var2, var3);
   }

   public ContinuationWebSocketFrame(boolean var1, int var2, ByteBuf var3, String var4) {
      super(var1, var2, var3);
      this.aggregatedText = var4;
   }

   public ContinuationWebSocketFrame(boolean var1, int var2, String var3) {
      this(var1, var2, fromText(var3), (String)null);
   }

   public String text() {
      return this.content().toString(CharsetUtil.UTF_8);
   }

   private static ByteBuf fromText(String var0) {
      return var0 != null && !var0.isEmpty() ? Unpooled.copiedBuffer((CharSequence)var0, CharsetUtil.UTF_8) : Unpooled.EMPTY_BUFFER;
   }

   public String aggregatedText() {
      return this.aggregatedText;
   }

   public ContinuationWebSocketFrame copy() {
      return new ContinuationWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy(), this.aggregatedText());
   }

   public ContinuationWebSocketFrame duplicate() {
      return new ContinuationWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate(), this.aggregatedText());
   }

   public ContinuationWebSocketFrame retain() {
      super.retain();
      return this;
   }

   public ContinuationWebSocketFrame retain(int var1) {
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
