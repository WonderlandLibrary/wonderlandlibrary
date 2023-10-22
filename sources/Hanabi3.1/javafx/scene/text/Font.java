package javafx.scene.text;

import com.sun.javafx.tk.Toolkit;
import java.io.File;
import java.io.FilePermission;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javafx.beans.NamedArg;

public final class Font {
   private static final String DEFAULT_FAMILY = "System";
   private static final String DEFAULT_FULLNAME = "System Regular";
   private static float defaultSystemFontSize = -1.0F;
   private static Font DEFAULT;
   private String name;
   private String family;
   private String style;
   private double size;
   private int hash;
   private Object nativeFont;

   private static float getDefaultSystemFontSize() {
      if (defaultSystemFontSize == -1.0F) {
         defaultSystemFontSize = Toolkit.getToolkit().getFontLoader().getSystemFontSize();
      }

      return defaultSystemFontSize;
   }

   public static synchronized Font getDefault() {
      if (DEFAULT == null) {
         DEFAULT = new Font("System Regular", (double)getDefaultSystemFontSize());
      }

      return DEFAULT;
   }

   public static List getFamilies() {
      return Toolkit.getToolkit().getFontLoader().getFamilies();
   }

   public static List getFontNames() {
      return Toolkit.getToolkit().getFontLoader().getFontNames();
   }

   public static List getFontNames(String var0) {
      return Toolkit.getToolkit().getFontLoader().getFontNames(var0);
   }

   public static Font font(String var0, FontWeight var1, FontPosture var2, double var3) {
      String var5 = var0 != null && !"".equals(var0) ? var0 : "System";
      double var6 = var3 < 0.0 ? (double)getDefaultSystemFontSize() : var3;
      return Toolkit.getToolkit().getFontLoader().font(var5, var1, var2, (float)var6);
   }

   public static Font font(String var0, FontWeight var1, double var2) {
      return font(var0, var1, (FontPosture)null, var2);
   }

   public static Font font(String var0, FontPosture var1, double var2) {
      return font(var0, (FontWeight)null, var1, var2);
   }

   public static Font font(String var0, double var1) {
      return font(var0, (FontWeight)null, (FontPosture)null, var1);
   }

   public static Font font(String var0) {
      return font(var0, (FontWeight)null, (FontPosture)null, -1.0);
   }

   public static Font font(double var0) {
      return font((String)null, (FontWeight)null, (FontPosture)null, var0);
   }

   public final String getName() {
      return this.name;
   }

   public final String getFamily() {
      return this.family;
   }

   public final String getStyle() {
      return this.style;
   }

   public final double getSize() {
      return this.size;
   }

   public Font(@NamedArg("size") double var1) {
      this((String)null, var1);
   }

   public Font(@NamedArg("name") String var1, @NamedArg("size") double var2) {
      this.hash = 0;
      this.name = var1;
      this.size = var2;
      if (var1 == null || "".equals(var1)) {
         this.name = "System Regular";
      }

      if (var2 < 0.0) {
         this.size = (double)getDefaultSystemFontSize();
      }

      Toolkit.getToolkit().getFontLoader().loadFont(this);
   }

   private Font(Object var1, String var2, String var3, String var4, double var5) {
      this.hash = 0;
      this.nativeFont = var1;
      this.family = var2;
      this.name = var3;
      this.style = var4;
      this.size = var5;
   }

   public static Font loadFont(String var0, double var1) {
      URL var3 = null;

      try {
         var3 = new URL(var0);
      } catch (Exception var20) {
         return null;
      }

      if (var1 <= 0.0) {
         var1 = (double)getDefaultSystemFontSize();
      }

      if (var3.getProtocol().equals("file")) {
         String var24 = var3.getFile();
         var24 = (new File(var24)).getPath();

         try {
            SecurityManager var25 = System.getSecurityManager();
            if (var25 != null) {
               FilePermission var26 = new FilePermission(var24, "read");
               var25.checkPermission(var26);
            }
         } catch (Exception var21) {
            return null;
         }

         return Toolkit.getToolkit().getFontLoader().loadFont(var24, var1);
      } else {
         Font var4 = null;
         URLConnection var5 = null;
         InputStream var6 = null;

         Object var8;
         try {
            var5 = var3.openConnection();
            var6 = var5.getInputStream();
            var4 = Toolkit.getToolkit().getFontLoader().loadFont(var6, var1);
            return var4;
         } catch (Exception var22) {
            var8 = null;
         } finally {
            try {
               if (var6 != null) {
                  var6.close();
               }
            } catch (Exception var19) {
            }

         }

         return (Font)var8;
      }
   }

   public static Font loadFont(InputStream var0, double var1) {
      if (var1 <= 0.0) {
         var1 = (double)getDefaultSystemFontSize();
      }

      return Toolkit.getToolkit().getFontLoader().loadFont(var0, var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Font[name=");
      var1 = var1.append(this.name);
      var1 = var1.append(", family=").append(this.family);
      var1 = var1.append(", style=").append(this.style);
      var1 = var1.append(", size=").append(this.size);
      var1 = var1.append("]");
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Font)) {
         return false;
      } else {
         boolean var10000;
         label23: {
            Font var2 = (Font)var1;
            if (this.name == null) {
               if (var2.name != null) {
                  break label23;
               }
            } else if (!this.name.equals(var2.name)) {
               break label23;
            }

            if (this.size == var2.size) {
               var10000 = true;
               return var10000;
            }
         }

         var10000 = false;
         return var10000;
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 17L;
         var1 = 37L * var1 + (long)this.name.hashCode();
         var1 = 37L * var1 + Double.doubleToLongBits(this.size);
         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   /** @deprecated */
   @Deprecated
   public Object impl_getNativeFont() {
      return this.nativeFont;
   }

   /** @deprecated */
   @Deprecated
   public void impl_setNativeFont(Object var1, String var2, String var3, String var4) {
      this.nativeFont = var1;
      this.name = var2;
      this.family = var3;
      this.style = var4;
   }

   /** @deprecated */
   @Deprecated
   public static Font impl_NativeFont(Object var0, String var1, String var2, String var3, double var4) {
      Font var6 = new Font(var0, var2, var1, var3, var4);
      return var6;
   }
}
