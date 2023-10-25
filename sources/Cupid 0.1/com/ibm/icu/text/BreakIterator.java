package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.util.ULocale;
import java.lang.ref.SoftReference;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;
import java.util.MissingResourceException;

public abstract class BreakIterator implements Cloneable {
  private static final boolean DEBUG = ICUDebug.enabled("breakiterator");
  
  public static final int DONE = -1;
  
  public static final int KIND_CHARACTER = 0;
  
  public static final int KIND_WORD = 1;
  
  public static final int KIND_LINE = 2;
  
  public static final int KIND_SENTENCE = 3;
  
  public static final int KIND_TITLE = 4;
  
  private static final int KIND_COUNT = 5;
  
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException();
    } 
  }
  
  public abstract int first();
  
  public abstract int last();
  
  public abstract int next(int paramInt);
  
  public abstract int next();
  
  public abstract int previous();
  
  public abstract int following(int paramInt);
  
  public int preceding(int offset) {
    int pos = following(offset);
    while (pos >= offset && pos != -1)
      pos = previous(); 
    return pos;
  }
  
  public boolean isBoundary(int offset) {
    if (offset == 0)
      return true; 
    return (following(offset - 1) == offset);
  }
  
  public abstract int current();
  
  public abstract CharacterIterator getText();
  
  public void setText(String newText) {
    setText(new StringCharacterIterator(newText));
  }
  
  private static final SoftReference<?>[] iterCache = (SoftReference<?>[])new SoftReference[5];
  
  private static BreakIteratorServiceShim shim;
  
  private ULocale validLocale;
  
  private ULocale actualLocale;
  
  public abstract void setText(CharacterIterator paramCharacterIterator);
  
  public static BreakIterator getWordInstance() {
    return getWordInstance(ULocale.getDefault());
  }
  
  public static BreakIterator getWordInstance(Locale where) {
    return getBreakInstance(ULocale.forLocale(where), 1);
  }
  
  public static BreakIterator getWordInstance(ULocale where) {
    return getBreakInstance(where, 1);
  }
  
  public static BreakIterator getLineInstance() {
    return getLineInstance(ULocale.getDefault());
  }
  
  public static BreakIterator getLineInstance(Locale where) {
    return getBreakInstance(ULocale.forLocale(where), 2);
  }
  
  public static BreakIterator getLineInstance(ULocale where) {
    return getBreakInstance(where, 2);
  }
  
  public static BreakIterator getCharacterInstance() {
    return getCharacterInstance(ULocale.getDefault());
  }
  
  public static BreakIterator getCharacterInstance(Locale where) {
    return getBreakInstance(ULocale.forLocale(where), 0);
  }
  
  public static BreakIterator getCharacterInstance(ULocale where) {
    return getBreakInstance(where, 0);
  }
  
  public static BreakIterator getSentenceInstance() {
    return getSentenceInstance(ULocale.getDefault());
  }
  
  public static BreakIterator getSentenceInstance(Locale where) {
    return getBreakInstance(ULocale.forLocale(where), 3);
  }
  
  public static BreakIterator getSentenceInstance(ULocale where) {
    return getBreakInstance(where, 3);
  }
  
  public static BreakIterator getTitleInstance() {
    return getTitleInstance(ULocale.getDefault());
  }
  
  public static BreakIterator getTitleInstance(Locale where) {
    return getBreakInstance(ULocale.forLocale(where), 4);
  }
  
  public static BreakIterator getTitleInstance(ULocale where) {
    return getBreakInstance(where, 4);
  }
  
  public static Object registerInstance(BreakIterator iter, Locale locale, int kind) {
    return registerInstance(iter, ULocale.forLocale(locale), kind);
  }
  
  public static Object registerInstance(BreakIterator iter, ULocale locale, int kind) {
    if (iterCache[kind] != null) {
      BreakIteratorCache cache = (BreakIteratorCache)iterCache[kind].get();
      if (cache != null && 
        cache.getLocale().equals(locale))
        iterCache[kind] = null; 
    } 
    return getShim().registerInstance(iter, locale, kind);
  }
  
  public static boolean unregister(Object key) {
    if (key == null)
      throw new IllegalArgumentException("registry key must not be null"); 
    if (shim != null) {
      for (int kind = 0; kind < 5; kind++)
        iterCache[kind] = null; 
      return shim.unregister(key);
    } 
    return false;
  }
  
  public static BreakIterator getBreakInstance(ULocale where, int kind) {
    if (iterCache[kind] != null) {
      BreakIteratorCache breakIteratorCache = (BreakIteratorCache)iterCache[kind].get();
      if (breakIteratorCache != null && 
        breakIteratorCache.getLocale().equals(where))
        return breakIteratorCache.createBreakInstance(); 
    } 
    BreakIterator result = getShim().createBreakIterator(where, kind);
    BreakIteratorCache cache = new BreakIteratorCache(where, result);
    iterCache[kind] = new SoftReference(cache);
    if (result instanceof RuleBasedBreakIterator) {
      RuleBasedBreakIterator rbbi = (RuleBasedBreakIterator)result;
      rbbi.setBreakType(kind);
    } 
    return result;
  }
  
  public static synchronized Locale[] getAvailableLocales() {
    return getShim().getAvailableLocales();
  }
  
  public static synchronized ULocale[] getAvailableULocales() {
    return getShim().getAvailableULocales();
  }
  
  private static final class BreakIteratorCache {
    private BreakIterator iter;
    
    private ULocale where;
    
    BreakIteratorCache(ULocale where, BreakIterator iter) {
      this.where = where;
      this.iter = (BreakIterator)iter.clone();
    }
    
    ULocale getLocale() {
      return this.where;
    }
    
    BreakIterator createBreakInstance() {
      return (BreakIterator)this.iter.clone();
    }
  }
  
  static abstract class BreakIteratorServiceShim {
    public abstract Object registerInstance(BreakIterator param1BreakIterator, ULocale param1ULocale, int param1Int);
    
    public abstract boolean unregister(Object param1Object);
    
    public abstract Locale[] getAvailableLocales();
    
    public abstract ULocale[] getAvailableULocales();
    
    public abstract BreakIterator createBreakIterator(ULocale param1ULocale, int param1Int);
  }
  
  private static BreakIteratorServiceShim getShim() {
    if (shim == null)
      try {
        Class<?> cls = Class.forName("com.ibm.icu.text.BreakIteratorFactory");
        shim = (BreakIteratorServiceShim)cls.newInstance();
      } catch (MissingResourceException e) {
        throw e;
      } catch (Exception e) {
        if (DEBUG)
          e.printStackTrace(); 
        throw new RuntimeException(e.getMessage());
      }  
    return shim;
  }
  
  public final ULocale getLocale(ULocale.Type type) {
    return (type == ULocale.ACTUAL_LOCALE) ? this.actualLocale : this.validLocale;
  }
  
  final void setLocale(ULocale valid, ULocale actual) {
    if (((valid == null) ? true : false) != ((actual == null) ? true : false))
      throw new IllegalArgumentException(); 
    this.validLocale = valid;
    this.actualLocale = actual;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\BreakIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */