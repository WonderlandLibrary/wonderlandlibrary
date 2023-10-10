package io.netty.buffer;

import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;

public class DefaultByteBufHolder implements ByteBufHolder {
   private final ByteBuf data;

   public DefaultByteBufHolder(ByteBuf var1) {
      if (var1 == null) {
         throw new NullPointerException("data");
      } else {
         this.data = var1;
      }
   }

   public ByteBuf content() {
      if (this.data.refCnt() <= 0) {
         throw new IllegalReferenceCountException(this.data.refCnt());
      } else {
         return this.data;
      }
   }

   public ByteBufHolder copy() {
      return new DefaultByteBufHolder(this.data.copy());
   }

   public ByteBufHolder duplicate() {
      return new DefaultByteBufHolder(this.data.duplicate());
   }

   public int refCnt() {
      return this.data.refCnt();
   }

   public ByteBufHolder retain() {
      this.data.retain();
      return this;
   }

   public ByteBufHolder retain(int var1) {
      this.data.retain(var1);
      return this;
   }

   public boolean release() {
      return this.data.release();
   }

   public boolean release(int var1) {
      return this.data.release(var1);
   }

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + '(' + this.content().toString() + ')';
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }
}
