package com.ibm.icu.text;

import com.ibm.icu.impl.ICULocaleService;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUService;
import com.ibm.icu.util.ULocale;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

final class CollatorServiceShim extends Collator.ServiceShim {
  Collator getInstance(ULocale locale) {
    try {
      ULocale[] actualLoc = new ULocale[1];
      Collator coll = (Collator)service.get(locale, actualLoc);
      if (coll == null)
        throw new MissingResourceException("Could not locate Collator data", "", ""); 
      coll = (Collator)coll.clone();
      coll.setLocale(actualLoc[0], actualLoc[0]);
      return coll;
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException(e.getMessage());
    } 
  }
  
  Object registerInstance(Collator collator, ULocale locale) {
    return service.registerObject(collator, locale);
  }
  
  Object registerFactory(Collator.CollatorFactory f) {
    class CFactory extends ICULocaleService.LocaleKeyFactory {
      Collator.CollatorFactory delegate;
      
      CFactory(Collator.CollatorFactory fctry) {
        super(fctry.visible());
        this.delegate = fctry;
      }
      
      public Object handleCreate(ULocale loc, int kind, ICUService srvc) {
        Object coll = this.delegate.createCollator(loc);
        return coll;
      }
      
      public String getDisplayName(String id, ULocale displayLocale) {
        ULocale objectLocale = new ULocale(id);
        return this.delegate.getDisplayName(objectLocale, displayLocale);
      }
      
      public Set<String> getSupportedIDs() {
        return this.delegate.getSupportedLocaleIDs();
      }
    };
    return service.registerFactory((ICUService.Factory)new CFactory(f));
  }
  
  boolean unregister(Object registryKey) {
    return service.unregisterFactory((ICUService.Factory)registryKey);
  }
  
  Locale[] getAvailableLocales() {
    Locale[] result;
    if (service.isDefault()) {
      result = ICUResourceBundle.getAvailableLocales("com/ibm/icu/impl/data/icudt51b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    } else {
      result = service.getAvailableLocales();
    } 
    return result;
  }
  
  ULocale[] getAvailableULocales() {
    ULocale[] result;
    if (service.isDefault()) {
      result = ICUResourceBundle.getAvailableULocales("com/ibm/icu/impl/data/icudt51b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    } else {
      result = service.getAvailableULocales();
    } 
    return result;
  }
  
  String getDisplayName(ULocale objectLocale, ULocale displayLocale) {
    String id = objectLocale.getName();
    return service.getDisplayName(id, displayLocale);
  }
  
  private static class CService extends ICULocaleService {
    CService() {
      super("Collator");
      class CollatorFactory extends ICULocaleService.ICUResourceBundleFactory {
        CollatorFactory() {
          super("com/ibm/icu/impl/data/icudt51b/coll");
        }
        
        protected Object handleCreate(ULocale uloc, int kind, ICUService srvc) {
          return new RuleBasedCollator(uloc);
        }
      };
      registerFactory((ICUService.Factory)new CollatorFactory());
      markDefault();
    }
    
    protected Object handleDefault(ICUService.Key key, String[] actualIDReturn) {
      if (actualIDReturn != null)
        actualIDReturn[0] = "root"; 
      try {
        return new RuleBasedCollator(ULocale.ROOT);
      } catch (MissingResourceException e) {
        return null;
      } 
    }
  }
  
  private static ICULocaleService service = new CService();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CollatorServiceShim.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */