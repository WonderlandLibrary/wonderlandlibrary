package com.sun.media.jfxmedia.track;

import java.util.Locale;

public class AudioTrack extends Track {
   public static final int UNKNOWN = 0;
   public static final int FRONT_LEFT = 1;
   public static final int FRONT_RIGHT = 2;
   public static final int FRONT_CENTER = 4;
   public static final int REAR_LEFT = 8;
   public static final int REAR_RIGHT = 16;
   public static final int REAR_CENTER = 32;
   private int numChannels;
   private int channelMask;
   private float encodedSampleRate;

   public AudioTrack(boolean var1, long var2, String var4, Locale var5, Track.Encoding var6, int var7, int var8, float var9) {
      super(var1, var2, var4, var5, var6);
      if (var7 < 1) {
         throw new IllegalArgumentException("numChannels < 1!");
      } else if (var9 <= 0.0F) {
         throw new IllegalArgumentException("encodedSampleRate <= 0.0");
      } else {
         this.numChannels = var7;
         this.channelMask = var8;
         this.encodedSampleRate = var9;
      }
   }

   public int getNumChannels() {
      return this.numChannels;
   }

   public int getChannelMask() {
      return this.channelMask;
   }

   public float getEncodedSampleRate() {
      return this.encodedSampleRate;
   }

   public final String toString() {
      return "AudioTrack {\n    name: " + this.getName() + "\n    encoding: " + this.getEncodingType() + "\n    language: " + this.getLocale() + "\n    numChannels: " + this.numChannels + "\n    channelMask: " + this.channelMask + "\n    encodedSampleRate: " + this.encodedSampleRate + "\n}";
   }
}
