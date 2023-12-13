package com.sun.webkit;

public final class SharedBuffer {
   private long nativePointer;

   SharedBuffer() {
      this.nativePointer = twkCreate();
   }

   private SharedBuffer(long var1) {
      if (var1 == 0L) {
         throw new IllegalArgumentException("nativePointer is 0");
      } else {
         this.nativePointer = var1;
      }
   }

   private static SharedBuffer fwkCreate(long var0) {
      return new SharedBuffer(var0);
   }

   long size() {
      if (this.nativePointer == 0L) {
         throw new IllegalStateException("nativePointer is 0");
      } else {
         return twkSize(this.nativePointer);
      }
   }

   int getSomeData(long var1, byte[] var3, int var4, int var5) {
      if (this.nativePointer == 0L) {
         throw new IllegalStateException("nativePointer is 0");
      } else if (var1 < 0L) {
         throw new IndexOutOfBoundsException("position is negative");
      } else if (var1 > this.size()) {
         throw new IndexOutOfBoundsException("position is greater than size");
      } else if (var3 == null) {
         throw new NullPointerException("buffer is null");
      } else if (var4 < 0) {
         throw new IndexOutOfBoundsException("offset is negative");
      } else if (var5 < 0) {
         throw new IndexOutOfBoundsException("length is negative");
      } else if (var5 > var3.length - var4) {
         throw new IndexOutOfBoundsException("length is greater than buffer.length - offset");
      } else {
         return twkGetSomeData(this.nativePointer, var1, var3, var4, var5);
      }
   }

   void append(byte[] var1, int var2, int var3) {
      if (this.nativePointer == 0L) {
         throw new IllegalStateException("nativePointer is 0");
      } else if (var1 == null) {
         throw new NullPointerException("buffer is null");
      } else if (var2 < 0) {
         throw new IndexOutOfBoundsException("offset is negative");
      } else if (var3 < 0) {
         throw new IndexOutOfBoundsException("length is negative");
      } else if (var3 > var1.length - var2) {
         throw new IndexOutOfBoundsException("length is greater than buffer.length - offset");
      } else {
         twkAppend(this.nativePointer, var1, var2, var3);
      }
   }

   void dispose() {
      if (this.nativePointer == 0L) {
         throw new IllegalStateException("nativePointer is 0");
      } else {
         twkDispose(this.nativePointer);
         this.nativePointer = 0L;
      }
   }

   private static native long twkCreate();

   private static native long twkSize(long var0);

   private static native int twkGetSomeData(long var0, long var2, byte[] var4, int var5, int var6);

   private static native void twkAppend(long var0, byte[] var2, int var3, int var4);

   private static native void twkDispose(long var0);
}
