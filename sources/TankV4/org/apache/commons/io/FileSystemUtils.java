package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class FileSystemUtils {
   private static final FileSystemUtils INSTANCE = new FileSystemUtils();
   private static final int INIT_PROBLEM = -1;
   private static final int OTHER = 0;
   private static final int WINDOWS = 1;
   private static final int UNIX = 2;
   private static final int POSIX_UNIX = 3;
   private static final int OS;
   private static final String DF;

   /** @deprecated */
   @Deprecated
   public static long freeSpace(String var0) throws IOException {
      return INSTANCE.freeSpaceOS(var0, OS, false, -1L);
   }

   public static long freeSpaceKb(String var0) throws IOException {
      return freeSpaceKb(var0, -1L);
   }

   public static long freeSpaceKb(String var0, long var1) throws IOException {
      return INSTANCE.freeSpaceOS(var0, OS, true, var1);
   }

   public static long freeSpaceKb() throws IOException {
      return freeSpaceKb(-1L);
   }

   public static long freeSpaceKb(long var0) throws IOException {
      return freeSpaceKb((new File(".")).getAbsolutePath(), var0);
   }

   long freeSpaceOS(String var1, int var2, boolean var3, long var4) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("Path must not be empty");
      } else {
         switch(var2) {
         case 0:
            throw new IllegalStateException("Unsupported operating system");
         case 1:
            return var3 ? this.freeSpaceWindows(var1, var4) / 1024L : this.freeSpaceWindows(var1, var4);
         case 2:
            return this.freeSpaceUnix(var1, var3, false, var4);
         case 3:
            return this.freeSpaceUnix(var1, var3, true, var4);
         default:
            throw new IllegalStateException("Exception caught when determining operating system");
         }
      }
   }

   long freeSpaceWindows(String var1, long var2) throws IOException {
      var1 = FilenameUtils.normalize(var1, false);
      if (var1.length() > 0 && var1.charAt(0) != '"') {
         var1 = "\"" + var1 + "\"";
      }

      String[] var4 = new String[]{"cmd.exe", "/C", "dir /a /-c " + var1};
      List var5 = this.performCommand(var4, Integer.MAX_VALUE, var2);

      for(int var6 = var5.size() - 1; var6 >= 0; --var6) {
         String var7 = (String)var5.get(var6);
         if (var7.length() > 0) {
            return this.parseDir(var7, var1);
         }
      }

      throw new IOException("Command line 'dir /-c' did not return any info for path '" + var1 + "'");
   }

   long parseDir(String var1, String var2) throws IOException {
      int var3 = 0;
      int var4 = 0;

      int var5;
      char var6;
      for(var5 = var1.length() - 1; var5 >= 0; --var5) {
         var6 = var1.charAt(var5);
         if (Character.isDigit(var6)) {
            var4 = var5 + 1;
            break;
         }
      }

      while(var5 >= 0) {
         var6 = var1.charAt(var5);
         if (!Character.isDigit(var6) && var6 != ',' && var6 != '.') {
            var3 = var5 + 1;
            break;
         }

         --var5;
      }

      if (var5 < 0) {
         throw new IOException("Command line 'dir /-c' did not return valid info for path '" + var2 + "'");
      } else {
         StringBuilder var8 = new StringBuilder(var1.substring(var3, var4));

         for(int var7 = 0; var7 < var8.length(); ++var7) {
            if (var8.charAt(var7) == ',' || var8.charAt(var7) == '.') {
               var8.deleteCharAt(var7--);
            }
         }

         return this.parseBytes(var8.toString(), var2);
      }
   }

   long freeSpaceUnix(String var1, boolean var2, boolean var3, long var4) throws IOException {
      if (var1.length() == 0) {
         throw new IllegalArgumentException("Path must not be empty");
      } else {
         String var6 = "-";
         if (var2) {
            var6 = var6 + "k";
         }

         if (var3) {
            var6 = var6 + "P";
         }

         String[] var7 = var6.length() > 1 ? new String[]{DF, var6, var1} : new String[]{DF, var1};
         List var8 = this.performCommand(var7, 3, var4);
         if (var8.size() < 2) {
            throw new IOException("Command line '" + DF + "' did not return info as expected " + "for path '" + var1 + "'- response was " + var8);
         } else {
            String var9 = (String)var8.get(1);
            StringTokenizer var10 = new StringTokenizer(var9, " ");
            String var11;
            if (var10.countTokens() < 4) {
               if (var10.countTokens() != 1 || var8.size() < 3) {
                  throw new IOException("Command line '" + DF + "' did not return data as expected " + "for path '" + var1 + "'- check path is valid");
               }

               var11 = (String)var8.get(2);
               var10 = new StringTokenizer(var11, " ");
            } else {
               var10.nextToken();
            }

            var10.nextToken();
            var10.nextToken();
            var11 = var10.nextToken();
            return this.parseBytes(var11, var1);
         }
      }
   }

   long parseBytes(String var1, String var2) throws IOException {
      try {
         long var3 = Long.parseLong(var1);
         if (var3 < 0L) {
            throw new IOException("Command line '" + DF + "' did not find free space in response " + "for path '" + var2 + "'- check path is valid");
         } else {
            return var3;
         }
      } catch (NumberFormatException var5) {
         throw new IOExceptionWithCause("Command line '" + DF + "' did not return numeric data as expected " + "for path '" + var2 + "'- check path is valid", var5);
      }
   }

   List performCommand(String[] var1, int var2, long var3) throws IOException {
      ArrayList var5 = new ArrayList(20);
      Process var6 = null;
      InputStream var7 = null;
      OutputStream var8 = null;
      InputStream var9 = null;
      BufferedReader var10 = null;

      ArrayList var13;
      try {
         Thread var11 = ThreadMonitor.start(var3);
         var6 = this.openProcess(var1);
         var7 = var6.getInputStream();
         var8 = var6.getOutputStream();
         var9 = var6.getErrorStream();
         var10 = new BufferedReader(new InputStreamReader(var7));

         for(String var12 = var10.readLine(); var12 != null && var5.size() < var2; var12 = var10.readLine()) {
            var12 = var12.toLowerCase(Locale.ENGLISH).trim();
            var5.add(var12);
         }

         var6.waitFor();
         ThreadMonitor.stop(var11);
         if (var6.exitValue() != 0) {
            throw new IOException("Command line returned OS error code '" + var6.exitValue() + "' for command " + Arrays.asList(var1));
         }

         if (var5.isEmpty()) {
            throw new IOException("Command line did not return any info for command " + Arrays.asList(var1));
         }

         var13 = var5;
      } catch (InterruptedException var15) {
         throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(var1) + " timeout=" + var3, var15);
      }

      IOUtils.closeQuietly(var7);
      IOUtils.closeQuietly(var8);
      IOUtils.closeQuietly(var9);
      IOUtils.closeQuietly((Reader)var10);
      if (var6 != null) {
         var6.destroy();
      }

      return var13;
   }

   Process openProcess(String[] var1) throws IOException {
      return Runtime.getRuntime().exec(var1);
   }

   static {
      boolean var0 = false;
      String var1 = "df";

      byte var4;
      try {
         String var2 = System.getProperty("os.name");
         if (var2 == null) {
            throw new IOException("os.name not found");
         }

         var2 = var2.toLowerCase(Locale.ENGLISH);
         if (var2.indexOf("windows") != -1) {
            var4 = 1;
         } else if (var2.indexOf("linux") == -1 && var2.indexOf("mpe/ix") == -1 && var2.indexOf("freebsd") == -1 && var2.indexOf("irix") == -1 && var2.indexOf("digital unix") == -1 && var2.indexOf("unix") == -1 && var2.indexOf("mac os x") == -1) {
            if (var2.indexOf("sun os") == -1 && var2.indexOf("sunos") == -1 && var2.indexOf("solaris") == -1) {
               if (var2.indexOf("hp-ux") == -1 && var2.indexOf("aix") == -1) {
                  var4 = 0;
               } else {
                  var4 = 3;
               }
            } else {
               var4 = 3;
               var1 = "/usr/xpg4/bin/df";
            }
         } else {
            var4 = 2;
         }
      } catch (Exception var3) {
         var4 = -1;
      }

      OS = var4;
      DF = var1;
   }
}
