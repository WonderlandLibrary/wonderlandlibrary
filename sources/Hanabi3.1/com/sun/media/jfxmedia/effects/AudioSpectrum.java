package com.sun.media.jfxmedia.effects;

public interface AudioSpectrum {
   boolean getEnabled();

   void setEnabled(boolean var1);

   int getBandCount();

   void setBandCount(int var1);

   double getInterval();

   void setInterval(double var1);

   int getSensitivityThreshold();

   void setSensitivityThreshold(int var1);

   float[] getMagnitudes(float[] var1);

   float[] getPhases(float[] var1);
}
