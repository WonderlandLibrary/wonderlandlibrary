package com.sun.media.jfxmedia.effects;

public interface AudioEqualizer {
   int MAX_NUM_BANDS = 64;

   boolean getEnabled();

   void setEnabled(boolean var1);

   EqualizerBand addBand(double var1, double var3, double var5);

   boolean removeBand(double var1);
}
