package com.ibm.icu.util;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** @deprecated */
public class OverlayBundle extends ResourceBundle {
   private String[] baseNames;
   private Locale locale;
   private ResourceBundle[] bundles;

   /** @deprecated */
   public OverlayBundle(String[] var1, Locale var2) {
      this.baseNames = var1;
      this.locale = var2;
      this.bundles = new ResourceBundle[var1.length];
   }

   /** @deprecated */
   protected Object handleGetObject(String var1) throws MissingResourceException {
      Object var2 = null;

      for(int var3 = 0; var3 < this.bundles.length; ++var3) {
         this.load(var3);

         try {
            var2 = this.bundles[var3].getObject(var1);
         } catch (MissingResourceException var5) {
            if (var3 == this.bundles.length - 1) {
               throw var5;
            }
         }

         if (var2 != null) {
            break;
         }
      }

      return var2;
   }

   /** @deprecated */
   public Enumeration getKeys() {
      int var1 = this.bundles.length - 1;
      this.load(var1);
      return this.bundles[var1].getKeys();
   }

   private void load(int var1) throws MissingResourceException {
      if (this.bundles[var1] == null) {
         boolean var2 = false;

         try {
            this.bundles[var1] = ResourceBundle.getBundle(this.baseNames[var1], this.locale);
            if (this.bundles[var1].getLocale().equals(this.locale)) {
               return;
            }

            if (this.locale.getCountry().length() != 0 && var1 != this.bundles.length - 1) {
               var2 = true;
            }
         } catch (MissingResourceException var6) {
            if (var1 == this.bundles.length - 1) {
               throw var6;
            }

            var2 = true;
         }

         if (var2) {
            Locale var3 = new Locale("xx", this.locale.getCountry(), this.locale.getVariant());

            try {
               this.bundles[var1] = ResourceBundle.getBundle(this.baseNames[var1], var3);
            } catch (MissingResourceException var5) {
               if (this.bundles[var1] == null) {
                  throw var5;
               }
            }
         }
      }

   }
}
