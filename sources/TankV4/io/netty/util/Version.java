package io.netty.util;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public final class Version {
   private static final String PROP_VERSION = ".version";
   private static final String PROP_BUILD_DATE = ".buildDate";
   private static final String PROP_COMMIT_DATE = ".commitDate";
   private static final String PROP_SHORT_COMMIT_HASH = ".shortCommitHash";
   private static final String PROP_LONG_COMMIT_HASH = ".longCommitHash";
   private static final String PROP_REPO_STATUS = ".repoStatus";
   private final String artifactId;
   private final String artifactVersion;
   private final long buildTimeMillis;
   private final long commitTimeMillis;
   private final String shortCommitHash;
   private final String longCommitHash;
   private final String repositoryStatus;

   public static Map identify() {
      return identify((ClassLoader)null);
   }

   public static Map identify(ClassLoader var0) {
      if (var0 == null) {
         var0 = Thread.currentThread().getContextClassLoader();
      }

      Properties var1 = new Properties();

      try {
         Enumeration var2 = var0.getResources("META-INF/io.netty.versions.properties");

         while(var2.hasMoreElements()) {
            URL var3 = (URL)var2.nextElement();
            InputStream var4 = var3.openStream();
            var1.load(var4);

            try {
               var4.close();
            } catch (Exception var8) {
            }
         }
      } catch (Exception var9) {
      }

      HashSet var10 = new HashSet();
      Iterator var11 = var1.keySet().iterator();

      String var5;
      while(var11.hasNext()) {
         Object var13 = var11.next();
         var5 = (String)var13;
         int var6 = var5.indexOf(46);
         if (var6 > 0) {
            String var7 = var5.substring(0, var6);
            if (var1.containsKey(var7 + ".version") && var1.containsKey(var7 + ".buildDate") && var1.containsKey(var7 + ".commitDate") && var1.containsKey(var7 + ".shortCommitHash") && var1.containsKey(var7 + ".longCommitHash") && var1.containsKey(var7 + ".repoStatus")) {
               var10.add(var7);
            }
         }
      }

      TreeMap var12 = new TreeMap();
      Iterator var14 = var10.iterator();

      while(var14.hasNext()) {
         var5 = (String)var14.next();
         var12.put(var5, new Version(var5, var1.getProperty(var5 + ".version"), parseIso8601(var1.getProperty(var5 + ".buildDate")), parseIso8601(var1.getProperty(var5 + ".commitDate")), var1.getProperty(var5 + ".shortCommitHash"), var1.getProperty(var5 + ".longCommitHash"), var1.getProperty(var5 + ".repoStatus")));
      }

      return var12;
   }

   private static long parseIso8601(String var0) {
      try {
         return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")).parse(var0).getTime();
      } catch (ParseException var2) {
         return 0L;
      }
   }

   public static void main(String[] var0) {
      Iterator var1 = identify().values().iterator();

      while(var1.hasNext()) {
         Version var2 = (Version)var1.next();
         System.err.println(var2);
      }

   }

   private Version(String var1, String var2, long var3, long var5, String var7, String var8, String var9) {
      this.artifactId = var1;
      this.artifactVersion = var2;
      this.buildTimeMillis = var3;
      this.commitTimeMillis = var5;
      this.shortCommitHash = var7;
      this.longCommitHash = var8;
      this.repositoryStatus = var9;
   }

   public String artifactId() {
      return this.artifactId;
   }

   public String artifactVersion() {
      return this.artifactVersion;
   }

   public long buildTimeMillis() {
      return this.buildTimeMillis;
   }

   public long commitTimeMillis() {
      return this.commitTimeMillis;
   }

   public String shortCommitHash() {
      return this.shortCommitHash;
   }

   public String longCommitHash() {
      return this.longCommitHash;
   }

   public String repositoryStatus() {
      return this.repositoryStatus;
   }

   public String toString() {
      return this.artifactId + '-' + this.artifactVersion + '.' + this.shortCommitHash + ("clean".equals(this.repositoryStatus) ? "" : " (repository: " + this.repositoryStatus + ')');
   }
}
