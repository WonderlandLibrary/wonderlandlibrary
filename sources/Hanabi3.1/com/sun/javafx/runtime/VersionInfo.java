package com.sun.javafx.runtime;

public class VersionInfo {
   private static final String BUILD_TIMESTAMP = "Thu Mar 18 23:09:28 PDT 2021";
   private static final String HUDSON_JOB_NAME = "8u-windows-amd64";
   private static final String HUDSON_BUILD_NUMBER = "58";
   private static final String PROMOTED_BUILD_NUMBER = "09";
   private static final String PRODUCT_NAME = "Java(TM)";
   private static final String RAW_VERSION = "8.0.291";
   private static final String RELEASE_MILESTONE = "fcs";
   private static final String RELEASE_NAME = "8u291";
   private static final String VERSION;
   private static final String RUNTIME_VERSION;

   public static synchronized void setupSystemProperties() {
      if (System.getProperty("javafx.version") == null) {
         System.setProperty("javafx.version", getVersion());
         System.setProperty("javafx.runtime.version", getRuntimeVersion());
      }

   }

   public static String getBuildTimestamp() {
      return "Thu Mar 18 23:09:28 PDT 2021";
   }

   public static String getHudsonJobName() {
      return "8u-windows-amd64".equals("not_hudson") ? "" : "8u-windows-amd64";
   }

   public static String getHudsonBuildNumber() {
      return "58";
   }

   public static String getReleaseMilestone() {
      return "fcs".equals("fcs") ? "" : "fcs";
   }

   public static String getVersion() {
      return VERSION;
   }

   public static String getRuntimeVersion() {
      return RUNTIME_VERSION;
   }

   static {
      String var0 = "8.0.291";
      if (getReleaseMilestone().length() > 0) {
         var0 = var0 + "-fcs";
      }

      VERSION = var0;
      if (getHudsonJobName().length() > 0) {
         var0 = var0 + "-b09";
      } else {
         var0 = var0 + " (Thu Mar 18 23:09:28 PDT 2021)";
      }

      RUNTIME_VERSION = var0;
   }
}
