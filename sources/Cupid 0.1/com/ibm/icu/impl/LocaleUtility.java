package com.ibm.icu.impl;

import java.util.Locale;

public class LocaleUtility {
  public static Locale getLocaleFromName(String name) {
    String language = "";
    String country = "";
    String variant = "";
    int i1 = name.indexOf('_');
    if (i1 < 0) {
      language = name;
    } else {
      language = name.substring(0, i1);
      i1++;
      int i2 = name.indexOf('_', i1);
      if (i2 < 0) {
        country = name.substring(i1);
      } else {
        country = name.substring(i1, i2);
        variant = name.substring(i2 + 1);
      } 
    } 
    return new Locale(language, country, variant);
  }
  
  public static boolean isFallbackOf(String parent, String child) {
    if (!child.startsWith(parent))
      return false; 
    int i = parent.length();
    return (i == child.length() || child.charAt(i) == '_');
  }
  
  public static boolean isFallbackOf(Locale parent, Locale child) {
    return isFallbackOf(parent.toString(), child.toString());
  }
  
  public static Locale fallback(Locale loc) {
    String[] parts = { loc.getLanguage(), loc.getCountry(), loc.getVariant() };
    int i;
    for (i = 2; i >= 0; i--) {
      if (parts[i].length() != 0) {
        parts[i] = "";
        break;
      } 
    } 
    if (i < 0)
      return null; 
    return new Locale(parts[0], parts[1], parts[2]);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\LocaleUtility.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */