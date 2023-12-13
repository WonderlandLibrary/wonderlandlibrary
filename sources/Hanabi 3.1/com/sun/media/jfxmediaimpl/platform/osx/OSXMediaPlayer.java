package com.sun.media.jfxmediaimpl.platform.osx;

import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;

final class OSXMediaPlayer extends NativeMediaPlayer {
   private final AudioEqualizer audioEq;
   private final AudioSpectrum audioSpectrum;
   private final Locator mediaLocator;

   OSXMediaPlayer(NativeMedia var1) {
      super(var1);
      this.init();
      this.mediaLocator = var1.getLocator();
      this.osxCreatePlayer(this.mediaLocator.getStringLocation());
      this.audioEq = this.createNativeAudioEqualizer(this.osxGetAudioEqualizerRef());
      this.audioSpectrum = this.createNativeAudioSpectrum(this.osxGetAudioSpectrumRef());
   }

   OSXMediaPlayer(Locator var1) {
      this((NativeMedia)(new OSXMedia(var1)));
   }

   public AudioEqualizer getEqualizer() {
      return this.audioEq;
   }

   public AudioSpectrum getAudioSpectrum() {
      return this.audioSpectrum;
   }

   public MediaPlayerOverlay getMediaPlayerOverlay() {
      return null;
   }

   protected long playerGetAudioSyncDelay() throws MediaException {
      return this.osxGetAudioSyncDelay();
   }

   protected void playerSetAudioSyncDelay(long var1) throws MediaException {
      this.osxSetAudioSyncDelay(var1);
   }

   protected void playerPlay() throws MediaException {
      this.osxPlay();
   }

   protected void playerStop() throws MediaException {
      this.osxStop();
   }

   protected void playerPause() throws MediaException {
      this.osxPause();
   }

   protected void playerFinish() throws MediaException {
      this.osxFinish();
   }

   protected float playerGetRate() throws MediaException {
      return this.osxGetRate();
   }

   protected void playerSetRate(float var1) throws MediaException {
      this.osxSetRate(var1);
   }

   protected double playerGetPresentationTime() throws MediaException {
      return this.osxGetPresentationTime();
   }

   protected boolean playerGetMute() throws MediaException {
      return this.osxGetMute();
   }

   protected void playerSetMute(boolean var1) throws MediaException {
      this.osxSetMute(var1);
   }

   protected float playerGetVolume() throws MediaException {
      return this.osxGetVolume();
   }

   protected void playerSetVolume(float var1) throws MediaException {
      this.osxSetVolume(var1);
   }

   protected float playerGetBalance() throws MediaException {
      return this.osxGetBalance();
   }

   protected void playerSetBalance(float var1) throws MediaException {
      this.osxSetBalance(var1);
   }

   protected double playerGetDuration() throws MediaException {
      double var1 = this.osxGetDuration();
      return var1 == -1.0 ? Double.POSITIVE_INFINITY : var1;
   }

   protected void playerSeek(double var1) throws MediaException {
      this.osxSeek(var1);
   }

   protected void playerDispose() {
      this.osxDispose();
   }

   public void playerInit() throws MediaException {
   }

   private native void osxCreatePlayer(String var1) throws MediaException;

   private native long osxGetAudioEqualizerRef();

   private native long osxGetAudioSpectrumRef();

   private native long osxGetAudioSyncDelay() throws MediaException;

   private native void osxSetAudioSyncDelay(long var1) throws MediaException;

   private native void osxPlay() throws MediaException;

   private native void osxStop() throws MediaException;

   private native void osxPause() throws MediaException;

   private native void osxFinish() throws MediaException;

   private native float osxGetRate() throws MediaException;

   private native void osxSetRate(float var1) throws MediaException;

   private native double osxGetPresentationTime() throws MediaException;

   private native boolean osxGetMute() throws MediaException;

   private native void osxSetMute(boolean var1) throws MediaException;

   private native float osxGetVolume() throws MediaException;

   private native void osxSetVolume(float var1) throws MediaException;

   private native float osxGetBalance() throws MediaException;

   private native void osxSetBalance(float var1) throws MediaException;

   private native double osxGetDuration() throws MediaException;

   private native void osxSeek(double var1) throws MediaException;

   private native void osxDispose();
}
