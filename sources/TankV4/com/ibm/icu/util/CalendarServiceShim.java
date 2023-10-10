package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.impl.ICULocaleService;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUService;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

class CalendarServiceShim extends Calendar.CalendarShim {
   private static ICULocaleService service = new CalendarServiceShim.CalService();

   Locale[] getAvailableLocales() {
      return service.isDefault() ? ICUResourceBundle.getAvailableLocales() : service.getAvailableLocales();
   }

   ULocale[] getAvailableULocales() {
      return service.isDefault() ? ICUResourceBundle.getAvailableULocales() : service.getAvailableULocales();
   }

   Calendar createInstance(ULocale var1) {
      ULocale[] var2 = new ULocale[1];
      if (var1.equals(ULocale.ROOT)) {
         var1 = ULocale.ROOT;
      }

      ULocale var3;
      if (var1.getKeywordValue("calendar") == null) {
         String var4 = CalendarUtil.getCalendarType(var1);
         var3 = var1.setKeywordValue("calendar", var4);
      } else {
         var3 = var1;
      }

      Calendar var5 = (Calendar)service.get(var3, var2);
      if (var5 == null) {
         throw new MissingResourceException("Unable to construct Calendar", "", "");
      } else {
         var5 = (Calendar)var5.clone();
         return var5;
      }
   }

   Object registerFactory(Calendar.CalendarFactory var1) {
      return service.registerFactory(new CalendarServiceShim.CalFactory(var1));
   }

   boolean unregister(Object var1) {
      return service.unregisterFactory((ICUService.Factory)var1);
   }

   private static class CalService extends ICULocaleService {
      CalService() {
         super("Calendar");

         class RBCalendarFactory extends ICULocaleService.ICUResourceBundleFactory {
            final CalendarServiceShim.CalService this$0;

            RBCalendarFactory(CalendarServiceShim.CalService var1) {
               this.this$0 = var1;
            }

            protected Object handleCreate(ULocale var1, int var2, ICUService var3) {
               return Calendar.createInstance(var1);
            }
         }

         this.registerFactory(new RBCalendarFactory(this));
         this.markDefault();
      }
   }

   private static final class CalFactory extends ICULocaleService.LocaleKeyFactory {
      private Calendar.CalendarFactory delegate;

      CalFactory(Calendar.CalendarFactory var1) {
         super(var1.visible());
         this.delegate = var1;
      }

      public Object create(ICUService.Key var1, ICUService var2) {
         if (this.handlesKey(var1) && var1 instanceof ICULocaleService.LocaleKey) {
            ICULocaleService.LocaleKey var3 = (ICULocaleService.LocaleKey)var1;
            Object var4 = this.delegate.createCalendar(var3.canonicalLocale());
            if (var4 == null) {
               var4 = var2.getKey(var1, (String[])null, this);
            }

            return var4;
         } else {
            return null;
         }
      }

      protected Set getSupportedIDs() {
         return this.delegate.getSupportedLocaleNames();
      }
   }
}
