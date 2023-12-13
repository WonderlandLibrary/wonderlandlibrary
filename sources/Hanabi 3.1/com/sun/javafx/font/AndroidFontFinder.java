package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class AndroidFontFinder {
   private static final String SYSTEM_FONT_NAME = "sans serif";
   private static final float SYSTEM_FONT_SIZE = 16.0F;
   static final String fontDescriptor_2_X_Path = "/com/sun/javafx/font/android_system_fonts.xml";
   static final String fontDescriptor_4_X_Path = "/system/etc/system_fonts.xml";
   static final String systemFontsDir = "/system/fonts";

   public static String getSystemFont() {
      return "sans serif";
   }

   public static float getSystemFontSize() {
      return 16.0F;
   }

   public static String getSystemFontsDir() {
      return "/system/fonts";
   }

   private static boolean parse_2_X_SystemDefaultFonts(HashMap var0, HashMap var1, HashMap var2) {
      InputStream var3 = AndroidFontFinder.class.getResourceAsStream("/com/sun/javafx/font/android_system_fonts.xml");
      if (var3 == null) {
         System.err.println("Resource not found: /com/sun/javafx/font/android_system_fonts.xml");
         return false;
      } else {
         return parseSystemDefaultFonts(var3, var0, var1, var2);
      }
   }

   private static boolean parse_4_X_SystemDefaultFonts(HashMap var0, HashMap var1, HashMap var2) {
      File var3 = new File("/system/etc/system_fonts.xml");

      try {
         return parseSystemDefaultFonts(new FileInputStream(var3), var0, var1, var2);
      } catch (FileNotFoundException var5) {
         System.err.println("File not found: /system/etc/system_fonts.xml");
         return false;
      }
   }

   private static boolean parseSystemDefaultFonts(InputStream var0, final HashMap var1, final HashMap var2, final HashMap var3) {
      try {
         SAXParserFactory var4 = SAXParserFactory.newInstance();
         SAXParser var5 = var4.newSAXParser();
         DefaultHandler var6 = new DefaultHandler() {
            private static final char DASH = '-';
            private static final String FAMILY = "family";
            private static final String FILE = "file";
            private static final String FILESET = "fileset";
            private static final String NAME = "name";
            private static final String NAMESET = "nameset";
            private static final char SPACE = ' ';
            final List filesets = new ArrayList();
            boolean inFamily = false;
            boolean inFile = false;
            boolean inFileset = false;
            boolean inName = false;
            boolean inNameset = false;
            private final List namesets = new ArrayList();
            private final String[] styles = new String[]{"regular", "bold", "italic", "bold italic"};

            public void characters(char[] var1x, int var2x, int var3x) throws SAXException {
               String var4;
               if (this.inName) {
                  var4 = (new String(var1x, var2x, var3x)).toLowerCase();
                  this.namesets.add(var4);
               } else if (this.inFile) {
                  var4 = new String(var1x, var2x, var3x);
                  this.filesets.add(var4);
               }

            }

            public void endElement(String var1x, String var2x, String var3x) throws SAXException {
               if (var3x.equalsIgnoreCase("family")) {
                  Iterator var4 = this.namesets.iterator();

                  while(var4.hasNext()) {
                     String var5 = (String)var4.next();
                     int var6 = 0;
                     String var7 = var5.replace('-', ' ');
                     Iterator var8 = this.filesets.iterator();

                     while(var8.hasNext()) {
                        String var9 = (String)var8.next();
                        String var10 = var7 + " " + this.styles[var6];
                        String var11 = "/system/fonts" + File.separator + var9;
                        File var12 = new File(var11);
                        if (var12.exists() && var12.canRead()) {
                           var1.put(var10, var11);
                           var2.put(var10, var7);
                           ArrayList var13 = (ArrayList)var3.get(var7);
                           if (var13 == null) {
                              var13 = new ArrayList();
                              var3.put(var7, var13);
                           }

                           var13.add(var10);
                           ++var6;
                        }
                     }
                  }

                  this.inFamily = false;
               } else if (var3x.equalsIgnoreCase("nameset")) {
                  this.inNameset = false;
               } else if (var3x.equalsIgnoreCase("fileset")) {
                  this.inFileset = false;
               } else if (var3x.equalsIgnoreCase("name")) {
                  this.inName = false;
               } else if (var3x.equalsIgnoreCase("file")) {
                  this.inFile = false;
               }

            }

            public void startElement(String var1x, String var2x, String var3x, Attributes var4) throws SAXException {
               if (var3x.equalsIgnoreCase("family")) {
                  this.inFamily = true;
                  this.namesets.clear();
                  this.filesets.clear();
               } else if (var3x.equalsIgnoreCase("nameset")) {
                  this.inNameset = true;
               } else if (var3x.equalsIgnoreCase("fileset")) {
                  this.inFileset = true;
               } else if (var3x.equalsIgnoreCase("name")) {
                  this.inName = true;
               } else if (var3x.equalsIgnoreCase("file")) {
                  this.inFile = true;
               }

            }
         };
         var5.parse(var0, var6);
         return true;
      } catch (IOException var7) {
         System.err.println("Failed to load default fonts descriptor: /system/etc/system_fonts.xml");
      } catch (Exception var8) {
         System.err.println("Failed parsing default fonts descriptor;");
         var8.printStackTrace();
      }

      return false;
   }

   public static void populateFontFileNameMap(HashMap var0, HashMap var1, HashMap var2, Locale var3) {
      if (var0 != null && var1 != null && var2 != null) {
         if (var3 == null) {
            var3 = Locale.ENGLISH;
         }

         boolean var4 = parse_4_X_SystemDefaultFonts(var0, var1, var2);
         if (!var4) {
            parse_2_X_SystemDefaultFonts(var0, var1, var2);
         }

      }
   }

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("javafx_font");
         return null;
      });
   }
}
