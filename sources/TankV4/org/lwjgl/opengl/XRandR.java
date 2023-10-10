package org.lwjgl.opengl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lwjgl.LWJGLUtil;

public class XRandR {
   private static XRandR.Screen[] current;
   private static Map screens;
   private static final Pattern SCREEN_PATTERN1 = Pattern.compile("^(\\d+)x(\\d+)\\+(\\d+)\\+(\\d+)$");
   private static final Pattern SCREEN_PATTERN2 = Pattern.compile("^(\\d+)x(\\d+)$");

   private static void populate() {
      if (screens == null) {
         screens = new HashMap();

         try {
            Process var0 = Runtime.getRuntime().exec(new String[]{"xrandr", "-q"});
            ArrayList var1 = new ArrayList();
            ArrayList var2 = new ArrayList();
            String var3 = null;
            BufferedReader var4 = new BufferedReader(new InputStreamReader(var0.getInputStream()));

            String var5;
            while((var5 = var4.readLine()) != null) {
               var5 = var5.trim();
               String[] var6 = var5.split("\\s+");
               if ("connected".equals(var6[1])) {
                  if (var3 != null) {
                     screens.put(var3, var2.toArray(new XRandR.Screen[var2.size()]));
                     var2.clear();
                  }

                  var3 = var6[0];
                  parseScreen(var1, var3, "primary".equals(var6[2]) ? var6[3] : var6[2]);
               } else if (Pattern.matches("\\d*x\\d*", var6[0])) {
                  parseScreen(var2, var3, var6[0]);
               }
            }

            screens.put(var3, var2.toArray(new XRandR.Screen[var2.size()]));
            current = (XRandR.Screen[])var1.toArray(new XRandR.Screen[var1.size()]);
         } catch (Throwable var7) {
            LWJGLUtil.log("Exception in XRandR.populate(): " + var7.getMessage());
            screens.clear();
            current = new XRandR.Screen[0];
         }
      }

   }

   public static XRandR.Screen[] getConfiguration() {
      populate();
      return (XRandR.Screen[])current.clone();
   }

   public static void setConfiguration(XRandR.Screen... var0) {
      if (var0.length == 0) {
         throw new IllegalArgumentException("Must specify at least one screen");
      } else {
         ArrayList var1 = new ArrayList();
         var1.add("xrandr");
         XRandR.Screen[] var2 = current;
         int var3 = var2.length;

         int var4;
         XRandR.Screen var5;
         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var2[var4];
            boolean var6 = false;
            XRandR.Screen[] var7 = var0;
            int var8 = var0.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               XRandR.Screen var10 = var7[var9];
               if (var10.name.equals(var5.name)) {
                  var6 = true;
                  break;
               }
            }

            if (!var6) {
               var1.add("--output");
               var1.add(var5.name);
               var1.add("--off");
            }
         }

         var2 = var0;
         var3 = var0.length;

         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var2[var4];
            XRandR.Screen.access$000(var5, var1);
         }

         try {
            Process var12 = Runtime.getRuntime().exec((String[])var1.toArray(new String[var1.size()]));
            BufferedReader var13 = new BufferedReader(new InputStreamReader(var12.getInputStream()));

            String var14;
            while((var14 = var13.readLine()) != null) {
               LWJGLUtil.log("Unexpected output from xrandr process: " + var14);
            }

            current = var0;
         } catch (IOException var11) {
            LWJGLUtil.log("XRandR exception in setConfiguration(): " + var11.getMessage());
         }

      }
   }

   public static String[] getScreenNames() {
      populate();
      return (String[])screens.keySet().toArray(new String[screens.size()]);
   }

   public static XRandR.Screen[] getResolutions(String var0) {
      populate();
      return (XRandR.Screen[])((XRandR.Screen[])screens.get(var0)).clone();
   }

   private static void parseScreen(List var0, String var1, String var2) {
      Matcher var3 = SCREEN_PATTERN1.matcher(var2);
      if (!var3.matches()) {
         var3 = SCREEN_PATTERN2.matcher(var2);
         if (!var3.matches()) {
            LWJGLUtil.log("Did not match: " + var2);
            return;
         }
      }

      int var4 = Integer.parseInt(var3.group(1));
      int var5 = Integer.parseInt(var3.group(2));
      int var6;
      int var7;
      if (var3.groupCount() > 3) {
         var6 = Integer.parseInt(var3.group(3));
         var7 = Integer.parseInt(var3.group(4));
      } else {
         var6 = 0;
         var7 = 0;
      }

      var0.add(new XRandR.Screen(var1, var4, var5, var6, var7));
   }

   public static class Screen implements Cloneable {
      public final String name;
      public final int width;
      public final int height;
      public int xPos;
      public int yPos;

      private Screen(String var1, int var2, int var3, int var4, int var5) {
         this.name = var1;
         this.width = var2;
         this.height = var3;
         this.xPos = var4;
         this.yPos = var5;
      }

      private void getArgs(List var1) {
         var1.add("--output");
         var1.add(this.name);
         var1.add("--mode");
         var1.add(this.width + "x" + this.height);
         var1.add("--pos");
         var1.add(this.xPos + "x" + this.yPos);
      }

      public String toString() {
         return this.name + " " + this.width + "x" + this.height + " @ " + this.xPos + "x" + this.yPos;
      }

      static void access$000(XRandR.Screen var0, List var1) {
         var0.getArgs(var1);
      }

      Screen(String var1, int var2, int var3, int var4, int var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }
   }
}
