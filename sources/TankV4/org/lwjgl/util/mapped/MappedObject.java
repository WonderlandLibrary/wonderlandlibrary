package org.lwjgl.util.mapped;

import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public abstract class MappedObject {
   static final boolean CHECKS = LWJGLUtil.getPrivilegedBoolean("org.lwjgl.util.mapped.Checks");
   public long baseAddress;
   public long viewAddress;
   ByteBuffer preventGC;
   public static int SIZEOF = -1;
   public int view;

   protected MappedObject() {
   }

   protected final long getViewAddress(int var1) {
      throw new InternalError("type not registered");
   }

   public final void setViewAddress(long var1) {
      if (CHECKS) {
         this.checkAddress(var1);
      }

      this.viewAddress = var1;
   }

   final void checkAddress(long var1) {
      long var3 = MemoryUtil.getAddress0((Buffer)this.preventGC);
      int var5 = (int)(var1 - var3);
      if (var1 < var3 || this.preventGC.capacity() < var5 + this.getSizeof()) {
         throw new IndexOutOfBoundsException(Integer.toString(var5 / this.getSizeof()));
      }
   }

   final void checkRange(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else if ((long)this.preventGC.capacity() < this.viewAddress - MemoryUtil.getAddress0((Buffer)this.preventGC) + (long)var1) {
         throw new BufferOverflowException();
      }
   }

   public final int getAlign() {
      throw new InternalError("type not registered");
   }

   public final int getSizeof() {
      throw new InternalError("type not registered");
   }

   public final int capacity() {
      throw new InternalError("type not registered");
   }

   public static MappedObject map(ByteBuffer var0) {
      throw new InternalError("type not registered");
   }

   public static MappedObject map(long var0, int var2) {
      throw new InternalError("type not registered");
   }

   public static MappedObject malloc(int var0) {
      throw new InternalError("type not registered");
   }

   public final MappedObject dup() {
      throw new InternalError("type not registered");
   }

   public final MappedObject slice() {
      throw new InternalError("type not registered");
   }

   public final void runViewConstructor() {
      throw new InternalError("type not registered");
   }

   public final void next() {
      throw new InternalError("type not registered");
   }

   public final void copyTo(MappedObject var1) {
      throw new InternalError("type not registered");
   }

   public final void copyRange(MappedObject var1, int var2) {
      throw new InternalError("type not registered");
   }

   public static Iterable foreach(MappedObject var0) {
      return foreach(var0, var0.capacity());
   }

   public static Iterable foreach(MappedObject var0, int var1) {
      return new MappedForeach(var0, var1);
   }

   public final MappedObject[] asArray() {
      throw new InternalError("type not registered");
   }

   public final ByteBuffer backingByteBuffer() {
      return this.preventGC;
   }
}
