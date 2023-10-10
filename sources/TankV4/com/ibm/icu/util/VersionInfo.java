package com.ibm.icu.util;

import java.util.concurrent.ConcurrentHashMap;

public final class VersionInfo implements Comparable {
   public static final VersionInfo UNICODE_1_0 = getInstance(1, 0, 0, 0);
   public static final VersionInfo UNICODE_1_0_1 = getInstance(1, 0, 1, 0);
   public static final VersionInfo UNICODE_1_1_0 = getInstance(1, 1, 0, 0);
   public static final VersionInfo UNICODE_1_1_5 = getInstance(1, 1, 5, 0);
   public static final VersionInfo UNICODE_2_0 = getInstance(2, 0, 0, 0);
   public static final VersionInfo UNICODE_2_1_2 = getInstance(2, 1, 2, 0);
   public static final VersionInfo UNICODE_2_1_5 = getInstance(2, 1, 5, 0);
   public static final VersionInfo UNICODE_2_1_8 = getInstance(2, 1, 8, 0);
   public static final VersionInfo UNICODE_2_1_9 = getInstance(2, 1, 9, 0);
   public static final VersionInfo UNICODE_3_0 = getInstance(3, 0, 0, 0);
   public static final VersionInfo UNICODE_3_0_1 = getInstance(3, 0, 1, 0);
   public static final VersionInfo UNICODE_3_1_0 = getInstance(3, 1, 0, 0);
   public static final VersionInfo UNICODE_3_1_1 = getInstance(3, 1, 1, 0);
   public static final VersionInfo UNICODE_3_2 = getInstance(3, 2, 0, 0);
   public static final VersionInfo UNICODE_4_0 = getInstance(4, 0, 0, 0);
   public static final VersionInfo UNICODE_4_0_1 = getInstance(4, 0, 1, 0);
   public static final VersionInfo UNICODE_4_1 = getInstance(4, 1, 0, 0);
   public static final VersionInfo UNICODE_5_0 = getInstance(5, 0, 0, 0);
   public static final VersionInfo UNICODE_5_1 = getInstance(5, 1, 0, 0);
   public static final VersionInfo UNICODE_5_2 = getInstance(5, 2, 0, 0);
   public static final VersionInfo UNICODE_6_0 = getInstance(6, 0, 0, 0);
   public static final VersionInfo UNICODE_6_1 = getInstance(6, 1, 0, 0);
   public static final VersionInfo UNICODE_6_2 = getInstance(6, 2, 0, 0);
   public static final VersionInfo ICU_VERSION = getInstance(51, 2, 0, 0);
   /** @deprecated */
   public static final String ICU_DATA_VERSION_PATH = "51b";
   /** @deprecated */
   public static final VersionInfo ICU_DATA_VERSION = getInstance(51, 2, 0, 0);
   public static final VersionInfo UCOL_RUNTIME_VERSION;
   public static final VersionInfo UCOL_BUILDER_VERSION;
   public static final VersionInfo UCOL_TAILORINGS_VERSION;
   private static volatile VersionInfo javaVersion;
   private static final VersionInfo UNICODE_VERSION;
   private int m_version_;
   private static final ConcurrentHashMap MAP_ = new ConcurrentHashMap();
   private static final int LAST_BYTE_MASK_ = 255;
   private static final String INVALID_VERSION_NUMBER_ = "Invalid version number: Version number may be negative or greater than 255";

   public static VersionInfo getInstance(String var0) {
      int var1 = var0.length();
      int[] var2 = new int[]{0, 0, 0, 0};
      int var3 = 0;

      int var4;
      for(var4 = 0; var3 < 4 && var4 < var1; ++var4) {
         char var6 = var0.charAt(var4);
         if (var6 == '.') {
            ++var3;
         } else {
            var6 = (char)(var6 - 48);
            if (var6 < 0 || var6 > '\t') {
               throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
            }

            var2[var3] *= 10;
            var2[var3] += var6;
         }
      }

      if (var4 != var1) {
         throw new IllegalArgumentException("Invalid version number: String '" + var0 + "' exceeds version format");
      } else {
         for(int var5 = 0; var5 < 4; ++var5) {
            if (var2[var5] < 0 || var2[var5] > 255) {
               throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
            }
         }

         return getInstance(var2[0], var2[1], var2[2], var2[3]);
      }
   }

   public static VersionInfo getInstance(int var0, int var1, int var2, int var3) {
      if (var0 >= 0 && var0 <= 255 && var1 >= 0 && var1 <= 255 && var2 >= 0 && var2 <= 255 && var3 >= 0 && var3 <= 255) {
         int var4 = getInt(var0, var1, var2, var3);
         Integer var5 = var4;
         VersionInfo var6 = (VersionInfo)MAP_.get(var5);
         if (var6 == null) {
            var6 = new VersionInfo(var4);
            VersionInfo var7 = (VersionInfo)MAP_.putIfAbsent(var5, var6);
            if (var7 != null) {
               var6 = var7;
            }
         }

         return var6;
      } else {
         throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
      }
   }

