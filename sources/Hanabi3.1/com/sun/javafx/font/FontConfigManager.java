package com.sun.javafx.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

class FontConfigManager {
   static boolean debugFonts = false;
   static boolean useFontConfig = true;
   static boolean fontConfigFailed = false;
   static boolean useEmbeddedFontSupport = false;
   private static final String[] fontConfigNames;
   private static FcCompFont[] fontConfigFonts;
   private static String defaultFontFile;

   private FontConfigManager() {
   }

   private static String[] getFontConfigNames() {
      return fontConfigNames;
   }

   private static String getFCLocaleStr() {
      Locale var0 = Locale.getDefault();
      String var1 = var0.getLanguage();
      String var2 = var0.getCountry();
      if (!var2.equals("")) {
         var1 = var1 + "-" + var2;
      }

      return var1;
   }

   private static native boolean getFontConfig(String var0, FcCompFont[] var1, boolean var2);

   private static synchronized void initFontConfigLogFonts() {
      if (fontConfigFonts == null && !fontConfigFailed) {
         long var0 = 0L;
         if (debugFonts) {
            var0 = System.nanoTime();
         }

         String[] var2 = getFontConfigNames();
         FcCompFont[] var3 = new FcCompFont[var2.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4] = new FcCompFont();
            var3[var4].fcName = var2[var4];
            int var5 = var3[var4].fcName.indexOf(58);
            var3[var4].fcFamily = var3[var4].fcName.substring(0, var5);
            var3[var4].style = var4 % 4;
         }

         boolean var12 = false;
         if (useFontConfig) {
            var12 = getFontConfig(getFCLocaleStr(), var3, true);
         } else if (debugFonts) {
            System.err.println("Not using FontConfig");
         }

         if (useEmbeddedFontSupport || !var12) {
            FontConfigManager.EmbeddedFontSupport.initLogicalFonts(var3);
         }

         FontConfigFont var13 = null;

         int var6;
         for(var6 = 0; var6 < var3.length; ++var6) {
            FcCompFont var7 = var3[var6];
            if (var7.firstFont == null) {
               if (debugFonts) {
                  System.err.println("Fontconfig returned no font for " + var3[var6].fcName);
               }

               fontConfigFailed = true;
            } else if (var13 == null) {
               var13 = var7.firstFont;
               defaultFontFile = var13.fontFile;
            }
         }

         if (var13 == null) {
            fontConfigFailed = true;
            System.err.println("Error: JavaFX detected no fonts! Please refer to release notes for proper font configuration");
         } else {
            if (fontConfigFailed) {
               for(var6 = 0; var6 < var3.length; ++var6) {
                  if (var3[var6].firstFont == null) {
                     var3[var6].firstFont = var13;
                  }
               }
            }

            fontConfigFonts = var3;
            if (debugFonts) {
               long var14 = System.nanoTime();
               System.err.println("Time spent accessing fontconfig=" + (var14 - var0) / 1000000L + "ms.");

               for(int var8 = 0; var8 < fontConfigFonts.length; ++var8) {
                  FcCompFont var9 = fontConfigFonts[var8];
                  System.err.println("FC font " + var9.fcName + " maps to " + var9.firstFont.fullName + " in file " + var9.firstFont.fontFile);
                  if (var9.allFonts != null) {
                     for(int var10 = 0; var10 < var9.allFonts.length; ++var10) {
                        FontConfigFont var11 = var9.allFonts[var10];
                        System.err.println(" " + var10 + ") Family=" + var11.familyName + ", Style=" + var11.styleStr + ", Fullname=" + var11.fullName + ", File=" + var11.fontFile);
                     }
                  }
               }
            }

         }
      }
   }

   private static native boolean populateMapsNative(HashMap var0, HashMap var1, HashMap var2, Locale var3);

   public static void populateMaps(HashMap var0, HashMap var1, HashMap var2, Locale var3) {
      boolean var4 = false;
      if (useFontConfig && !fontConfigFailed) {
         var4 = populateMapsNative(var0, var1, var2, var3);
      }

      if (fontConfigFailed || useEmbeddedFontSupport || !var4) {
         FontConfigManager.EmbeddedFontSupport.populateMaps(var0, var1, var2, var3);
      }

   }

   private static String mapFxToFcLogicalFamilyName(String var0) {
      if (var0.equals("serif")) {
         return "serif";
      } else {
         return var0.equals("monospaced") ? "monospace" : "sans";
      }
   }

   public static FcCompFont getFontConfigFont(String var0, boolean var1, boolean var2) {
      initFontConfigLogFonts();
      if (fontConfigFonts == null) {
         return null;
      } else {
         String var3 = mapFxToFcLogicalFamilyName(var0.toLowerCase());
         int var4 = var1 ? 1 : 0;
         if (var2) {
            var4 += 2;
         }

         FcCompFont var5 = null;

         for(int var6 = 0; var6 < fontConfigFonts.length; ++var6) {
            if (var3.equals(fontConfigFonts[var6].fcFamily) && var4 == fontConfigFonts[var6].style) {
               var5 = fontConfigFonts[var6];
               break;
            }
         }

         if (var5 == null) {
            var5 = fontConfigFonts[0];
         }

         if (debugFonts) {
            System.err.println("FC name=" + var3 + " style=" + var4 + " uses " + var5.firstFont.fullName + " in file: " + var5.firstFont.fontFile);
         }

         return var5;
      }
   }

   public static String getDefaultFontPath() {
      if (fontConfigFonts == null && !fontConfigFailed) {
         getFontConfigFont("System", false, false);
      }

      return defaultFontFile;
   }

   public static ArrayList getFileNames(FcCompFont var0, boolean var1) {
      ArrayList var2 = new ArrayList();
      if (var0.allFonts != null) {
         int var3 = var1 ? 1 : 0;

         for(int var4 = var3; var4 < var0.allFonts.length; ++var4) {
            var2.add(var0.allFonts[var4].fontFile);
         }
      }

      return var2;
   }

   public static ArrayList getFontNames(FcCompFont var0, boolean var1) {
      ArrayList var2 = new ArrayList();
      if (var0.allFonts != null) {
         int var3 = var1 ? 1 : 0;

         for(int var4 = var3; var4 < var0.allFonts.length; ++var4) {
            var2.add(var0.allFonts[var4].fullName);
         }
      }

      return var2;
   }

   static {
      AccessController.doPrivileged(() -> {
         String var0 = System.getProperty("prism.debugfonts", "");
         debugFonts = "true".equals(var0);
         String var1 = System.getProperty("prism.useFontConfig", "true");
         useFontConfig = "true".equals(var1);
         String var2 = System.getProperty("prism.embeddedfonts", "");
         useEmbeddedFontSupport = "true".equals(var2);
         return null;
      });
      fontConfigNames = new String[]{"sans:regular:roman", "sans:bold:roman", "sans:regular:italic", "sans:bold:italic", "serif:regular:roman", "serif:bold:roman", "serif:regular:italic", "serif:bold:italic", "monospace:regular:roman", "monospace:bold:roman", "monospace:regular:italic", "monospace:bold:italic"};
   }

   private static class EmbeddedFontSupport {
      private static String fontDirProp = null;
      private static String fontDir;
      private static boolean fontDirFromJRE = false;
      static String[] jreFontsProperties;

      private static void initEmbeddedFonts() {
         fontDirProp = System.getProperty("prism.fontdir");
         if (fontDirProp != null) {
            fontDir = fontDirProp;
         } else {
            try {
               String var0 = System.getProperty("java.home");
               if (var0 == null) {
                  return;
               }

               File var1 = new File(var0, "lib/fonts");
               if (var1.exists()) {
                  fontDirFromJRE = true;
                  fontDir = var1.getPath();
               }

               if (FontConfigManager.debugFonts) {
                  System.err.println("Fallback fontDir is " + var1 + " exists = " + var1.exists());
               }
            } catch (Exception var2) {
               if (FontConfigManager.debugFonts) {
                  var2.printStackTrace();
               }

               fontDir = "/";
            }
         }

      }

      private static String getStyleStr(int var0) {
         switch (var0) {
            case 0:
               return "regular";
            case 1:
               return "bold";
            case 2:
               return "italic";
            case 3:
               return "bolditalic";
            default:
               return "regular";
         }
      }

      private static boolean exists(File var0) {
         return (Boolean)AccessController.doPrivileged(() -> {
            return var0.exists();
         });
      }

      static void initLogicalFonts(FcCompFont[] var0) {
         Properties var1 = new Properties();

         try {
            File var2 = new File(fontDir, "logicalfonts.properties");
            if (var2.exists()) {
               FileInputStream var14 = new FileInputStream(var2);
               var1.load(var14);
               var14.close();
            } else if (fontDirFromJRE) {
               for(int var3 = 0; var3 < jreFontsProperties.length; var3 += 2) {
                  var1.setProperty(jreFontsProperties[var3], jreFontsProperties[var3 + 1]);
               }

               if (FontConfigManager.debugFonts) {
                  System.err.println("Using fallback implied logicalfonts.properties");
               }
            }
         } catch (IOException var12) {
            if (FontConfigManager.debugFonts) {
               System.err.println(var12);
               return;
            }
         }

         for(int var13 = 0; var13 < var0.length; ++var13) {
            String var15 = var0[var13].fcFamily;
            String var4 = getStyleStr(var0[var13].style);
            String var5 = var15 + "." + var4 + ".";
            ArrayList var6 = new ArrayList();
            int var7 = 0;

            while(true) {
               String var8 = var1.getProperty(var5 + var7 + ".file");
               String var9 = var1.getProperty(var5 + var7 + ".font");
               ++var7;
               if (var8 == null) {
                  if (var6.size() > 0) {
                     var0[var13].allFonts = new FontConfigFont[var6.size()];
                     var6.toArray(var0[var13].allFonts);
                  }
                  break;
               }

               File var10 = new File(fontDir, var8);
               if (!exists(var10)) {
                  if (FontConfigManager.debugFonts) {
                     System.out.println("Failed to find logical font file " + var10);
                  }
               } else {
                  FontConfigFont var11 = new FontConfigFont();
                  var11.fontFile = var10.getPath();
                  var11.fullName = var9;
                  var11.familyName = null;
                  var11.styleStr = null;
                  if (var0[var13].firstFont == null) {
                     var0[var13].firstFont = var11;
                  }

                  var6.add(var11);
               }
            }
         }

      }

      static void populateMaps(HashMap var0, HashMap var1, HashMap var2, Locale var3) {
         Properties var4 = new Properties();
         AccessController.doPrivileged(() -> {
            try {
               String var1 = fontDir + "/allfonts.properties";
               FileInputStream var2 = new FileInputStream(var1);
               var4.load(var2);
               var2.close();
            } catch (IOException var3) {
               var4.clear();
               if (FontConfigManager.debugFonts) {
                  System.err.println(var3);
                  System.err.println("Fall back to opening the files");
               }
            }

            return null;
         });
         if (!var4.isEmpty()) {
            int var5 = Integer.MAX_VALUE;

            try {
               var5 = Integer.parseInt(var4.getProperty("maxFont", ""));
            } catch (NumberFormatException var14) {
            }

            if (var5 <= 0) {
               var5 = Integer.MAX_VALUE;
            }

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = var4.getProperty("family." + var6);
               String var8 = var4.getProperty("font." + var6);
               String var9 = var4.getProperty("file." + var6);
               if (var9 == null) {
                  break;
               }

               File var10 = new File(fontDir, var9);
               if (exists(var10) && var7 != null && var8 != null) {
                  String var11 = var8.toLowerCase(Locale.ENGLISH);
                  String var12 = var7.toLowerCase(Locale.ENGLISH);
                  var0.put(var11, var10.getPath());
                  var1.put(var11, var7);
                  ArrayList var13 = (ArrayList)var2.get(var12);
                  if (var13 == null) {
                     var13 = new ArrayList(4);
                     var2.put(var12, var13);
                  }

                  var13.add(var8);
               }
            }
         }

      }

      static {
         AccessController.doPrivileged(() -> {
            initEmbeddedFonts();
            return null;
         });
         jreFontsProperties = new String[]{"sans.regular.0.font", "Lucida Sans Regular", "sans.regular.0.file", "LucidaSansRegular.ttf", "sans.bold.0.font", "Lucida Sans Bold", "sans.bold.0.file", "LucidaSansDemiBold.ttf", "monospace.regular.0.font", "Lucida Typewriter Regular", "monospace.regular.0.file", "LucidaTypewriterRegular.ttf", "monospace.bold.0.font", "Lucida Typewriter Bold", "monospace.bold.0.file", "LucidaTypewriterBold.ttf", "serif.regular.0.font", "Lucida Bright", "serif.regular.0.file", "LucidaBrightRegular.ttf", "serif.bold.0.font", "Lucida Bright Demibold", "serif.bold.0.file", "LucidaBrightDemiBold.ttf", "serif.italic.0.font", "Lucida Bright Italic", "serif.italic.0.file", "LucidaBrightItalic.ttf", "serif.bolditalic.0.font", "Lucida Bright Demibold Italic", "serif.bolditalic.0.file", "LucidaBrightDemiItalic.ttf"};
      }
   }

   public static class FcCompFont {
      public String fcName;
      public String fcFamily;
      public int style;
      public FontConfigFont firstFont;
      public FontConfigFont[] allFonts;
   }

   public static class FontConfigFont {
      public String familyName;
      public String styleStr;
      public String fullName;
      public String fontFile;
   }
}
