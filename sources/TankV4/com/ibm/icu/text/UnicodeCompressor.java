package com.ibm.icu.text;

public final class UnicodeCompressor implements SCSU {
   private static boolean[] sSingleTagTable = new boolean[]{false, true, true, true, true, true, true, true, true, false, false, true, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
   private static boolean[] sUnicodeTagTable = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false};
   private int fCurrentWindow = 0;
   private int[] fOffsets = new int[8];
   private int fMode = 0;
   private int[] fIndexCount = new int[256];
   private int[] fTimeStamps = new int[8];
   private int fTimeStamp = 0;

   public UnicodeCompressor() {
      this.reset();
   }

   public static byte[] compress(String var0) {
      return compress(var0.toCharArray(), 0, var0.length());
   }

   public static byte[] compress(char[] var0, int var1, int var2) {
      UnicodeCompressor var3 = new UnicodeCompressor();
      int var4 = Math.max(4, 3 * (var2 - var1) + 1);
      byte[] var5 = new byte[var4];
      int var6 = var3.compress(var0, var1, var2, (int[])null, var5, 0, var4);
      byte[] var7 = new byte[var6];
      System.arraycopy(var5, 0, var7, 0, var6);
      return var7;
   }

   public int compress(char[] param1, int param2, int param3, int[] param4, byte[] param5, int param6, int param7) {
      // $FF: Couldn't be decompiled
   }

   public void reset() {
      this.fOffsets[0] = 128;
      this.fOffsets[1] = 192;
      this.fOffsets[2] = 1024;
      this.fOffsets[3] = 1536;
      this.fOffsets[4] = 2304;
      this.fOffsets[5] = 12352;
      this.fOffsets[6] = 12448;
      this.fOffsets[7] = 65280;

      int var1;
      for(var1 = 0; var1 < 8; ++var1) {
         this.fTimeStamps[var1] = 0;
      }

      for(var1 = 0; var1 <= 255; ++var1) {
         this.fIndexCount[var1] = 0;
      }

      this.fTimeStamp = 0;
      this.fCurrentWindow = 0;
      this.fMode = 0;
   }

   private static int makeIndex(int var0) {
      if (var0 >= 192 && var0 < 320) {
         return 249;
      } else if (var0 >= 592 && var0 < 720) {
         return 250;
      } else if (var0 >= 880 && var0 < 1008) {
         return 251;
      } else if (var0 >= 1328 && var0 < 1424) {
         return 252;
      } else if (var0 >= 12352 && var0 < 12448) {
         return 253;
      } else if (var0 >= 12448 && var0 < 12576) {
         return 254;
      } else if (var0 >= 65376 && var0 < 65439) {
         return 255;
      } else if (var0 >= 128 && var0 < 13312) {
         return var0 / 128 & 255;
      } else {
         return var0 >= 57344 && var0 <= 65535 ? (var0 - '가') / 128 & 255 : 0;
      }
   }

   private int findDynamicWindow(int var1) {
      for(int var2 = 7; var2 >= 0; --var2) {
         if (var1 >= var2) {
            int var10003 = this.fTimeStamps[var2]++;
            return var2;
         }
      }

      return -1;
   }

   private static int findStaticWindow(int var0) {
      for(int var1 = 7; var1 >= 0; --var1) {
         if (var0 >= var1) {
            return var1;
         }
      }

      return -1;
   }

   private int getLRDefinedWindow() {
      int var1 = Integer.MAX_VALUE;
      int var2 = -1;

      for(int var3 = 7; var3 >= 0; --var3) {
         if (this.fTimeStamps[var3] < var1) {
            var1 = this.fTimeStamps[var3];
            var2 = var3;
         }
      }

      return var2;
   }
}
