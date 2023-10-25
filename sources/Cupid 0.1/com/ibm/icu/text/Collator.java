package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.VersionInfo;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

public abstract class Collator implements Comparator<Object>, Freezable<Collator> {
  public static final int PRIMARY = 0;
  
  public static final int SECONDARY = 1;
  
  public static final int TERTIARY = 2;
  
  public static final int QUATERNARY = 3;
  
  public static final int IDENTICAL = 15;
  
  public static final int FULL_DECOMPOSITION = 15;
  
  public static final int NO_DECOMPOSITION = 16;
  
  public static final int CANONICAL_DECOMPOSITION = 17;
  
  private static ServiceShim shim;
  
  public void setStrength(int newStrength) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (newStrength != 0 && newStrength != 1 && newStrength != 2 && newStrength != 3 && newStrength != 15)
      throw new IllegalArgumentException("Incorrect comparison level."); 
    this.m_strength_ = newStrength;
  }
  
  public Collator setStrength2(int newStrength) {
    setStrength(newStrength);
    return this;
  }
  
  public void setDecomposition(int decomposition) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    internalSetDecomposition(decomposition);
  }
  
  protected void internalSetDecomposition(int decomposition) {
    if (decomposition != 16 && decomposition != 17)
      throw new IllegalArgumentException("Wrong decomposition mode."); 
    this.m_decomposition_ = decomposition;
    if (decomposition != 16)
      Norm2AllModes.getFCDNormalizer2(); 
  }
  
  public void setReorderCodes(int... order) {
    throw new UnsupportedOperationException();
  }
  
  public static final Collator getInstance() {
    return getInstance(ULocale.getDefault());
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  public static interface ReorderCodes {
    public static final int DEFAULT = -1;
    
    public static final int NONE = 103;
    
    public static final int OTHERS = 103;
    
    public static final int SPACE = 4096;
    
    public static final int FIRST = 4096;
    
    public static final int PUNCTUATION = 4097;
    
    public static final int SYMBOL = 4098;
    
    public static final int CURRENCY = 4099;
    
    public static final int DIGIT = 4100;
    
    public static final int LIMIT = 4101;
  }
  
  public static abstract class CollatorFactory {
    public boolean visible() {
      return true;
    }
    
    public Collator createCollator(ULocale loc) {
      return createCollator(loc.toLocale());
    }
    
    public Collator createCollator(Locale loc) {
      return createCollator(ULocale.forLocale(loc));
    }
    
    public String getDisplayName(Locale objectLocale, Locale displayLocale) {
      return getDisplayName(ULocale.forLocale(objectLocale), ULocale.forLocale(displayLocale));
    }
    
    public String getDisplayName(ULocale objectLocale, ULocale displayLocale) {
      if (visible()) {
        Set<String> supported = getSupportedLocaleIDs();
        String name = objectLocale.getBaseName();
        if (supported.contains(name))
          return objectLocale.getDisplayName(displayLocale); 
      } 
      return null;
    }
    
    public abstract Set<String> getSupportedLocaleIDs();
  }
  
  static abstract class ServiceShim {
    abstract Collator getInstance(ULocale param1ULocale);
    
    abstract Object registerInstance(Collator param1Collator, ULocale param1ULocale);
    
    abstract Object registerFactory(Collator.CollatorFactory param1CollatorFactory);
    
    abstract boolean unregister(Object param1Object);
    
    abstract Locale[] getAvailableLocales();
    
    abstract ULocale[] getAvailableULocales();
    
    abstract String getDisplayName(ULocale param1ULocale1, ULocale param1ULocale2);
  }
  
  private static ServiceShim getShim() {
    if (shim == null)
      try {
        Class<?> cls = Class.forName("com.ibm.icu.text.CollatorServiceShim");
        shim = (ServiceShim)cls.newInstance();
      } catch (MissingResourceException e) {
        throw e;
      } catch (Exception e) {
        if (DEBUG)
          e.printStackTrace(); 
        throw new RuntimeException(e.getMessage());
      }  
    return shim;
  }
  
  public static final Collator getInstance(ULocale locale) {
    return getShim().getInstance(locale);
  }
  
  public static final Collator getInstance(Locale locale) {
    return getInstance(ULocale.forLocale(locale));
  }
  
  public static final Object registerInstance(Collator collator, ULocale locale) {
    return getShim().registerInstance(collator, locale);
  }
  
  public static final Object registerFactory(CollatorFactory factory) {
    return getShim().registerFactory(factory);
  }
  
  public static final boolean unregister(Object registryKey) {
    if (shim == null)
      return false; 
    return shim.unregister(registryKey);
  }
  
  public static Locale[] getAvailableLocales() {
    if (shim == null)
      return ICUResourceBundle.getAvailableLocales("com/ibm/icu/impl/data/icudt51b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER); 
    return shim.getAvailableLocales();
  }
  
  public static final ULocale[] getAvailableULocales() {
    if (shim == null)
      return ICUResourceBundle.getAvailableULocales("com/ibm/icu/impl/data/icudt51b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER); 
    return shim.getAvailableULocales();
  }
  
  private static final String[] KEYWORDS = new String[] { "collation" };
  
  private static final String RESOURCE = "collations";
  
  private static final String BASE = "com/ibm/icu/impl/data/icudt51b/coll";
  
  private int m_strength_;
  
  private int m_decomposition_;
  
  public static final String[] getKeywords() {
    return KEYWORDS;
  }
  
  public static final String[] getKeywordValues(String keyword) {
    if (!keyword.equals(KEYWORDS[0]))
      throw new IllegalArgumentException("Invalid keyword: " + keyword); 
    return ICUResourceBundle.getKeywordValues("com/ibm/icu/impl/data/icudt51b/coll", "collations");
  }
  
  public static final String[] getKeywordValuesForLocale(String key, ULocale locale, boolean commonlyUsed) {
    String baseLoc = locale.getBaseName();
    LinkedList<String> values = new LinkedList<String>();
    UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/coll", baseLoc);
    String defcoll = null;
    while (bundle != null) {
      UResourceBundle collations = bundle.get("collations");
      Enumeration<String> collEnum = collations.getKeys();
      while (collEnum.hasMoreElements()) {
        String collkey = collEnum.nextElement();
        if (collkey.equals("default")) {
          if (defcoll == null)
            defcoll = collations.getString("default"); 
          continue;
        } 
        if (!values.contains(collkey))
          values.add(collkey); 
      } 
      bundle = ((ICUResourceBundle)bundle).getParent();
    } 
    Iterator<String> itr = values.iterator();
    String[] result = new String[values.size()];
    result[0] = defcoll;
    int idx = 1;
    while (itr.hasNext()) {
      String collKey = itr.next();
      if (!collKey.equals(defcoll))
        result[idx++] = collKey; 
    } 
    return result;
  }
  
  public static final ULocale getFunctionalEquivalent(String keyword, ULocale locID, boolean[] isAvailable) {
    return ICUResourceBundle.getFunctionalEquivalent("com/ibm/icu/impl/data/icudt51b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER, "collations", keyword, locID, isAvailable, true);
  }
  
  public static final ULocale getFunctionalEquivalent(String keyword, ULocale locID) {
    return getFunctionalEquivalent(keyword, locID, null);
  }
  
  public static String getDisplayName(Locale objectLocale, Locale displayLocale) {
    return getShim().getDisplayName(ULocale.forLocale(objectLocale), ULocale.forLocale(displayLocale));
  }
  
  public static String getDisplayName(ULocale objectLocale, ULocale displayLocale) {
    return getShim().getDisplayName(objectLocale, displayLocale);
  }
  
  public static String getDisplayName(Locale objectLocale) {
    return getShim().getDisplayName(ULocale.forLocale(objectLocale), ULocale.getDefault(ULocale.Category.DISPLAY));
  }
  
  public static String getDisplayName(ULocale objectLocale) {
    return getShim().getDisplayName(objectLocale, ULocale.getDefault(ULocale.Category.DISPLAY));
  }
  
  public int getStrength() {
    return this.m_strength_;
  }
  
  public int getDecomposition() {
    return this.m_decomposition_;
  }
  
  public boolean equals(String source, String target) {
    return (compare(source, target) == 0);
  }
  
  public UnicodeSet getTailoredSet() {
    return new UnicodeSet(0, 1114111);
  }
  
  public int compare(Object source, Object target) {
    return compare((String)source, (String)target);
  }
  
  public int[] getReorderCodes() {
    throw new UnsupportedOperationException();
  }
  
  public static int[] getEquivalentReorderCodes(int reorderCode) {
    throw new UnsupportedOperationException();
  }
  
  public boolean isFrozen() {
    return false;
  }
  
  public Collator freeze() {
    throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
  }
  
  public Collator cloneAsThawed() {
    throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
  }
  
  protected Collator() {
    this.m_strength_ = 2;
    this.m_decomposition_ = 17;
  }
  
  private static final boolean DEBUG = ICUDebug.enabled("collator");
  
  private ULocale validLocale;
  
  private ULocale actualLocale;
  
  public final ULocale getLocale(ULocale.Type type) {
    return (type == ULocale.ACTUAL_LOCALE) ? this.actualLocale : this.validLocale;
  }
  
  final void setLocale(ULocale valid, ULocale actual) {
    if (((valid == null) ? true : false) != ((actual == null) ? true : false))
      throw new IllegalArgumentException(); 
    this.validLocale = valid;
    this.actualLocale = actual;
  }
  
  public abstract int compare(String paramString1, String paramString2);
  
  public abstract CollationKey getCollationKey(String paramString);
  
  public abstract RawCollationKey getRawCollationKey(String paramString, RawCollationKey paramRawCollationKey);
  
  public abstract int setVariableTop(String paramString);
  
  public abstract int getVariableTop();
  
  public abstract void setVariableTop(int paramInt);
  
  public abstract VersionInfo getVersion();
  
  public abstract VersionInfo getUCAVersion();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\Collator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */