package org.lwjgl;

import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ReadOnlyBufferException;

public class PointerBuffer implements Comparable {
   private static final boolean is64Bit;
   protected final ByteBuffer pointers;
   protected final Buffer view;
   protected final IntBuffer view32;
   protected final LongBuffer view64;

   public PointerBuffer(int var1) {
      this(BufferUtils.createByteBuffer(var1 * getPointerSize()));
   }

   public PointerBuffer(ByteBuffer var1) {
      if (LWJGLUtil.CHECKS) {
         checkSource(var1);
      }

      this.pointers = var1.slice().order(var1.order());
      if (is64Bit) {
         this.view32 = null;
         this.view = this.view64 = this.pointers.asLongBuffer();
      } else {
         this.view = this.view32 = this.pointers.asIntBuffer();
         this.view64 = null;
      }

   }

   private static void checkSource(ByteBuffer var0) {
      if (!var0.isDirect()) {
         throw new IllegalArgumentException("The source buffer is not direct.");
      } else {
         int var1 = is64Bit ? 8 : 4;
         if ((MemoryUtil.getAddress0((Buffer)var0) + (long)var0.position()) % (long)var1 != 0L || var0.remaining() % var1 != 0) {
            throw new IllegalArgumentException("The source buffer is not aligned to " + var1 + " bytes.");
         }
      }
   }

   public ByteBuffer getBuffer() {
      return this.pointers;
   }

   public static boolean is64Bit() {
      return is64Bit;
   }

   public static int getPointerSize() {
      return is64Bit ? 8 : 4;
   }

   public final int capacity() {
      return this.view.capacity();
   }

   public final int position() {
      return this.view.position();
   }

   public final int positionByte() {
      return this.position() * getPointerSize();
   }

   public final PointerBuffer position(int var1) {
      this.view.position(var1);
      return this;
   }

   public final int limit() {
      return this.view.limit();
   }

   public final PointerBuffer limit(int var1) {
      this.view.limit(var1);
      return this;
   }

   public final PointerBuffer mark() {
      this.view.mark();
      return this;
   }

   public final PointerBuffer reset() {
      this.view.reset();
      return this;
   }

   public final PointerBuffer clear() {
      this.view.clear();
      return this;
   }

   public final PointerBuffer flip() {
      this.view.flip();
      return this;
   }

   public final PointerBuffer rewind() {
      this.view.rewind();
      return this;
   }

   public final int remaining() {
      return this.view.remaining();
   }

   public final int remainingByte() {
      return this.remaining() * getPointerSize();
   }

   public final boolean hasRemaining() {
      return this.view.hasRemaining();
   }

   public static PointerBuffer allocateDirect(int var0) {
      return new PointerBuffer(var0);
   }

   protected PointerBuffer newInstance(ByteBuffer var1) {
      return new PointerBuffer(var1);
   }

   public PointerBuffer slice() {
      int var1 = getPointerSize();
      this.pointers.position(this.view.position() * var1);
      this.pointers.limit(this.view.limit() * var1);
      PointerBuffer var2 = this.newInstance(this.pointers);
      this.pointers.clear();
      return var2;
   }

   public PointerBuffer duplicate() {
      PointerBuffer var1 = this.newInstance(this.pointers);
      var1.position(this.view.position());
      var1.limit(this.view.limit());
      return var1;
   }

   public PointerBuffer asReadOnlyBuffer() {
      PointerBuffer.PointerBufferR var1 = new PointerBuffer.PointerBufferR(this.pointers);
      var1.position(this.view.position());
      var1.limit(this.view.limit());
      return var1;
   }

   public boolean isReadOnly() {
      return false;
   }

   public long get() {
      return is64Bit ? this.view64.get() : (long)this.view32.get() & 4294967295L;
   }

   public PointerBuffer put(long var1) {
      if (is64Bit) {
         this.view64.put(var1);
      } else {
         this.view32.put((int)var1);
      }

      return this;
   }

   public PointerBuffer put(PointerWrapper var1) {
      return this.put(var1.getPointer());
   }

   public static void put(ByteBuffer var0, long var1) {
      if (is64Bit) {
         var0.putLong(var1);
      } else {
         var0.putInt((int)var1);
      }

   }

   public long get(int var1) {
      return is64Bit ? this.view64.get(var1) : (long)this.view32.get(var1) & 4294967295L;
   }

   public PointerBuffer put(int var1, long var2) {
      if (is64Bit) {
         this.view64.put(var1, var2);
      } else {
         this.view32.put(var1, (int)var2);
      }

      return this;
   }

   public PointerBuffer put(int var1, PointerWrapper var2) {
      return this.put(var1, var2.getPointer());
   }

   public static void put(ByteBuffer var0, int var1, long var2) {
      if (is64Bit) {
         var0.putLong(var1, var2);
      } else {
         var0.putInt(var1, (int)var2);
      }

   }

