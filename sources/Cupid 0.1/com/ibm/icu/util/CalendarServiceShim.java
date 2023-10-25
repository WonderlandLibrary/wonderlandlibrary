package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.impl.ICULocaleService;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUService;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

class CalendarServiceShim extends Calendar.CalendarShim {
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
  
  private static final class CalFactory extends ICULocaleService.LocaleKeyFactory {
    private Calendar.CalendarFactory delegate;
    
    CalFactory(Calendar.CalendarFactory delegate) {
      super(delegate.visible());
      this.delegate = delegate;
    }
    
    public Object create(ICUService.Key key, ICUService srvc) {
      if (!handlesKey(key) || !(key instanceof ICULocaleService.LocaleKey))
        return null; 
      ICULocaleService.LocaleKey lkey = (ICULocaleService.LocaleKey)key;
      Object result = this.delegate.createCalendar(lkey.canonicalLocale());
      if (result == null)
        result = srvc.getKey(key, null, (ICUService.Factory)this); 
      return result;
    }
    
    protected Set<String> getSupportedIDs() {
      return this.delegate.getSupportedLocaleNames();
    }
  }
  
  Calendar createInstance(ULocale desiredLocale) {
    ULocale useLocale, actualLoc[] = new ULocale[1];
    if (desiredLocale.equals(ULocale.ROOT))
      desiredLocale = ULocale.ROOT; 
    if (desiredLocale.getKeywordValue("calendar") == null) {
      String calType = CalendarUtil.getCalendarType(desiredLocale);
      useLocale = desiredLocale.setKeywordValue("calendar", calType);
    } else {
      useLocale = desiredLocale;
    } 
    Calendar cal = (Calendar)service.get(useLocale, actualLoc);
    if (cal == null)
      throw new MissingResourceException("Unable to construct Calendar", "", ""); 
    cal = (Calendar)cal.clone();
    return cal;
  }
  
  Object registerFactory(Calendar.CalendarFactory factory) {
    return service.registerFactory((ICUService.Factory)new CalFactory(factory));
  }
  
  boolean unregister(Object k) {
    return service.unregisterFactory((ICUService.Factory)k);
  }
  
  private static class CalService extends ICULocaleService {
    CalService() {
      super("Calendar");
      class RBCalendarFactory extends ICULocaleService.ICUResourceBundleFactory {
        protected Object handleCreate(ULocale loc, int kind, ICUService sercice) {
          return Calendar.createInstance(loc);
        }
      };
      registerFactory((ICUService.Factory)new RBCalendarFactory());
      markDefault();
    }
  }
  
  private static ICULocaleService service = new CalService();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\CalendarServiceShim.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */