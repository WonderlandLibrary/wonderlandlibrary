package cn.hanabi.utils;

public class OSUtils {
   protected static OSType detectedOS;

   public static OSType getOperatingSystemType() {
      if (detectedOS == null) {
         String OS = System.getProperty("os.name", "generic");
         if (!OS.contains("mac") && !OS.contains("darwin")) {
            if (OS.contains("win")) {
               detectedOS = OSUtils.OSType.Windows;
            } else if (OS.contains("nux")) {
               detectedOS = OSUtils.OSType.Linux;
            } else {
               detectedOS = OSUtils.OSType.Other;
            }
         } else {
            detectedOS = OSUtils.OSType.MacOS;
         }
      }

      return detectedOS;
   }

   public static enum OSType {
      Windows,
      MacOS,
      Linux,
      Other;
   }
}
