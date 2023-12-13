package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.ICULocaleService;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUService;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.util.Locale;
import java.util.MissingResourceException;

final class BreakIteratorFactory extends BreakIterator.BreakIteratorServiceShim {
   static final ICULocaleService service = new BreakIteratorFactory.BFService();
   private static final String[] KIND_NAMES = new String[]{"grapheme", "word", "line", "sentence", "title"};

   public Object registerInstance(BreakIterator var1, ULocale var2, int var3) {
      var1.setText((CharacterIterator)(new java.text.StringCharacterIterator("")));
      return service.registerObject(var1, var2, var3);
   }

   public boolean unregister(Object var1) {
      return service.isDefault() ? false : service.unregisterFactory((ICUService.Factory)var1);
   }

   public Locale[] getAvailableLocales() {
      return service == null ? ICUResourceBundle.getAvailableLocales() : service.getAvailableLocales();
   }

   public ULocale[] getAvailableULocales() {
      return service == null ? ICUResourceBundle.getAvailableULocales() : service.getAvailableULocales();
   }

   public BreakIterator createBreakIterator(ULocale var1, int var2) {
      if (service.isDefault()) {
         return createBreakInstance(var1, var2);
      } else {
         ULocale[] var3 = new ULocale[1];
         BreakIterator var4 = (BreakIterator)service.get(var1, var2, var3);
         var4.setLocale(var3[0], var3[0]);
         return var4;
      }
   }

   private static BreakIterator createBreakInstance(ULocale var0, int var1) {
      RuleBasedBreakIterator var2 = null;
      ICUResourceBundle var3 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/brkitr", var0);
      InputStream var4 = null;

      try {
         String var5 = KIND_NAMES[var1];
         String var6 = var3.getStringWithFallback("boundaries/" + var5);
         String var7 = "data/icudt51b/brkitr/" + var6;
         var4 = ICUData.getStream(var7);
      } catch (Exception var9) {
         throw new MissingResourceException(var9.toString(), "", "");
      }

      try {
         var2 = RuleBasedBreakIterator.getInstanceFromCompiledRules(var4);
      } catch (IOException var8) {
         Assert.fail((Exception)var8);
      }

      ULocale var10 = ULocale.forLocale(var3.getLocale());
      var2.setLocale(var10, var10);
      var2.setBreakType(var1);
      return var2;
   }

   static BreakIterator access$000(ULocale var0, int var1) {
      return createBreakInstance(var0, var1);
   }

   private static class BFService extends ICULocaleService {
      BFService() {
         super("BreakIterator");

         class RBBreakIteratorFactory extends ICULocaleService.ICUResourceBundleFactory {
            final BreakIteratorFactory.BFService this$0;

            RBBreakIteratorFactory(BreakIteratorFactory.BFService var1) {
               this.this$0 = var1;
            }

            protected Object handleCreate(ULocale var1, int var2, ICUService var3) {
               return BreakIteratorFactory.access$000(var1, var2);
            }
         }

         this.registerFactory(new RBBreakIteratorFactory(this));
         this.markDefault();
      }
   }
}
