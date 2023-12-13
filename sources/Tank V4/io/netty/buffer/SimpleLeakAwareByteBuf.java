package io.netty.buffer;

import io.netty.util.ResourceLeak;
import java.nio.ByteOrder;

final class SimpleLeakAwareByteBuf extends WrappedByteBuf {
   private final ResourceLeak leak;

   SimpleLeakAwareByteBuf(ByteBuf var1, ResourceLeak var2) {
      super(var1);
      this.leak = var2;
   }

   public boolean release() {
      boolean var1 = super.release();
      if (var1) {
         this.leak.close();
      }

      return var1;
   }

   public boolean release(int var1) {
      boolean var2 = super.release(var1);
      if (var2) {
         this.leak.close();
      }

      return var2;
   }

   public ByteBuf order(ByteOrder var1) {
      this.leak.record();
      return this.order() == var1 ? this : new SimpleLeakAwareByteBuf(super.order(var1), this.leak);
   }

   public ByteBuf slice() {
      return new SimpleLeakAwareByteBuf(super.slice(), this.leak);
   }

   public ByteBuf slice(int var1, int var2) {
      return new SimpleLeakAwareByteBuf(super.slice(var1, var2), this.leak);
   }

   public ByteBuf duplicate() {
      return new SimpleLeakAwareByteBuf(super.duplicate(), this.leak);
   }

   public ByteBuf readSlice(int var1) {
      return new SimpleLeakAwareByteBuf(super.readSlice(var1), this.leak);
   }
}
