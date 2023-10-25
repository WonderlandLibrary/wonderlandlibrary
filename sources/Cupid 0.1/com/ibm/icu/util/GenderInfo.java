package com.ibm.icu.util;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

public class GenderInfo {
  private final ListGenderStyle style;
  
  public enum Gender {
    MALE, FEMALE, OTHER;
  }
  
  public static GenderInfo getInstance(ULocale uLocale) {
    return genderInfoCache.get(uLocale);
  }
  
  public static GenderInfo getInstance(Locale locale) {
    return getInstance(ULocale.forLocale(locale));
  }
  
  public enum ListGenderStyle {
    NEUTRAL, MIXED_NEUTRAL, MALE_TAINTS;
    
    private static Map<String, ListGenderStyle> fromNameMap = new HashMap<String, ListGenderStyle>(3);
    
    static {
      fromNameMap.put("neutral", NEUTRAL);
      fromNameMap.put("maleTaints", MALE_TAINTS);
      fromNameMap.put("mixedNeutral", MIXED_NEUTRAL);
    }
    
    public static ListGenderStyle fromName(String name) {
      ListGenderStyle result = fromNameMap.get(name);
      if (result == null)
        throw new IllegalArgumentException("Unknown gender style name: " + name); 
      return result;
    }
  }
  
  public Gender getListGender(Gender... genders) {
    return getListGender(Arrays.asList(genders));
  }
  
  public Gender getListGender(List<Gender> genders) {
    boolean hasFemale;
    boolean hasMale;
    if (genders.size() == 0)
      return Gender.OTHER; 
    if (genders.size() == 1)
      return genders.get(0); 
    switch (this.style) {
      case NEUTRAL:
        return Gender.OTHER;
      case MIXED_NEUTRAL:
        hasFemale = false;
        hasMale = false;
        for (Gender gender : genders) {
          switch (gender) {
            case NEUTRAL:
              if (hasMale)
                return Gender.OTHER; 
              hasFemale = true;
            case MIXED_NEUTRAL:
              if (hasFemale)
                return Gender.OTHER; 
              hasMale = true;
            case MALE_TAINTS:
              return Gender.OTHER;
          } 
        } 
        return hasMale ? Gender.MALE : Gender.FEMALE;
      case MALE_TAINTS:
        for (Gender gender : genders) {
          if (gender != Gender.FEMALE)
            return Gender.MALE; 
        } 
        return Gender.FEMALE;
    } 
    return Gender.OTHER;
  }
  
  public GenderInfo(ListGenderStyle genderStyle) {
    this.style = genderStyle;
  }
  
  private static GenderInfo neutral = new GenderInfo(ListGenderStyle.NEUTRAL);
  
  private static class Cache {
    private final ICUCache<ULocale, GenderInfo> cache = (ICUCache<ULocale, GenderInfo>)new SimpleCache();
    
    public GenderInfo get(ULocale locale) {
      GenderInfo result = (GenderInfo)this.cache.get(locale);
      if (result == null) {
        result = load(locale);
        if (result == null) {
          ULocale fallback = locale.getFallback();
          result = (fallback == null) ? GenderInfo.neutral : get(fallback);
        } 
        this.cache.put(locale, result);
      } 
      return result;
    }
    
    private static GenderInfo load(ULocale ulocale) {
      UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "genderList", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
      UResourceBundle genderList = rb.get("genderList");
      try {
        return new GenderInfo(GenderInfo.ListGenderStyle.fromName(genderList.getString(ulocale.toString())));
      } catch (MissingResourceException mre) {
        return null;
      } 
    }
    
    private Cache() {}
  }
  
  private static Cache genderInfoCache = new Cache();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\GenderInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */