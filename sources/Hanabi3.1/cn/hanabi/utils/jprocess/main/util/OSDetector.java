package cn.hanabi.utils.jprocess.main.util;

public class OSDetector {
   private static final String OS = System.getProperty("os.name").toLowerCase();

   private OSDetector() {
   }

   public static boolean isWindows() {
      return OS.contains("win");
   }

   public static boolean isMac() {
      return OS.contains("mac");
   }

   public static boolean isUnix() {
      return OS.contains("nix") || OS.contains("nux") || OS.contains("aix") || OS.matches("mac.*os.*x");
   }

   public static boolean isLinux() {
      return OS.contains("linux");
   }

   public static boolean isSolaris() {
      return OS.contains("sunos");
   }
}
