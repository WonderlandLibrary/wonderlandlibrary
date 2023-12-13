package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

final class NativeMediaAudioClip extends AudioClip {
   private URI sourceURI;
   private Locator mediaLocator;
   private AtomicInteger playCount;

   private NativeMediaAudioClip(URI var1) throws URISyntaxException, FileNotFoundException, IOException {
      this.sourceURI = var1;
      this.playCount = new AtomicInteger(0);
      if (Logger.canLog(1)) {
         Logger.logMsg(1, "Creating AudioClip for URI " + var1);
      }

      this.mediaLocator = new Locator(this.sourceURI);
      this.mediaLocator.init();
      this.mediaLocator.cacheMedia();
   }

   Locator getLocator() {
      return this.mediaLocator;
   }

   public static AudioClip load(URI var0) throws URISyntaxException, FileNotFoundException, IOException {
      return new NativeMediaAudioClip(var0);
   }

   public static AudioClip create(byte[] var0, int var1, int var2, int var3, int var4, int var5) {
      throw new UnsupportedOperationException("NativeMediaAudioClip does not support creating clips from raw sample data");
   }

   public AudioClip createSegment(double var1, double var3) throws IllegalArgumentException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public AudioClip createSegment(int var1, int var2) throws IllegalArgumentException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public AudioClip resample(int var1, int var2, int var3) throws IllegalArgumentException, IOException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public AudioClip append(AudioClip var1) throws IOException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public AudioClip flatten() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean isPlaying() {
      return this.playCount.get() > 0;
   }

   public void play() {
      this.play(this.clipVolume, this.clipBalance, this.clipRate, this.clipPan, this.loopCount, this.clipPriority);
   }

   public void play(double var1) {
      this.play(var1, this.clipBalance, this.clipRate, this.clipPan, this.loopCount, this.clipPriority);
   }

   public void play(double var1, double var3, double var5, double var7, int var9, int var10) {
      this.playCount.getAndIncrement();
      NativeMediaAudioClipPlayer.playClip(this, var1, var3, var5, var7, var9, var10);
   }

   public void stop() {
      NativeMediaAudioClipPlayer.stopPlayers(this.mediaLocator);
   }

   public static void stopAllClips() {
      NativeMediaAudioClipPlayer.stopPlayers((Locator)null);
   }

   void playFinished() {
      this.playCount.decrementAndGet();
   }
}
