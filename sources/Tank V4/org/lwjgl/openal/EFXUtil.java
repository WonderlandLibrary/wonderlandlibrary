package org.lwjgl.openal;

public final class EFXUtil {
   private static final int EFFECT = 1111;
   private static final int FILTER = 2222;

   private EFXUtil() {
   }

   public static boolean isEffectSupported(int var0) {
      switch(var0) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 32768:
         return testSupportGeneric(1111, var0);
      default:
         throw new IllegalArgumentException("Unknown or invalid effect type: " + var0);
      }
   }

   public static boolean isFilterSupported(int var0) {
      switch(var0) {
      case 0:
      case 1:
      case 2:
      case 3:
         return testSupportGeneric(2222, var0);
      default:
         throw new IllegalArgumentException("Unknown or invalid filter type: " + var0);
      }
   }

   private static boolean testSupportGeneric(int param0, int param1) {
      // $FF: Couldn't be decompiled
   }
}
