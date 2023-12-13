package io.netty.channel.udt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.util.ReferenceCounted;

public final class UdtMessage extends DefaultByteBufHolder {
   public UdtMessage(ByteBuf var1) {
      super(var1);
   }

   public UdtMessage copy() {
      return new UdtMessage(this.content().copy());
   }

   public UdtMessage duplicate() {
      return new UdtMessage(this.content().duplicate());
   }

   public UdtMessage retain() {
      super.retain();
      return this;
   }

   public UdtMessage retain(int var1) {
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
