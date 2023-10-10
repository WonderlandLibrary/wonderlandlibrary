package com.ibm.icu.util;

import com.ibm.icu.impl.ICULocaleService;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUService;
import java.util.Locale;

final class CurrencyServiceShim extends Currency.ServiceShim {
   static final ICULocaleService service = new CurrencyServiceShim.CFService();

   Locale[] getAvailableLocales() {
      return service.isDefault() ? ICUResourceBundle.getAvailableLocales() : service.getAvailableLocales();
   }

   ULocale[] getAvailableULocales() {
      return service.isDefault() ? ICUResourceBundle.getAvailableULocales() : service.getAvailableULocales();
   }

   Currency createInstance(ULocale var1) {
      if (service.isDefault()) {
         return Currency.createCurrency(var1);
      } else {
         Currency var2 = (Currency)service.get(var1);
         return var2;
      }
   }

   Object registerInstance(Currency var1, ULocale var2) {
      return service.registerObject(var1, var2);
   }

   boolean unregister(Object var1) {
      return service.unregisterFactory((ICUService.Factory)var1);
   }

   private static class CFService extends ICULocaleService {
      CFService() {
         super("Currency");

         class CurrencyFactory extends ICULocaleService.ICUResourceBundleFactory {
            final CurrencyServiceShim.CFService this$0;

            CurrencyFactory(CurrencyServiceShim.CFService var1) {
               this.this$0 = var1;
            }

            protected Object handleCreate(ULocale var1, int var2, ICUService var3) {
               return Currency.createCurrency(var1);
            }
         }

         this.registerFactory(new CurrencyFactory(this));
         this.markDefault();
      }
   }
}
