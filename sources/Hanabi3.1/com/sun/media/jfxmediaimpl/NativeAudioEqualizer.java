package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.EqualizerBand;

final class NativeAudioEqualizer implements AudioEqualizer {
   private final long nativeRef;

   NativeAudioEqualizer(long var1) {
      if (var1 == 0L) {
         throw new IllegalArgumentException("Invalid native media reference");
      } else {
         this.nativeRef = var1;
      }
   }

   public boolean getEnabled() {
      return this.nativeGetEnabled(this.nativeRef);
   }

   public void setEnabled(boolean var1) {
      this.nativeSetEnabled(this.nativeRef, var1);
   }

   public EqualizerBand addBand(double var1, double var3, double var5) {
      return this.nativeGetNumBands(this.nativeRef) >= 64 && var5 >= -24.0 && var5 <= 12.0 ? null : this.nativeAddBand(this.nativeRef, var1, var3, var5);
   }

   public boolean removeBand(double var1) {
      return var1 > 0.0 ? this.nativeRemoveBand(this.nativeRef, var1) : false;
   }

   private native boolean nativeGetEnabled(long var1);

   private native void nativeSetEnabled(long var1, boolean var3);

   private native int nativeGetNumBands(long var1);

   private native EqualizerBand nativeAddBand(long var1, double var3, double var5, double var7);

   private native boolean nativeRemoveBand(long var1, double var3);
}
