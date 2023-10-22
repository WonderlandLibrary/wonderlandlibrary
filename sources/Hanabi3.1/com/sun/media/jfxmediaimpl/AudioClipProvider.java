package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AudioClipProvider {
   private static AudioClipProvider primaDonna;
   private boolean useNative = false;

   public static synchronized AudioClipProvider getProvider() {
      if (null == primaDonna) {
         primaDonna = new AudioClipProvider();
      }

      return primaDonna;
   }

   private AudioClipProvider() {
      try {
         this.useNative = NativeAudioClip.init();
      } catch (UnsatisfiedLinkError var2) {
         Logger.logMsg(1, "JavaFX AudioClip native methods not linked, using NativeMedia implementation");
      } catch (Exception var3) {
         Logger.logMsg(4, "Exception while loading native AudioClip library: " + var3);
      }

   }

   public AudioClip load(URI var1) throws URISyntaxException, FileNotFoundException, IOException {
      return this.useNative ? NativeAudioClip.load(var1) : NativeMediaAudioClip.load(var1);
   }

   public AudioClip create(byte[] var1, int var2, int var3, int var4, int var5, int var6) throws IllegalArgumentException {
      return this.useNative ? NativeAudioClip.create(var1, var2, var3, var4, var5, var6) : NativeMediaAudioClip.create(var1, var2, var3, var4, var5, var6);
   }

   public void stopAllClips() {
      if (this.useNative) {
         NativeAudioClip.stopAllClips();
      }

      NativeMediaAudioClip.stopAllClips();
   }
}