   public PointerBuffer get(long[] var1, int var2, int var3) {
      if (is64Bit) {
         this.view64.get(var1, var2, var3);
      } else {
         checkBounds(var2, var3, var1.length);
         if (var3 > this.view32.remaining()) {
            throw new BufferUnderflowException();
         }

         int var4 = var2 + var3;

         for(int var5 = var2; var5 < var4; ++var5) {
            var1[var5] = (long)this.view32.get() & 4294967295L;
         }
      }

      return this;
   }

   public PointerBuffer get(long[] var1) {
      return this.get(var1, 0, var1.length);
   }

   public PointerBuffer put(PointerBuffer var1) {
      if (is64Bit) {
         this.view64.put(var1.view64);
      } else {
         this.view32.put(var1.view32);
      }

      return this;
   }

   public PointerBuffer put(long[] var1, int var2, int var3) {
      if (is64Bit) {
         this.view64.put(var1, var2, var3);
      } else {
         checkBounds(var2, var3, var1.length);
         if (var3 > this.view32.remaining()) {
            throw new BufferOverflowException();
         }

         int var4 = var2 + var3;

         for(int var5 = var2; var5 < var4; ++var5) {
            this.view32.put((int)var1[var5]);
         }
      }

      return this;
   }

   public final PointerBuffer put(long[] var1) {
      return this.put(var1, 0, var1.length);
   }

   public PointerBuffer compact() {
      if (is64Bit) {
         this.view64.compact();
      } else {
         this.view32.compact();
      }

      return this;
   }

   public ByteOrder order() {
      return is64Bit ? this.view64.order() : this.view32.order();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(48);
      var1.append(this.getClass().getName());
      var1.append("[pos=");
      var1.append(this.position());
      var1.append(" lim=");
      var1.append(this.limit());
      var1.append(" cap=");
      var1.append(this.capacity());
      var1.append("]");
      return var1.toString();
   }

   public int hashCode() {
      int var1 = 1;
      int var2 = this.position();

      for(int var3 = this.limit() - 1; var3 >= var2; --var3) {
         var1 = 31 * var1 + (int)this.get(var3);
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof PointerBuffer)) {
         return false;
      } else {
         PointerBuffer var2 = (PointerBuffer)var1;
         if (this.remaining() != var2.remaining()) {
            return false;
         } else {
            int var3 = this.position();
            int var4 = this.limit() - 1;

            for(int var5 = var2.limit() - 1; var4 >= var3; --var5) {
               long var6 = this.get(var4);
               long var8 = var2.get(var5);
               if (var6 != var8) {
                  return false;
               }

               --var4;
            }

            return true;
         }
      }
   }

   public int compareTo(Object var1) {
      PointerBuffer var2 = (PointerBuffer)var1;
      int var3 = this.position() + Math.min(this.remaining(), var2.remaining());
      int var4 = this.position();

      for(int var5 = var2.position(); var4 < var3; ++var5) {
         long var6 = this.get(var4);
         long var8 = var2.get(var5);
         if (var6 != var8) {
            if (var6 < var8) {
               return -1;
            }

            return 1;
         }

         ++var4;
      }

      return this.remaining() - var2.remaining();
   }

   private static void checkBounds(int var0, int var1, int var2) {
      if ((var0 | var1 | var0 + var1 | var2 - (var0 + var1)) < 0) {
         throw new IndexOutOfBoundsException();
      }
   }

   static {
      boolean var0 = false;

      label14: {
         try {
            Method var1 = Class.forName("org.lwjgl.Sys").getDeclaredMethod("is64Bit", (Class[])null);
            var0 = (Boolean)var1.invoke((Object)null, (Object[])null);
         } catch (Throwable var3) {
            is64Bit = var0;
            break label14;
         }

         is64Bit = var0;
      }

   }

   private static final class PointerBufferR extends PointerBuffer {
      PointerBufferR(ByteBuffer var1) {
         super(var1);
      }

      public boolean isReadOnly() {
         return true;
      }

      protected PointerBuffer newInstance(ByteBuffer var1) {
         return new PointerBuffer.PointerBufferR(var1);
      }

      public PointerBuffer asReadOnlyBuffer() {
         return this.duplicate();
      }

      public PointerBuffer put(long var1) {
         throw new ReadOnlyBufferException();
      }

      public PointerBuffer put(int var1, long var2) {
         throw new ReadOnlyBufferException();
      }

      public PointerBuffer put(PointerBuffer var1) {
         throw new ReadOnlyBufferException();
      }

      public PointerBuffer put(long[] var1, int var2, int var3) {
         throw new ReadOnlyBufferException();
      }

      public PointerBuffer compact() {
         throw new ReadOnlyBufferException();
      }
   }
}
