package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.effects.AudioSpectrum;

public class AudioSpectrumEvent extends PlayerEvent {
   private AudioSpectrum source;
   private double timestamp;
   private double duration;
   private boolean queryTimestamp;

   public AudioSpectrumEvent(AudioSpectrum var1, double var2, double var4, boolean var6) {
      this.source = var1;
      this.timestamp = var2;
      this.duration = var4;
      this.queryTimestamp = var6;
   }

   public final AudioSpectrum getSource() {
      return this.source;
   }

   public final void setTimestamp(double var1) {
      this.timestamp = var1;
   }

   public final double getTimestamp() {
      return this.timestamp;
   }

   public final double getDuration() {
      return this.duration;
   }

   public final boolean queryTimestamp() {
      return this.queryTimestamp;
   }
}
