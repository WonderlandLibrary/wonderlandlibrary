package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.MissingResourceException;

public class CalendarUtil {
  private static ICUCache<String, String> CALTYPE_CACHE = new SimpleCache<String, String>();
  
  private static final String CALKEY = "calendar";
  
  private static final String DEFCAL = "gregorian";
  
  public static String getCalendarType(ULocale loc) {
    String calType = null;
    calType = loc.getKeywordValue("calendar");
    if (calType != null)
      return calType; 
    String baseLoc = loc.getBaseName();
    calType = CALTYPE_CACHE.get(baseLoc);
    if (calType != null)
      return calType; 
    ULocale canonical = ULocale.createCanonical(loc.toString());
    calType = canonical.getKeywordValue("calendar");
    if (calType == null) {
      String region = canonical.getCountry();
      if (region.length() == 0) {
        ULocale fullLoc = ULocale.addLikelySubtags(canonical);
        region = fullLoc.getCountry();
      } 
      try {
        UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        UResourceBundle calPref = rb.get("calendarPreferenceData");
        UResourceBundle order = null;
        try {
          order = calPref.get(region);
        } catch (MissingResourceException mre) {
          order = calPref.get("001");
        } 
        calType = order.getString(0);
      } catch (MissingResourceException mre) {}
      if (calType == null)
        calType = "gregorian"; 
    } 
    CALTYPE_CACHE.put(baseLoc, calType);
    return calType;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\CalendarUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */