package com.sun.webkit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

final class LocalizedStrings {
   private static final Logger log = Logger.getLogger(LocalizedStrings.class.getName());
   private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("com.sun.webkit.LocalizedStrings", Locale.getDefault(), new EncodingResourceBundleControl("utf-8"));

   private LocalizedStrings() {
   }

   private static String getLocalizedProperty(String var0) {
      log.log(Level.FINE, "Get property: " + var0);
      String var1 = BUNDLE.getString(var0);
      if (var1 != null && var1.trim().length() > 0) {
         log.log(Level.FINE, "Property value: " + var1);
         return var1.trim();
      } else {
         log.log(Level.FINE, "Unknown property value");
         return null;
      }
   }

   private static final class EncodingResourceBundleControl extends ResourceBundle.Control {
      private final String encoding;

      private EncodingResourceBundleControl(String var1) {
         this.encoding = var1;
      }

      public ResourceBundle newBundle(String var1, Locale var2, String var3, ClassLoader var4, boolean var5) throws IllegalAccessException, InstantiationException, IOException {
         String var6 = this.toBundleName(var1, var2);
         String var7 = this.toResourceName(var6, "properties");
         URL var8 = var4.getResource(var7);
         if (var8 != null) {
            try {
               return new PropertyResourceBundle(new InputStreamReader(var8.openStream(), this.encoding));
            } catch (Exception var10) {
               LocalizedStrings.log.log(Level.FINE, "exception thrown during bundle initialization", var10);
            }
         }

         return super.newBundle(var1, var2, var3, var4, var5);
      }

      // $FF: synthetic method
      EncodingResourceBundleControl(String var1, Object var2) {
         this(var1);
      }
   }
}