   public static VersionInfo getInstance(int var0, int var1, int var2) {
      return getInstance(var0, var1, var2, 0);
   }

   public static VersionInfo getInstance(int var0, int var1) {
      return getInstance(var0, var1, 0, 0);
   }

   public static VersionInfo getInstance(int var0) {
      return getInstance(var0, 0, 0, 0);
   }

   /** @deprecated */
   public static VersionInfo javaVersion() {
      if (javaVersion == null) {
         Class var0 = VersionInfo.class;
         synchronized(VersionInfo.class){}
         if (javaVersion == null) {
            String var1 = System.getProperty("java.version");
            char[] var2 = var1.toCharArray();
            int var3 = 0;
            int var4 = 0;
            int var5 = 0;
            boolean var6 = false;

            while(var3 < var2.length) {
               char var7 = var2[var3++];
               if (var7 >= '0' && var7 <= '9') {
                  var6 = true;
                  var2[var4++] = var7;
               } else if (var6) {
                  if (var5 == 3) {
                     break;
                  }

                  var6 = false;
                  var2[var4++] = '.';
                  ++var5;
               }
            }

            while(var4 > 0 && var2[var4 - 1] == '.') {
               --var4;
            }

            String var9 = new String(var2, 0, var4);
            javaVersion = getInstance(var9);
         }
      }

      return javaVersion;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(7);
      var1.append(this.getMajor());
      var1.append('.');
      var1.append(this.getMinor());
      var1.append('.');
      var1.append(this.getMilli());
      var1.append('.');
      var1.append(this.getMicro());
      return var1.toString();
   }

   public int getMajor() {
      return this.m_version_ >> 24 & 255;
   }

   public int getMinor() {
      return this.m_version_ >> 16 & 255;
   }

   public int getMilli() {
      return this.m_version_ >> 8 & 255;
   }

   public int getMicro() {
      return this.m_version_ & 255;
   }

   public boolean equals(Object var1) {
      return var1 == this;
   }

   public int compareTo(VersionInfo var1) {
      return this.m_version_ - var1.m_version_;
   }

   private VersionInfo(int var1) {
      this.m_version_ = var1;
   }

   private static int getInt(int var0, int var1, int var2, int var3) {
      return var0 << 24 | var1 << 16 | var2 << 8 | var3;
   }

   public static void main(String[] var0) {
      String var1;
      if (ICU_VERSION.getMajor() <= 4) {
         if (ICU_VERSION.getMinor() % 2 != 0) {
            int var2 = ICU_VERSION.getMajor();
            int var3 = ICU_VERSION.getMinor() + 1;
            if (var3 >= 10) {
               var3 -= 10;
               ++var2;
            }

            var1 = "" + var2 + "." + var3 + "M" + ICU_VERSION.getMilli();
         } else {
            var1 = ICU_VERSION.getVersionString(2, 2);
         }
      } else if (ICU_VERSION.getMinor() == 0) {
         var1 = "" + ICU_VERSION.getMajor() + "M" + ICU_VERSION.getMilli();
      } else {
         var1 = ICU_VERSION.getVersionString(2, 2);
      }

      System.out.println("International Components for Unicode for Java " + var1);
      System.out.println("");
      System.out.println("Implementation Version: " + ICU_VERSION.getVersionString(2, 4));
      System.out.println("Unicode Data Version:   " + UNICODE_VERSION.getVersionString(2, 4));
      System.out.println("CLDR Data Version:      " + LocaleData.getCLDRVersion().getVersionString(2, 4));
      System.out.println("Time Zone Data Version: " + TimeZone.getTZDataVersion());
   }

   private String getVersionString(int var1, int var2) {
      if (var1 >= 1 && var2 >= 1 && var1 <= 4 && var2 <= 4 && var1 <= var2) {
         int[] var3 = new int[]{this.getMajor(), this.getMinor(), this.getMilli(), this.getMicro()};

         int var4;
         for(var4 = var2; var4 > var1 && var3[var4 - 1] == 0; --var4) {
         }

         StringBuilder var5 = new StringBuilder(7);
         var5.append(var3[0]);

         for(int var6 = 1; var6 < var4; ++var6) {
            var5.append(".");
            var5.append(var3[var6]);
         }

         return var5.toString();
      } else {
         throw new IllegalArgumentException("Invalid min/maxDigits range");
      }
   }

   public int compareTo(Object var1) {
      return this.compareTo((VersionInfo)var1);
   }

   static {
      UNICODE_VERSION = UNICODE_6_2;
      UCOL_RUNTIME_VERSION = getInstance(7);
      UCOL_BUILDER_VERSION = getInstance(8);
      UCOL_TAILORINGS_VERSION = getInstance(1);
   }
}
