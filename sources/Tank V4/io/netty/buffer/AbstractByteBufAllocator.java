package io.netty.buffer;

import io.netty.util.ResourceLeak;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;

public abstract class AbstractByteBufAllocator implements ByteBufAllocator {
   private static final int DEFAULT_INITIAL_CAPACITY = 256;
   private static final int DEFAULT_MAX_COMPONENTS = 16;
   private final boolean directByDefault;
   private final ByteBuf emptyBuf;

   protected static ByteBuf toLeakAwareBuffer(ByteBuf var0) {
      ResourceLeak var1;
      switch(ResourceLeakDetector.getLevel()) {
      case SIMPLE:
         var1 = AbstractByteBuf.leakDetector.open(var0);
         if (var1 != null) {
            var0 = new SimpleLeakAwareByteBuf((ByteBuf)var0, var1);
         }
         break;
      case ADVANCED:
      case PARANOID:
         var1 = AbstractByteBuf.leakDetector.open(var0);
         if (var1 != null) {
            var0 = new AdvancedLeakAwareByteBuf((ByteBuf)var0, var1);
         }
      }

      return (ByteBuf)var0;
   }

   protected AbstractByteBufAllocator() {
      this(false);
   }

   protected AbstractByteBufAllocator(boolean var1) {
      this.directByDefault = var1 && PlatformDependent.hasUnsafe();
      this.emptyBuf = new EmptyByteBuf(this);
   }

   public ByteBuf buffer() {
      return this.directByDefault ? this.directBuffer() : this.heapBuffer();
   }

   public ByteBuf buffer(int var1) {
      return this.directByDefault ? this.directBuffer(var1) : this.heapBuffer(var1);
   }

   public ByteBuf buffer(int var1, int var2) {
      return this.directByDefault ? this.directBuffer(var1, var2) : this.heapBuffer(var1, var2);
   }

   public ByteBuf ioBuffer() {
      return PlatformDependent.hasUnsafe() ? this.directBuffer(0) : this.heapBuffer(0);
   }

   public ByteBuf ioBuffer(int var1) {
      return PlatformDependent.hasUnsafe() ? this.directBuffer(var1) : this.heapBuffer(var1);
   }

   public ByteBuf ioBuffer(int var1, int var2) {
      return PlatformDependent.hasUnsafe() ? this.directBuffer(var1, var2) : this.heapBuffer(var1, var2);
   }

   public ByteBuf heapBuffer() {
      return this.heapBuffer(256, Integer.MAX_VALUE);
   }

   public ByteBuf heapBuffer(int var1) {
      return this.heapBuffer(var1, Integer.MAX_VALUE);
   }

   public ByteBuf heapBuffer(int var1, int var2) {
      if (var1 == 0 && var2 == 0) {
         return this.emptyBuf;
      } else {
         validate(var1, var2);
         return this.newHeapBuffer(var1, var2);
      }
   }

   public ByteBuf directBuffer() {
      return this.directBuffer(256, Integer.MAX_VALUE);
   }

   public ByteBuf directBuffer(int var1) {
      return this.directBuffer(var1, Integer.MAX_VALUE);
   }

   public ByteBuf directBuffer(int var1, int var2) {
      if (var1 == 0 && var2 == 0) {
         return this.emptyBuf;
      } else {
         validate(var1, var2);
         return this.newDirectBuffer(var1, var2);
      }
   }

   public CompositeByteBuf compositeBuffer() {
      return this.directByDefault ? this.compositeDirectBuffer() : this.compositeHeapBuffer();
   }

   public CompositeByteBuf compositeBuffer(int var1) {
      return this.directByDefault ? this.compositeDirectBuffer(var1) : this.compositeHeapBuffer(var1);
   }

   public CompositeByteBuf compositeHeapBuffer() {
      return this.compositeHeapBuffer(16);
   }

   public CompositeByteBuf compositeHeapBuffer(int var1) {
      return new CompositeByteBuf(this, false, var1);
   }

   public CompositeByteBuf compositeDirectBuffer() {
      return this.compositeDirectBuffer(16);
   }

   public CompositeByteBuf compositeDirectBuffer(int var1) {
      return new CompositeByteBuf(this, true, var1);
   }

   private static void validate(int var0, int var1) {
      if (var0 < 0) {
         throw new IllegalArgumentException("initialCapacity: " + var0 + " (expectd: 0+)");
      } else if (var0 > var1) {
         throw new IllegalArgumentException(String.format("initialCapacity: %d (expected: not greater than maxCapacity(%d)", var0, var1));
      }
   }

   protected abstract ByteBuf newHeapBuffer(int var1, int var2);

   protected abstract ByteBuf newDirectBuffer(int var1, int var2);

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + "(directByDefault: " + this.directByDefault + ')';
   }
}
