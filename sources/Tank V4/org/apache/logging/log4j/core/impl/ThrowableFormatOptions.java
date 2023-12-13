package org.apache.logging.log4j.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.core.helpers.Constants;

public final class ThrowableFormatOptions {
   private static final int DEFAULT_LINES = Integer.MAX_VALUE;
   protected static final ThrowableFormatOptions DEFAULT = new ThrowableFormatOptions();
   private static final String FULL = "full";
   private static final String NONE = "none";
   private static final String SHORT = "short";
   private final int lines;
   private final String separator;
   private final List packages;
   public static final String CLASS_NAME = "short.className";
   public static final String METHOD_NAME = "short.methodName";
   public static final String LINE_NUMBER = "short.lineNumber";
   public static final String FILE_NAME = "short.fileName";
   public static final String MESSAGE = "short.message";
   public static final String LOCALIZED_MESSAGE = "short.localizedMessage";

   protected ThrowableFormatOptions(int var1, String var2, List var3) {
      this.lines = var1;
      this.separator = var2 == null ? Constants.LINE_SEP : var2;
      this.packages = var3;
   }

   protected ThrowableFormatOptions(List var1) {
      this(Integer.MAX_VALUE, (String)null, var1);
   }

   protected ThrowableFormatOptions() {
      this(Integer.MAX_VALUE, (String)null, (List)null);
   }

   public int getLines() {
      return this.lines;
   }

   public String getSeparator() {
      return this.separator;
   }

   public List getPackages() {
      return this.packages;
   }

   public int minLines(int var1) {
      return this.lines > var1 ? var1 : this.lines;
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }

   public static ThrowableFormatOptions newInstance(String[] var0) {
      if (var0 != null && var0.length != 0) {
         String var2;
         if (var0.length == 1 && var0[0] != null && var0[0].length() > 0) {
            String[] var1 = var0[0].split(",", 2);
            var2 = var1[0].trim();
            Scanner var3 = new Scanner(var2);
            if (var1.length > 1 && (var2.equalsIgnoreCase("full") || var2.equalsIgnoreCase("short") || var2.equalsIgnoreCase("none") || var3.hasNextInt())) {
               var0 = new String[]{var2, var1[1].trim()};
            }

            var3.close();
         }

         int var15 = DEFAULT.lines;
         var2 = DEFAULT.separator;
         Object var16 = DEFAULT.packages;
         String[] var4 = var0;
         int var5 = var0.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            if (var7 != null) {
               String var8 = var7.trim();
               if (!var8.isEmpty()) {
                  if (var8.startsWith("separator(") && var8.endsWith(")")) {
                     var2 = var8.substring(10, var8.length() - 1);
                  } else if (var8.startsWith("filters(") && var8.endsWith(")")) {
                     String var9 = var8.substring(8, var8.length() - 1);
                     if (var9.length() > 0) {
                        String[] var10 = var9.split(",");
                        if (var10.length > 0) {
                           var16 = new ArrayList(var10.length);
                           String[] var11 = var10;
                           int var12 = var10.length;

                           for(int var13 = 0; var13 < var12; ++var13) {
                              String var14 = var11[var13];
                              var14 = var14.trim();
                              if (var14.length() > 0) {
                                 ((List)var16).add(var14);
                              }
                           }
                        }
                     }
                  } else if (var8.equalsIgnoreCase("none")) {
                     var15 = 0;
                  } else if (!var8.equalsIgnoreCase("short") && !var8.equalsIgnoreCase("short.className") && !var8.equalsIgnoreCase("short.methodName") && !var8.equalsIgnoreCase("short.lineNumber") && !var8.equalsIgnoreCase("short.fileName") && !var8.equalsIgnoreCase("short.message") && !var8.equalsIgnoreCase("short.localizedMessage")) {
                     if (!var8.equalsIgnoreCase("full")) {
                        var15 = Integer.parseInt(var8);
                     }
                  } else {
                     var15 = 2;
                  }
               }
            }
         }

         return new ThrowableFormatOptions(var15, var2, (List)var16);
      } else {
         return DEFAULT;
      }
   }
}
