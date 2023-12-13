package net.minecraft.util;

import java.util.regex.Pattern;

public class StringUtils {
   private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

   public static String ticksToElapsedTime(int ticks) {
      int i = ticks / 20;
      int j = i / 60;
      i %= 60;
      return i < 10 ? j + ":0" + i : j + ":" + i;
   }

   public static String stripControlCodes(String p_76338_0_) {
      return patternControlCode.matcher(p_76338_0_).replaceAll("");
   }

   public static String upperSnakeCaseToPascal(String s) {
      if (s == null) {
         return null;
      } else {
         return s.length() == 1 ? Character.toString(s.charAt(0)) : s.charAt(0) + s.substring(1).toLowerCase();
      }
   }

   public static boolean isNullOrEmpty(String string) {
      return org.apache.commons.lang3.StringUtils.isEmpty(string);
   }
}
