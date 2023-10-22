package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;

final class GSTMediaPlayer extends NativeMediaPlayer {
   private GSTMedia gstMedia;
   private float mutedVolume;
   private boolean muteEnabled;
   private AudioEqualizer audioEqualizer;
   private AudioSpectrum audioSpectrum;

   private GSTMediaPlayer(GSTMedia var1) {
      super(var1);
      this.gstMedia = null;
      this.mutedVolume = 1.0F;
      this.muteEnabled = false;
      this.init();
      this.gstMedia = var1;
      int var2 = this.gstInitPlayer(this.gstMedia.getNativeMediaRef());
      if (0 != var2) {
         this.dispose();
         this.throwMediaErrorException(var2, (String)null);
      }

      long var3 = this.gstMedia.getNativeMediaRef();
      this.audioSpectrum = this.createNativeAudioSpectrum(this.gstGetAudioSpectrum(var3));
      this.audioEqualizer = this.createNativeAudioEqualizer(this.gstGetAudioEqualizer(var3));
   }

   GSTMediaPlayer(Locator var1) {
      this(new GSTMedia(var1));
   }

   public AudioEqualizer getEqualizer() {
      return this.audioEqualizer;
   }

   public AudioSpectrum getAudioSpectrum() {
      return this.audioSpectrum;
   }

   public MediaPlayerOverlay getMediaPlayerOverlay() {
      return null;
   }

   private void throwMediaErrorException(int var1, String var2) throws MediaException {
      MediaError var3 = MediaError.getFromCode(var1);
      throw new MediaException(var2, (Throwable)null, var3);
   }

   protected long playerGetAudioSyncDelay() throws MediaException {
      long[] var1 = new long[1];
      int var2 = this.gstGetAudioSyncDelay(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var2) {
         this.throwMediaErrorException(var2, (String)null);
      }

      return var1[0];
   }

   protected void playerSetAudioSyncDelay(long var1) throws MediaException {
      int var3 = this.gstSetAudioSyncDelay(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var3) {
         this.throwMediaErrorException(var3, (String)null);
      }

   }

   protected void playerPlay() throws MediaException {
      int var1 = this.gstPlay(this.gstMedia.getNativeMediaRef());
      if (0 != var1) {
         this.throwMediaErrorException(var1, (String)null);
      }

   }

   protected void playerStop() throws MediaException {
      int var1 = this.gstStop(this.gstMedia.getNativeMediaRef());
      if (0 != var1) {
         this.throwMediaErrorException(var1, (String)null);
      }

   }

   protected void playerPause() throws MediaException {
      int var1 = this.gstPause(this.gstMedia.getNativeMediaRef());
      if (0 != var1) {
         this.throwMediaErrorException(var1, (String)null);
      }

   }

   protected void playerFinish() throws MediaException {
      int var1 = this.gstFinish(this.gstMedia.getNativeMediaRef());
      if (0 != var1) {
         this.throwMediaErrorException(var1, (String)null);
      }

   }

   protected float playerGetRate() throws MediaException {
      float[] var1 = new float[1];
      int var2 = this.gstGetRate(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var2) {
         this.throwMediaErrorException(var2, (String)null);
      }

      return var1[0];
   }

   protected void playerSetRate(float var1) throws MediaException {
      int var2 = this.gstSetRate(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var2) {
         this.throwMediaErrorException(var2, (String)null);
      }

   }

   protected double playerGetPresentationTime() throws MediaException {
      double[] var1 = new double[1];
      int var2 = this.gstGetPresentationTime(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var2) {
         this.throwMediaErrorException(var2, (String)null);
      }

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
      int var2 = this.gstGetVolume(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var2) {
         this.throwMediaErrorException(var2, (String)null);
      }

      return var1[0];
   }

   protected synchronized void playerSetVolume(float var1) throws MediaException {
      if (!this.muteEnabled) {
         int var2 = this.gstSetVolume(this.gstMedia.getNativeMediaRef(), var1);
         if (0 != var2) {
            this.throwMediaErrorException(var2, (String)null);
         } else {
            this.mutedVolume = var1;
         }
      } else {
         this.mutedVolume = var1;
      }

   }

   protected float playerGetBalance() throws MediaException {
      float[] var1 = new float[1];
      int var2 = this.gstGetBalance(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var2) {
         this.throwMediaErrorException(var2, (String)null);
      }

      return var1[0];
   }

   protected void playerSetBalance(float var1) throws MediaException {
      int var2 = this.gstSetBalance(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var2) {
         this.throwMediaErrorException(var2, (String)null);
      }

   }

   protected double playerGetDuration() throws MediaException {
      double[] var1 = new double[1];
      int var2 = this.gstGetDuration(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var2) {
         this.throwMediaErrorException(var2, (String)null);
      }

      return var1[0] == -1.0 ? Double.POSITIVE_INFINITY : var1[0];
   }

   protected void playerSeek(double var1) throws MediaException {
      int var3 = this.gstSeek(this.gstMedia.getNativeMediaRef(), var1);
      if (0 != var3) {
         this.throwMediaErrorException(var3, (String)null);
      }

   }

   protected void playerInit() throws MediaException {
   }

   protected void playerDispose() {
      this.audioEqualizer = null;
      this.audioSpectrum = null;
      this.gstMedia = null;
   }

   private native int gstInitPlayer(long var1);

   private native long gstGetAudioEqualizer(long var1);

   private native long gstGetAudioSpectrum(long var1);

   private native int gstGetAudioSyncDelay(long var1, long[] var3);

   private native int gstSetAudioSyncDelay(long var1, long var3);

   private native int gstPlay(long var1);

   private native int gstPause(long var1);

   private native int gstStop(long var1);

   private native int gstFinish(long var1);

   private native int gstGetRate(long var1, float[] var3);

   private native int gstSetRate(long var1, float var3);

   private native int gstGetPresentationTime(long var1, double[] var3);

   private native int gstGetVolume(long var1, float[] var3);

   private native int gstSetVolume(long var1, float var3);

   private native int gstGetBalance(long var1, float[] var3);

   private native int gstSetBalance(long var1, float var3);

   private native int gstGetDuration(long var1, double[] var3);

   private native int gstSeek(long var1, double var3);
}
