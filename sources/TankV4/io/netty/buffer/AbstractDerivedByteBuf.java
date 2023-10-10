package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.ByteBuffer;

public abstract class AbstractDerivedByteBuf extends AbstractByteBuf {
   protected AbstractDerivedByteBuf(int var1) {
      super(var1);
   }

   public final int refCnt() {
      return this.unwrap().refCnt();
   }

   public final ByteBuf retain() {
      this.unwrap().retain();
      return this;
   }

   public final ByteBuf retain(int var1) {
      this.unwrap().retain(var1);
      return this;
   }

   public final boolean release() {
      return this.unwrap().release();
   }

   public final boolean release(int var1) {
      return this.unwrap().release(var1);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      return this.nioBuffer(var1, var2);
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      return this.unwrap().nioBuffer(var1, var2);
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }
}
