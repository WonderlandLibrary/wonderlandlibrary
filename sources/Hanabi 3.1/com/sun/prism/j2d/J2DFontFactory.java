package com.sun.prism.j2d;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.PGFont;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

final class J2DFontFactory implements FontFactory {
   FontFactory prismFontFactory;
   private static boolean compositeFontMethodsInitialized = false;
   private static Method getCompositeFontUIResource = null;

   J2DFontFactory(FontFactory var1) {
      this.prismFontFactory = var1;
   }

   public PGFont createFont(String var1, float var2) {
      return this.prismFontFactory.createFont(var1, var2);
   }

   public PGFont createFont(String var1, boolean var2, boolean var3, float var4) {
      return this.prismFontFactory.createFont(var1, var2, var3, var4);
   }

   public synchronized PGFont deriveFont(PGFont var1, boolean var2, boolean var3, float var4) {
      return this.prismFontFactory.deriveFont(var1, var2, var3, var4);
   }

   public String[] getFontFamilyNames() {
      return this.prismFontFactory.getFontFamilyNames();
   }

   public String[] getFontFullNames() {
      return this.prismFontFactory.getFontFullNames();
   }

   public String[] getFontFullNames(String var1) {
      return this.prismFontFactory.getFontFullNames(var1);
   }

   public boolean isPlatformFont(String var1) {
      return this.prismFontFactory.isPlatformFont(var1);
   }

   public final boolean hasPermission() {
      return this.prismFontFactory.hasPermission();
   }

   public PGFont loadEmbeddedFont(String var1, InputStream var2, float var3, boolean var4) {
      if (!this.hasPermission()) {
         return this.createFont("System Regular", var3);
      } else {
         PGFont var5 = this.prismFontFactory.loadEmbeddedFont(var1, var2, var3, var4);
         if (var5 == null) {
            return null;
         } else {
            FontResource var6 = var5.getFontResource();
            registerFont(var5.getFontResource());
            return var5;
         }
      }
   }

   public static void registerFont(FontResource var0) {
      AccessController.doPrivileged(() -> {
         FileInputStream var1 = null;

         try {
            File var2 = new File(var0.getFileName());
            var1 = new FileInputStream(var2);
            Font var3 = Font.createFont(0, var1);
            var0.setPeer(var3);
         } catch (Exception var12) {
            var12.printStackTrace();
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (Exception var11) {
               }
            }

         }

         return null;
      });
   }

   public PGFont loadEmbeddedFont(String var1, String var2, float var3, boolean var4) {
      if (!this.hasPermission()) {
         return this.createFont("System Regular", var3);
      } else {
         PGFont var5 = this.prismFontFactory.loadEmbeddedFont(var1, var2, var3, var4);
         if (var5 == null) {
            return null;
         } else {
            final FontResource var6 = var5.getFontResource();
            AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  try {
                     File var1 = new File(var6.getFileName());
                     Font var2 = Font.createFont(0, var1);
                     var6.setPeer(var2);
                  } catch (Exception var3) {
                     var3.printStackTrace();
                  }

                  return null;
               }
            });
            return var5;
         }
      }
   }

   static Font getCompositeFont(Font var0) {
      if (PlatformUtil.isMac()) {
         return var0;
      } else {
         Class var1 = J2DFontFactory.class;
         synchronized(J2DFontFactory.class) {
            if (!compositeFontMethodsInitialized) {
               AccessController.doPrivileged(() -> {
                  compositeFontMethodsInitialized = true;

                  Class var0;
                  try {
                     var0 = Class.forName("sun.font.FontUtilities", true, (ClassLoader)null);
                  } catch (ClassNotFoundException var5) {
                     try {
                        var0 = Class.forName("sun.font.FontManager", true, (ClassLoader)null);
                     } catch (ClassNotFoundException var4) {
                        return null;
                     }
                  }

                  try {
                     getCompositeFontUIResource = var0.getMethod("getCompositeFontUIResource", Font.class);
                  } catch (NoSuchMethodException var3) {
                  }

                  return null;
               });
            }
         }

         if (getCompositeFontUIResource != null) {
            try {
               return (Font)getCompositeFontUIResource.invoke((Object)null, var0);
            } catch (IllegalAccessException var3) {
            } catch (InvocationTargetException var4) {
            }
         }

         return var0;
      }
   }
}
