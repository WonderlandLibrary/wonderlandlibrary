package com.ibm.icu.text;

import com.ibm.icu.util.ULocale;
import java.util.HashMap;
import java.util.Map;

public class RbnfScannerProviderImpl implements RbnfLenientScannerProvider {
  private Map<String, RbnfLenientScanner> cache = new HashMap<String, RbnfLenientScanner>();
  
  public RbnfLenientScanner get(ULocale locale, String extras) {
    RbnfLenientScanner result = null;
    String key = locale.toString() + "/" + extras;
    synchronized (this.cache) {
      result = this.cache.get(key);
      if (result != null)
        return result; 
    } 
    result = createScanner(locale, extras);
    synchronized (this.cache) {
      this.cache.put(key, result);
    } 
    return result;
  }
  
  protected RbnfLenientScanner createScanner(ULocale locale, String extras) {
    RuleBasedCollator collator = null;
    try {
      collator = (RuleBasedCollator)Collator.getInstance(locale.toLocale());
      if (extras != null) {
        String rules = collator.getRules() + extras;
        collator = new RuleBasedCollator(rules);
      } 
      collator.setDecomposition(17);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("++++");
      collator = null;
    } 
    return new RbnfLenientScannerImpl(collator);
  }
  
  private static class RbnfLenientScannerImpl implements RbnfLenientScanner {
    private final RuleBasedCollator collator;
    
    private RbnfLenientScannerImpl(RuleBasedCollator rbc) {
      this.collator = rbc;
    }
    
    public boolean allIgnorable(String s) {
      CollationElementIterator iter = this.collator.getCollationElementIterator(s);
      int o = iter.next();
      while (o != -1 && CollationElementIterator.primaryOrder(o) == 0)
        o = iter.next(); 
      return (o == -1);
    }
    
    public int[] findText(String str, String key, int startingAt) {
      int p = startingAt;
      int keyLen = 0;
      while (p < str.length() && keyLen == 0) {
        keyLen = prefixLength(str.substring(p), key);
        if (keyLen != 0)
          return new int[] { p, keyLen }; 
        p++;
      } 
      return new int[] { -1, 0 };
    }
    
    public int[] findText2(String str, String key, int startingAt) {
      CollationElementIterator strIter = this.collator.getCollationElementIterator(str);
      CollationElementIterator keyIter = this.collator.getCollationElementIterator(key);
      int keyStart = -1;
      strIter.setOffset(startingAt);
      int oStr = strIter.next();
      int oKey = keyIter.next();
      while (oKey != -1) {
        while (oStr != -1 && CollationElementIterator.primaryOrder(oStr) == 0)
          oStr = strIter.next(); 
        while (oKey != -1 && CollationElementIterator.primaryOrder(oKey) == 0)
          oKey = keyIter.next(); 
        if (oStr == -1)
          return new int[] { -1, 0 }; 
        if (oKey == -1)
          break; 
        if (CollationElementIterator.primaryOrder(oStr) == CollationElementIterator.primaryOrder(oKey)) {
          keyStart = strIter.getOffset();
          oStr = strIter.next();
          oKey = keyIter.next();
          continue;
        } 
        if (keyStart != -1) {
          keyStart = -1;
          keyIter.reset();
          continue;
        } 
        oStr = strIter.next();
      } 
      if (oKey == -1)
        return new int[] { keyStart, strIter.getOffset() - keyStart }; 
      return new int[] { -1, 0 };
    }
    
    public int prefixLength(String str, String prefix) {
      CollationElementIterator strIter = this.collator.getCollationElementIterator(str);
      CollationElementIterator prefixIter = this.collator.getCollationElementIterator(prefix);
      int oStr = strIter.next();
      int oPrefix = prefixIter.next();
      while (oPrefix != -1) {
        while (CollationElementIterator.primaryOrder(oStr) == 0 && oStr != -1)
          oStr = strIter.next(); 
        while (CollationElementIterator.primaryOrder(oPrefix) == 0 && oPrefix != -1)
          oPrefix = prefixIter.next(); 
        if (oPrefix == -1)
          break; 
        if (oStr == -1)
          return 0; 
        if (CollationElementIterator.primaryOrder(oStr) != CollationElementIterator.primaryOrder(oPrefix))
          return 0; 
        oStr = strIter.next();
        oPrefix = prefixIter.next();
      } 
      int result = strIter.getOffset();
      if (oStr != -1)
        result--; 
      return result;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RbnfScannerProviderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */