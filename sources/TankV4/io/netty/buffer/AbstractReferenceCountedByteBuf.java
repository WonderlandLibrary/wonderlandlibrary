package io.netty.buffer;

import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCountedByteBuf extends AbstractByteBuf {
   private static final AtomicIntegerFieldUpdater refCntUpdater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
   private static final long REFCNT_FIELD_OFFSET;
   private volatile int refCnt = 1;

   protected AbstractReferenceCountedByteBuf(int var1) {
      super(var1);
   }

   public final int refCnt() {
      return PlatformDependent.getInt(this, REFCNT_FIELD_OFFSET);
   }

   protected final void setRefCnt(int var1) {
      this.refCnt = var1;
   }

   public ByteBuf retain() {
      int var1;
      do {
         var1 = this.refCnt;
         if (var1 == 0) {
            throw new IllegalReferenceCountException(0, 1);
         }

         if (var1 == Integer.MAX_VALUE) {
            throw new IllegalReferenceCountException(Integer.MAX_VALUE, 1);
         }
      } while(!refCntUpdater.compareAndSet(this, var1, var1 + 1));

      return this;
   }

   public ByteBuf retain(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("increment: " + var1 + " (expected: > 0)");
      } else {
         int var2;
         do {
            var2 = this.refCnt;
            if (var2 == 0) {
               throw new IllegalReferenceCountException(0, var1);
            }

            if (var2 > Integer.MAX_VALUE - var1) {
               throw new IllegalReferenceCountException(var2, var1);
            }
         } while(!refCntUpdater.compareAndSet(this, var2, var2 + var1));

         return this;
      }
   }

   public final boolean release() {
      int var1;
      do {
         var1 = this.refCnt;
         if (var1 == 0) {
            throw new IllegalReferenceCountException(0, -1);
         }
      } while(!refCntUpdater.compareAndSet(this, var1, var1 - 1));

      if (var1 == 1) {
         this.deallocate();
         return true;
      } else {
         return false;
      }
   }

   public final boolean release(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("decrement: " + var1 + " (expected: > 0)");
      } else {
         int var2;
         do {
            var2 = this.refCnt;
            if (var2 < var1) {
               throw new IllegalReferenceCountException(var2, -var1);
            }
         } while(!refCntUpdater.compareAndSet(this, var2, var2 - var1));

         if (var2 == var1) {
            this.deallocate();
            return true;
         } else {
            return false;
         }
      }
   }

   protected abstract void deallocate();

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }

   static {
      long var0 = -1L;

      try {
         if (PlatformDependent.hasUnsafe()) {
            var0 = PlatformDependent.objectFieldOffset(AbstractReferenceCountedByteBuf.class.getDeclaredField("refCnt"));
         }
      } catch (Throwable var3) {
      }

      REFCNT_FIELD_OFFSET = var0;
   }
}
