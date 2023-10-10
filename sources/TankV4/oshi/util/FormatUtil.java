package oshi.util;

import java.math.BigDecimal;

public abstract class FormatUtil {
   private static final long kibiByte = 1024L;
   private static final long mebiByte = 1048576L;
   private static final long gibiByte = 1073741824L;
   private static final long tebiByte = 1099511627776L;
   private static final long pebiByte = 1125899906842624L;

   public static String formatBytes(long var0) {
      if (var0 == 1L) {
         return String.format("%d byte", var0);
      } else if (var0 < 1024L) {
         return String.format("%d bytes", var0);
      } else if (var0 < 1048576L && var0 % 1024L == 0L) {
         return String.format("%.0f KB", (double)var0 / 1024.0D);
      } else if (var0 < 1048576L) {
         return String.format("%.1f KB", (double)var0 / 1024.0D);
      } else if (var0 < 1073741824L && var0 % 1048576L == 0L) {
         return String.format("%.0f MB", (double)var0 / 1048576.0D);
      } else if (var0 < 1073741824L) {
         return String.format("%.1f MB", (double)var0 / 1048576.0D);
      } else if (var0 % 1073741824L == 0L && var0 < 1099511627776L) {
         return String.format("%.0f GB", (double)var0 / 1.073741824E9D);
      } else if (var0 < 1099511627776L) {
         return String.format("%.1f GB", (double)var0 / 1.073741824E9D);
      } else if (var0 % 1099511627776L == 0L && var0 < 1125899906842624L) {
         return String.format("%.0f TiB", (double)var0 / 1.099511627776E12D);
      } else {
         return var0 < 1125899906842624L ? String.format("%.1f TiB", (double)var0 / 1.099511627776E12D) : String.format("%d bytes", var0);
      }
   }

   public static float round(float var0, int var1) {
      BigDecimal var2 = new BigDecimal(Float.toString(var0));
      var2 = var2.setScale(var1, 4);
      return var2.floatValue();
   }
}
