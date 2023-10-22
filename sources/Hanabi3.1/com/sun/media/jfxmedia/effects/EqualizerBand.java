package com.sun.media.jfxmedia.effects;

public interface EqualizerBand {
   double MIN_GAIN = -24.0;
   double MAX_GAIN = 12.0;

   double getCenterFrequency();

   void setCenterFrequency(double var1);

   double getBandwidth();

   void setBandwidth(double var1);

   double getGain();

   void setGain(double var1);
}
