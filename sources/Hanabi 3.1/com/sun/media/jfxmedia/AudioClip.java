package com.sun.media.jfxmedia;

import com.sun.media.jfxmediaimpl.AudioClipProvider;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class AudioClip {
   protected int clipPriority = 0;
   protected int loopCount = 0;
   protected double clipVolume = 1.0;
   protected double clipBalance = 0.0;
   protected double clipRate = 1.0;
   protected double clipPan = 0.0;
   public static final int SAMPLE_FORMAT_S8 = 0;
   public static final int SAMPLE_FORMAT_U8 = 1;
   public static final int SAMPLE_FORMAT_S16BE = 2;
   public static final int SAMPLE_FORMAT_U16BE = 3;
   public static final int SAMPLE_FORMAT_S16LE = 4;
   public static final int SAMPLE_FORMAT_U16LE = 5;
   public static final int SAMPLE_FORMAT_S24BE = 6;
   public static final int SAMPLE_FORMAT_U24BE = 7;
   public static final int SAMPLE_FORMAT_S24LE = 8;
   public static final int SAMPLE_FORMAT_U24LE = 9;

   public static AudioClip load(URI var0) throws URISyntaxException, FileNotFoundException, IOException {
      return AudioClipProvider.getProvider().load(var0);
   }

   public static AudioClip create(byte[] var0, int var1, int var2, int var3, int var4, int var5) throws IllegalArgumentException {
      return AudioClipProvider.getProvider().create(var0, var1, var2, var3, var4, var5);
   }

   public static void stopAllClips() {
      AudioClipProvider.getProvider().stopAllClips();
   }

   public abstract AudioClip createSegment(double var1, double var3) throws IllegalArgumentException;

   public abstract AudioClip createSegment(int var1, int var2) throws IllegalArgumentException;

   public abstract AudioClip resample(int var1, int var2, int var3) throws IllegalArgumentException, IOException;

   public abstract AudioClip append(AudioClip var1) throws IOException;

   public abstract AudioClip flatten();

   public int priority() {
      return this.clipPriority;
   }

   public void setPriority(int var1) {
      this.clipPriority = var1;
   }

   public int loopCount() {
      return this.loopCount;
   }

   public void setLoopCount(int var1) {
      this.loopCount = var1;
   }

   public double volume() {
      return this.clipVolume;
   }

   public void setVolume(double var1) {
      this.clipVolume = var1;
   }

   public double balance() {
      return this.clipBalance;
   }

   public void setBalance(double var1) {
      this.clipBalance = var1;
   }

   public double playbackRate() {
      return this.clipRate;
   }

   public void setPlaybackRate(double var1) {
      this.clipRate = var1;
   }

   public double pan() {
      return this.clipPan;
   }

   public void setPan(double var1) {
      this.clipPan = var1;
   }

   public abstract boolean isPlaying();

   public abstract void play();

   public abstract void play(double var1);

   public abstract void play(double var1, double var3, double var5, double var7, int var9, int var10);

   public abstract void stop();
}
