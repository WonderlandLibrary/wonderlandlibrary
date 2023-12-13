package com.google.common.hash;

import java.nio.charset.Charset;

abstract class AbstractHasher implements Hasher {
   public final Hasher putBoolean(boolean var1) {
      return this.putByte((byte)(var1 ? 1 : 0));
   }

   public final Hasher putDouble(double var1) {
      return this.putLong(Double.doubleToRawLongBits(var1));
   }

   public final Hasher putFloat(float var1) {
      return this.putInt(Float.floatToRawIntBits(var1));
   }

   public Hasher putUnencodedChars(CharSequence var1) {
      int var2 = 0;

      for(int var3 = var1.length(); var2 < var3; ++var2) {
         this.putChar(var1.charAt(var2));
      }

      return this;
   }

   public Hasher putString(CharSequence var1, Charset var2) {
      return this.putBytes(var1.toString().getBytes(var2));
   }

   public PrimitiveSink putString(CharSequence var1, Charset var2) {
      return this.putString(var1, var2);
   }

   public PrimitiveSink putUnencodedChars(CharSequence var1) {
      return this.putUnencodedChars(var1);
   }

   public PrimitiveSink putBoolean(boolean var1) {
      return this.putBoolean(var1);
   }

   public PrimitiveSink putDouble(double var1) {
      return this.putDouble(var1);
   }

   public PrimitiveSink putFloat(float var1) {
      return this.putFloat(var1);
   }
}
