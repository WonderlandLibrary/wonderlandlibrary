package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.effects.AudioSpectrum;

final class NativeAudioSpectrum implements AudioSpectrum {
   private static final float[] EMPTY_FLOAT_ARRAY = new float[0];
   public static final int DEFAULT_THRESHOLD = -60;
   public static final int DEFAULT_BANDS = 128;
   public static final double DEFAULT_INTERVAL = 0.1;
   private final long nativeRef;
   private float[] magnitudes;
   private float[] phases;

   NativeAudioSpectrum(long var1) {
      this.magnitudes = EMPTY_FLOAT_ARRAY;
      this.phases = EMPTY_FLOAT_ARRAY;
      if (var1 == 0L) {
         throw new IllegalArgumentException("Invalid native media reference");
      } else {
         this.nativeRef = var1;
         this.setBandCount(128);
      }
   }

   public boolean getEnabled() {
      return this.nativeGetEnabled(this.nativeRef);
   }

   public void setEnabled(boolean var1) {
      this.nativeSetEnabled(this.nativeRef, var1);
   }

   public int getBandCount() {
      return this.phases.length;
   }

   public void setBandCount(int var1) {
      if (var1 <= 1) {
         this.magnitudes = EMPTY_FLOAT_ARRAY;
         this.phases = EMPTY_FLOAT_ARRAY;
         throw new IllegalArgumentException("Number of bands must at least be 2");
      } else {
         this.magnitudes = new float[var1];

         for(int var2 = 0; var2 < this.magnitudes.length; ++var2) {
            this.magnitudes[var2] = -60.0F;
         }

         this.phases = new float[var1];
         this.nativeSetBands(this.nativeRef, var1, this.magnitudes, this.phases);
      }
   }

   public double getInterval() {
      return this.nativeGetInterval(this.nativeRef);
   }

   public void setInterval(double var1) {
      if (var1 * 1.0E9 >= 1.0) {
         this.nativeSetInterval(this.nativeRef, var1);
      } else {
         throw new IllegalArgumentException("Interval can't be less that 1 nanosecond");
      }
   }

   public int getSensitivityThreshold() {
      return this.nativeGetThreshold(this.nativeRef);
   }

   public void setSensitivityThreshold(int var1) {
      if (var1 <= 0) {
         this.nativeSetThreshold(this.nativeRef, var1);
      } else {
         throw new IllegalArgumentException(String.format("Sensitivity threshold must be less than 0: %d", var1));
      }
   }

   public float[] getMagnitudes(float[] var1) {
      int var2 = this.magnitudes.length;
      if (var1 == null || var1.length < var2) {
         var1 = new float[var2];
      }

      System.arraycopy(this.magnitudes, 0, var1, 0, var2);
      return var1;
   }

   public float[] getPhases(float[] var1) {
      int var2 = this.phases.length;
      if (var1 == null || var1.length < var2) {
         var1 = new float[var2];
      }

      System.arraycopy(this.phases, 0, var1, 0, var2);
      return var1;
   }

   private native boolean nativeGetEnabled(long var1);

   private native void nativeSetEnabled(long var1, boolean var3);

   private native void nativeSetBands(long var1, int var3, float[] var4, float[] var5);

   private native double nativeGetInterval(long var1);

   private native void nativeSetInterval(long var1, double var3);

   private native int nativeGetThreshold(long var1);

   private native void nativeSetThreshold(long var1, int var3);
}
