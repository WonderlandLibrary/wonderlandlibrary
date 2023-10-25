package com.ibm.icu.impl;

import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.VersionInfo;
import java.util.MissingResourceException;

public final class ICUDataVersion {
  private static final String U_ICU_VERSION_BUNDLE = "icuver";
  
  private static final String U_ICU_DATA_KEY = "DataVersion";
  
  public static VersionInfo getDataVersion() {
    UResourceBundle icudatares = null;
    try {
      icudatares = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "icuver", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
      icudatares = icudatares.get("DataVersion");
    } catch (MissingResourceException ex) {
      return null;
    } 
    return VersionInfo.getInstance(icudatares.getString());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ICUDataVersion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */