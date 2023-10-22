package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.effects.EqualizerBand;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.util.HashMap;
import java.util.Map;

public final class IOSMediaPlayer extends NativeMediaPlayer {
   private IOSMedia iosMedia;
   private final NullAudioEQ audioEqualizer;
   private final NullAudioSpectrum audioSpectrum;
   private final MediaPlayerOverlay mediaPlayerOverlay;
   private float mutedVolume;
   private boolean muteEnabled;

   private IOSMediaPlayer(IOSMedia var1) {
      super(var1);
      this.mutedVolume = 1.0F;
      this.iosMedia = var1;
      this.init();
      this.handleError(this.iosInitPlayer(this.iosMedia.getNativeMediaRef()));
      this.audioEqualizer = new NullAudioEQ();
      this.audioSpectrum = new NullAudioSpectrum();
      this.mediaPlayerOverlay = new MediaPlayerOverlayImpl();
   }

   IOSMediaPlayer(Locator var1) {
      this(new IOSMedia(var1));
   }

   public AudioEqualizer getEqualizer() {
      return this.audioEqualizer;
   }

   public AudioSpectrum getAudioSpectrum() {
      return this.audioSpectrum;
   }

   public MediaPlayerOverlay getMediaPlayerOverlay() {
      return this.mediaPlayerOverlay;
   }

   private void handleError(int var1) throws MediaException {
      if (0 != var1) {
         MediaError var2 = MediaError.getFromCode(var1);
         throw new MediaException("Media error occurred", (Throwable)null, var2);
      }
   }

   protected long playerGetAudioSyncDelay() throws MediaException {
      long[] var1 = new long[1];
      this.handleError(this.iosGetAudioSyncDelay(this.iosMedia.getNativeMediaRef(), var1));
      return var1[0];
   }

   protected void playerSetAudioSyncDelay(long var1) throws MediaException {
      this.handleError(this.iosSetAudioSyncDelay(this.iosMedia.getNativeMediaRef(), var1));
   }

   protected void playerPlay() throws MediaException {
      this.handleError(this.iosPlay(this.iosMedia.getNativeMediaRef()));
   }

   protected void playerStop() throws MediaException {
      this.handleError(this.iosStop(this.iosMedia.getNativeMediaRef()));
   }

   protected void playerPause() throws MediaException {
      this.handleError(this.iosPause(this.iosMedia.getNativeMediaRef()));
   }

   protected float playerGetRate() throws MediaException {
      float[] var1 = new float[1];
      this.handleError(this.iosGetRate(this.iosMedia.getNativeMediaRef(), var1));
      return var1[0];
   }

   protected void playerSetRate(float var1) throws MediaException {
      this.handleError(this.iosSetRate(this.iosMedia.getNativeMediaRef(), var1));
   }

   protected double playerGetPresentationTime() throws MediaException {
      double[] var1 = new double[1];
      this.handleError(this.iosGetPresentationTime(this.iosMedia.getNativeMediaRef(), var1));
      return var1[0];
   }

   protected boolean playerGetMute() throws MediaException {
      return this.muteEnabled;
   }

   protected synchronized void playerSetMute(boolean var1) throws MediaException {
      if (var1 != this.muteEnabled) {
         if (var1) {
            float var2 = this.getVolume();
            this.playerSetVolume(0.0F);
            this.muteEnabled = true;
            this.mutedVolume = var2;
         } else {
            this.muteEnabled = false;
            this.playerSetVolume(this.mutedVolume);
         }
      }

   }

   protected float playerGetVolume() throws MediaException {
      synchronized(this) {
         if (this.muteEnabled) {
            return this.mutedVolume;
         }
      }

      float[] var1 = new float[1];
      this.handleError(this.iosGetVolume(this.iosMedia.getNativeMediaRef(), var1));
      return var1[0];
   }

   protected synchronized void playerSetVolume(float var1) throws MediaException {
      if (!this.muteEnabled) {
         int var2 = this.iosSetVolume(this.iosMedia.getNativeMediaRef(), var1);
         if (0 != var2) {
            this.handleError(var2);
         } else {
            this.mutedVolume = var1;
         }
      } else {
         this.mutedVolume = var1;
      }

   }

   protected float playerGetBalance() throws MediaException {
      float[] var1 = new float[1];
      this.handleError(this.iosGetBalance(this.iosMedia.getNativeMediaRef(), var1));
      return var1[0];
   }

   protected void playerSetBalance(float var1) throws MediaException {
      this.handleError(this.iosSetBalance(this.iosMedia.getNativeMediaRef(), var1));
   }

   protected double playerGetDuration() throws MediaException {
      double[] var1 = new double[1];
      this.handleError(this.iosGetDuration(this.iosMedia.getNativeMediaRef(), var1));
      double var2;
      if (var1[0] == -1.0) {
         var2 = Double.POSITIVE_INFINITY;
      } else {
         var2 = var1[0];
      }

      return var2;
   }

   protected void playerSeek(double var1) throws MediaException {
      this.handleError(this.iosSeek(this.iosMedia.getNativeMediaRef(), var1));
   }

   protected void playerInit() throws MediaException {
   }

   protected void playerFinish() throws MediaException {
      this.handleError(this.iosFinish(this.iosMedia.getNativeMediaRef()));
   }

   protected void playerDispose() {
      this.iosDispose(this.iosMedia.getNativeMediaRef());
      this.iosMedia = null;
   }

   private native int iosInitPlayer(long var1);

   private native int iosGetAudioSyncDelay(long var1, long[] var3);

   private native int iosSetAudioSyncDelay(long var1, long var3);

   private native int iosPlay(long var1);

   private native int iosPause(long var1);

   private native int iosStop(long var1);

   private native int iosGetRate(long var1, float[] var3);

   private native int iosSetRate(long var1, float var3);

   private native int iosGetPresentationTime(long var1, double[] var3);

   private native int iosGetVolume(long var1, float[] var3);

   private native int iosSetVolume(long var1, float var3);

   private native int iosGetBalance(long var1, float[] var3);

   private native int iosSetBalance(long var1, float var3);

   private native int iosGetDuration(long var1, double[] var3);

   private native int iosSeek(long var1, double var3);

   private native void iosDispose(long var1);

   private native int iosFinish(long var1);

   private native int iosSetOverlayX(long var1, double var3);

   private native int iosSetOverlayY(long var1, double var3);

   private native int iosSetOverlayVisible(long var1, boolean var3);

   private native int iosSetOverlayWidth(long var1, double var3);

   private native int iosSetOverlayHeight(long var1, double var3);

   private native int iosSetOverlayPreserveRatio(long var1, boolean var3);

   private native int iosSetOverlayOpacity(long var1, double var3);

   private native int iosSetOverlayTransform(long var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25);

   private final class MediaPlayerOverlayImpl implements MediaPlayerOverlay {
      private MediaPlayerOverlayImpl() {
      }

      public void setOverlayX(double var1) {
         IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayX(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), var1));
      }

      public void setOverlayY(double var1) {
         IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayY(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), var1));
      }

      public void setOverlayVisible(boolean var1) {
         IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayVisible(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), var1));
      }

      public void setOverlayWidth(double var1) {
         IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayWidth(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), var1));
      }

      public void setOverlayHeight(double var1) {
         IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayHeight(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), var1));
      }

      public void setOverlayPreserveRatio(boolean var1) {
         IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayPreserveRatio(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), var1));
      }

      public void setOverlayOpacity(double var1) {
         IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayOpacity(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), var1));
      }

      public void setOverlayTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
         IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayTransform(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), var1, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23));
      }

      // $FF: synthetic method
      MediaPlayerOverlayImpl(Object var2) {
         this();
      }
   }

   private static final class NullEQBand implements EqualizerBand {
      private double center;
      private double bandwidth;
      private double gain;

      NullEQBand(double var1, double var3, double var5) {
         this.center = var1;
         this.bandwidth = var3;
         this.gain = var5;
      }

      public double getCenterFrequency() {
         return this.center;
      }

      public void setCenterFrequency(double var1) {
         this.center = var1;
      }

      public double getBandwidth() {
         return this.bandwidth;
      }

      public void setBandwidth(double var1) {
         this.bandwidth = var1;
      }

      public double getGain() {
         return this.gain;
      }

      public void setGain(double var1) {
         this.gain = var1;
      }
   }

   private static final class NullAudioSpectrum implements AudioSpectrum {
      private boolean enabled;
      private int bandCount;
      private double interval;
      private int threshold;
      private float[] fakeData;

      private NullAudioSpectrum() {
         this.enabled = false;
         this.bandCount = 128;
         this.interval = 0.1;
         this.threshold = 60;
      }

      public boolean getEnabled() {
         return this.enabled;
      }

      public void setEnabled(boolean var1) {
         this.enabled = var1;
      }

      public int getBandCount() {
         return this.bandCount;
      }

      public void setBandCount(int var1) {
         this.bandCount = var1;
         this.fakeData = new float[this.bandCount];
      }

      public double getInterval() {
         return this.interval;
      }

      public void setInterval(double var1) {
         this.interval = var1;
      }

      public int getSensitivityThreshold() {
         return this.threshold;
      }

      public void setSensitivityThreshold(int var1) {
         this.threshold = var1;
      }

      public float[] getMagnitudes(float[] var1) {
         int var2 = this.fakeData.length;
         if (var1 == null || var1.length < var2) {
            var1 = new float[var2];
         }

         System.arraycopy(this.fakeData, 0, var1, 0, var2);
         return var1;
      }

      public float[] getPhases(float[] var1) {
         int var2 = this.fakeData.length;
         if (var1 == null || var1.length < var2) {
            var1 = new float[var2];
         }

         System.arraycopy(this.fakeData, 0, var1, 0, var2);
         return var1;
      }

      // $FF: synthetic method
      NullAudioSpectrum(Object var1) {
         this();
      }
   }

   private static final class NullAudioEQ implements AudioEqualizer {
      private boolean enabled;
      private Map bands;

      private NullAudioEQ() {
         this.enabled = false;
         this.bands = new HashMap();
      }

      public boolean getEnabled() {
         return this.enabled;
      }

      public void setEnabled(boolean var1) {
         this.enabled = var1;
      }

      public EqualizerBand addBand(double var1, double var3, double var5) {
         Double var7 = new Double(var1);
         if (this.bands.containsKey(var7)) {
            this.removeBand(var1);
         }

         NullEQBand var8 = new NullEQBand(var1, var3, var5);
         this.bands.put(var7, var8);
         return var8;
      }

      public boolean removeBand(double var1) {
         Double var3 = new Double(var1);
         if (this.bands.containsKey(var3)) {
            this.bands.remove(var3);
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      NullAudioEQ(Object var1) {
         this();
      }
   }
}
