package org.apache.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class VersionInfo {
   public static final String UNAVAILABLE = "UNAVAILABLE";
   public static final String VERSION_PROPERTY_FILE = "version.properties";
   public static final String PROPERTY_MODULE = "info.module";
   public static final String PROPERTY_RELEASE = "info.release";
   public static final String PROPERTY_TIMESTAMP = "info.timestamp";
   private final String infoPackage;
   private final String infoModule;
   private final String infoRelease;
   private final String infoTimestamp;
   private final String infoClassloader;

   protected VersionInfo(String var1, String var2, String var3, String var4, String var5) {
      Args.notNull(var1, "Package identifier");
      this.infoPackage = var1;
      this.infoModule = var2 != null ? var2 : "UNAVAILABLE";
      this.infoRelease = var3 != null ? var3 : "UNAVAILABLE";
      this.infoTimestamp = var4 != null ? var4 : "UNAVAILABLE";
      this.infoClassloader = var5 != null ? var5 : "UNAVAILABLE";
   }

   public final String getPackage() {
      return this.infoPackage;
   }

   public final String getModule() {
      return this.infoModule;
   }

   public final String getRelease() {
      return this.infoRelease;
   }

   public final String getTimestamp() {
      return this.infoTimestamp;
   }

   public final String getClassloader() {
      return this.infoClassloader;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(20 + this.infoPackage.length() + this.infoModule.length() + this.infoRelease.length() + this.infoTimestamp.length() + this.infoClassloader.length());
      var1.append("VersionInfo(").append(this.infoPackage).append(':').append(this.infoModule);
      if (!"UNAVAILABLE".equals(this.infoRelease)) {
         var1.append(':').append(this.infoRelease);
      }

      if (!"UNAVAILABLE".equals(this.infoTimestamp)) {
         var1.append(':').append(this.infoTimestamp);
      }

      var1.append(')');
      if (!"UNAVAILABLE".equals(this.infoClassloader)) {
         var1.append('@').append(this.infoClassloader);
      }

      return var1.toString();
   }

   public static VersionInfo[] loadVersionInfo(String[] var0, ClassLoader var1) {
      Args.notNull(var0, "Package identifier array");
      ArrayList var2 = new ArrayList(var0.length);
      String[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         VersionInfo var7 = loadVersionInfo(var6, var1);
         if (var7 != null) {
            var2.add(var7);
         }
      }

      return (VersionInfo[])var2.toArray(new VersionInfo[var2.size()]);
   }

   public static VersionInfo loadVersionInfo(String var0, ClassLoader var1) {
      Args.notNull(var0, "Package identifier");
      ClassLoader var2 = var1 != null ? var1 : Thread.currentThread().getContextClassLoader();
      Properties var3 = null;

      try {
         InputStream var4 = var2.getResourceAsStream(var0.replace('.', '/') + "/" + "version.properties");
         if (var4 != null) {
            Properties var5 = new Properties();
            var5.load(var4);
            var3 = var5;
            var4.close();
         }
      } catch (IOException var7) {
      }

      VersionInfo var8 = null;
      if (var3 != null) {
         var8 = fromMap(var0, var3, var2);
      }

      return var8;
   }

   protected static VersionInfo fromMap(String var0, Map var1, ClassLoader var2) {
      Args.notNull(var0, "Package identifier");
      String var3 = null;
      String var4 = null;
      String var5 = null;
      if (var1 != null) {
         var3 = (String)var1.get("info.module");
         if (var3 != null && var3.length() < 1) {
            var3 = null;
         }

         var4 = (String)var1.get("info.release");
         if (var4 != null && (var4.length() < 1 || var4.equals("${pom.version}"))) {
            var4 = null;
         }

         var5 = (String)var1.get("info.timestamp");
         if (var5 != null && (var5.length() < 1 || var5.equals("${mvn.timestamp}"))) {
            var5 = null;
         }
      }

      String var6 = null;
      if (var2 != null) {
         var6 = var2.toString();
      }

      return new VersionInfo(var0, var3, var4, var5, var6);
   }

   public static String getUserAgent(String var0, String var1, Class var2) {
      VersionInfo var3 = loadVersionInfo(var1, var2.getClassLoader());
      String var4 = var3 != null ? var3.getRelease() : "UNAVAILABLE";
      String var5 = System.getProperty("java.version");
      return var0 + "/" + var4 + " (Java 1.5 minimum; Java/" + var5 + ")";
   }
}
