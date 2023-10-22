package com.sun.media.jfxmedia.track;

import java.util.Locale;

public abstract class Track {
   private boolean trackEnabled;
   private long trackID;
   private String name;
   private Locale locale;
   private Encoding encoding;

   protected Track(boolean var1, long var2, String var4, Locale var5, Encoding var6) {
      if (var4 == null) {
         throw new IllegalArgumentException("name == null!");
      } else if (var6 == null) {
         throw new IllegalArgumentException("encoding == null!");
      } else {
         this.trackEnabled = var1;
         this.trackID = var2;
         this.locale = var5;
         this.encoding = var6;
         this.name = var4;
      }
   }

   public Encoding getEncodingType() {
      return this.encoding;
   }

   public String getName() {
      return this.name;
   }

   public Locale getLocale() {
      return this.locale;
   }

   public long getTrackID() {
      return this.trackID;
   }

   public boolean isEnabled() {
      return this.trackEnabled;
   }

   public static enum Encoding {
      NONE,
      PCM,
      MPEG1AUDIO,
      MPEG1LAYER3,
      AAC,
      H264,
      VP6,
      CUSTOM;

      public static Encoding toEncoding(int var0) {
         Encoding[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Encoding var4 = var1[var3];
            if (var4.ordinal() == var0) {
               return var4;
            }
         }

         return NONE;
      }
   }
}
