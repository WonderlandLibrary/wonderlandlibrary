package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;

public abstract class WebSocketFrame extends DefaultByteBufHolder {
   private final boolean finalFragment;
   private final int rsv;

   protected WebSocketFrame(ByteBuf var1) {
      this(true, 0, var1);
   }

   protected WebSocketFrame(boolean var1, int var2, ByteBuf var3) {
      super(var3);
      this.finalFragment = var1;
      this.rsv = var2;
   }

   public boolean isFinalFragment() {
      return this.finalFragment;
   }

   public int rsv() {
      return this.rsv;
   }

   public abstract WebSocketFrame copy();

   public abstract WebSocketFrame duplicate();

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + "(data: " + this.content().toString() + ')';
   }

   public WebSocketFrame retain() {
      super.retain();
      return this;
   }

   public WebSocketFrame retain(int var1) {
      super.retain(var1);
      return this;
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
