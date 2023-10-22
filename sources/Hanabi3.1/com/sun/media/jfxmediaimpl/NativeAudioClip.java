package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.locator.Locator;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

final class NativeAudioClip extends AudioClip {
   private final Locator mediaSource;
   private long nativeHandle = 0L;
   private static NativeAudioClipDisposer clipDisposer = new NativeAudioClipDisposer();

   private static native boolean nacInit();

   private static native long nacLoad(Locator var0);

   private static native long nacCreate(byte[] var0, int var1, int var2, int var3, int var4, int var5);

   private static native void nacUnload(long var0);

   private static native void nacStopAll();

   public static synchronized boolean init() {
      return nacInit();
   }

   public static AudioClip load(URI var0) {
      NativeAudioClip var1 = null;

      try {
         Locator var2 = new Locator(var0);
         var2.init();
         var1 = new NativeAudioClip(var2);
      } catch (URISyntaxException var3) {
         throw new MediaException("Non-compliant URI", var3);
      } catch (IOException var4) {
         throw new MediaException("Cannot connect to media", var4);
      }

      if (null != var1 && 0L != var1.getNativeHandle()) {
         MediaDisposer.addResourceDisposer(var1, var1.getNativeHandle(), clipDisposer);
         return var1;
      } else {
         var1 = null;
         throw new MediaException("Cannot create audio clip");
      }
   }

   public static AudioClip create(byte[] var0, int var1, int var2, int var3, int var4, int var5) {
      NativeAudioClip var6 = new NativeAudioClip(var0, var1, var2, var3, var4, var5);
      if (null != var6 && 0L != var6.getNativeHandle()) {
         MediaDisposer.addResourceDisposer(var6, var6.getNativeHandle(), clipDisposer);
         return var6;
      } else {
         var6 = null;
         throw new MediaException("Cannot create audio clip");
      }
   }

   private native NativeAudioClip nacCreateSegment(long var1, double var3, double var5);

   private native NativeAudioClip nacCreateSegment(long var1, int var3, int var4);

   private native NativeAudioClip nacResample(long var1, int var3, int var4, int var5);

   private native NativeAudioClip nacAppend(long var1, long var3);

   private native boolean nacIsPlaying(long var1);

   private native void nacPlay(long var1, double var3, double var5, double var7, double var9, int var11, int var12);

   private native void nacStop(long var1);

   private NativeAudioClip(Locator var1) {
      this.mediaSource = var1;
      this.nativeHandle = nacLoad(this.mediaSource);
   }

   private NativeAudioClip(byte[] var1, int var2, int var3, int var4, int var5, int var6) {
      this.mediaSource = null;
      this.nativeHandle = nacCreate(var1, var2, var3, var4, var5, var6);
   }

   long getNativeHandle() {
      return this.nativeHandle;
   }

   public AudioClip createSegment(double var1, double var3) {
      return this.nacCreateSegment(this.nativeHandle, var1, var3);
   }

   public AudioClip createSegment(int var1, int var2) {
      return this.nacCreateSegment(this.nativeHandle, var1, var2);
   }

   public AudioClip resample(int var1, int var2, int var3) {
      return this.nacResample(this.nativeHandle, var1, var2, var3);
   }

   public AudioClip append(AudioClip var1) {
      if (!(var1 instanceof NativeAudioClip)) {
         throw new IllegalArgumentException("AudioClip type mismatch, cannot append");
      } else {
         return this.nacAppend(this.nativeHandle, ((NativeAudioClip)var1).getNativeHandle());
      }
   }

   public AudioClip flatten() {
      return this;
   }

   public boolean isPlaying() {
      return this.nacIsPlaying(this.nativeHandle);
   }

   public void play() {
      this.nacPlay(this.nativeHandle, this.clipVolume, this.clipBalance, this.clipPan, this.clipRate, this.loopCount, this.clipPriority);
   }

   public void play(double var1) {
      this.nacPlay(this.nativeHandle, var1, this.clipBalance, this.clipPan, this.clipRate, this.loopCount, this.clipPriority);
   }

   public void play(double var1, double var3, double var5, double var7, int var9, int var10) {
      this.nacPlay(this.nativeHandle, var1, var3, var7, var5, var9, var10);
   }

   public void stop() {
      this.nacStop(this.nativeHandle);
   }

   public static void stopAllClips() {
      nacStopAll();
   }

   private static class NativeAudioClipDisposer implements MediaDisposer.ResourceDisposer {
      private NativeAudioClipDisposer() {
      }

      public void disposeResource(Object var1) {
         long var2 = (Long)var1;
         if (0L != var2) {
            NativeAudioClip.nacUnload(var2);
         }

      }

      // $FF: synthetic method
      NativeAudioClipDisposer(Object var1) {
         this();
      }
   }
}
