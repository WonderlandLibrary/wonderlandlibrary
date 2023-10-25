package com.ibm.icu.util;

import com.ibm.icu.impl.ICULocaleService;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUService;
import java.util.Locale;

final class CurrencyServiceShim extends Currency.ServiceShim {
  Locale[] getAvailableLocales() {
    if (service.isDefault())
      return ICUResourceBundle.getAvailableLocales(); 
    return service.getAvailableLocales();
  }
  
  ULocale[] getAvailableULocales() {
    if (service.isDefault())
      return ICUResourceBundle.getAvailableULocales(); 
    return service.getAvailableULocales();
  }
  
  Currency createInstance(ULocale loc) {
    if (service.isDefault())
      return Currency.createCurrency(loc); 
    Currency curr = (Currency)service.get(loc);
    return curr;
  }
  
  Object registerInstance(Currency currency, ULocale locale) {
    return service.registerObject(currency, locale);
  }
  
  boolean unregister(Object registryKey) {
    return service.unregisterFactory((ICUService.Factory)registryKey);
  }
  
  private static class CFService extends ICULocaleService {
    CFService() {
      super("Currency");
      class CurrencyFactory extends ICULocaleService.ICUResourceBundleFactory {
        protected Object handleCreate(ULocale loc, int kind, ICUService srvc) {
          return Currency.createCurrency(loc);
        }
      };
      registerFactory((ICUService.Factory)new CurrencyFactory());
      markDefault();
    }
  }
  
  static final ICULocaleService service = new CFService();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\CurrencyServiceShim.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */