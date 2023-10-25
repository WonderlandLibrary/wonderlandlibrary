package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import java.util.HashMap;
import java.util.Map;

final class NormalizationTransliterator extends Transliterator {
  private final Normalizer2 norm2;
  
  static void register() {
    Transliterator.registerFactory("Any-NFC", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new NormalizationTransliterator("NFC", (Normalizer2)(Norm2AllModes.getNFCInstance()).comp);
          }
        });
    Transliterator.registerFactory("Any-NFD", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new NormalizationTransliterator("NFD", (Normalizer2)(Norm2AllModes.getNFCInstance()).decomp);
          }
        });
    Transliterator.registerFactory("Any-NFKC", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new NormalizationTransliterator("NFKC", (Normalizer2)(Norm2AllModes.getNFKCInstance()).comp);
          }
        });
    Transliterator.registerFactory("Any-NFKD", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new NormalizationTransliterator("NFKD", (Normalizer2)(Norm2AllModes.getNFKCInstance()).decomp);
          }
        });
    Transliterator.registerFactory("Any-FCD", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new NormalizationTransliterator("FCD", Norm2AllModes.getFCDNormalizer2());
          }
        });
    Transliterator.registerFactory("Any-FCC", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new NormalizationTransliterator("FCC", (Normalizer2)(Norm2AllModes.getNFCInstance()).fcc);
          }
        });
    Transliterator.registerSpecialInverse("NFC", "NFD", true);
    Transliterator.registerSpecialInverse("NFKC", "NFKD", true);
    Transliterator.registerSpecialInverse("FCC", "NFD", false);
    Transliterator.registerSpecialInverse("FCD", "FCD", false);
  }
  
  private NormalizationTransliterator(String id, Normalizer2 n2) {
    super(id, null);
    this.norm2 = n2;
  }
  
  protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental) {
    int start = offsets.start;
    int limit = offsets.limit;
    if (start >= limit)
      return; 
    StringBuilder segment = new StringBuilder();
    StringBuilder normalized = new StringBuilder();
    int c = text.char32At(start);
    do {
      int prev = start;
      segment.setLength(0);
      do {
        segment.appendCodePoint(c);
        start += Character.charCount(c);
      } while (start < limit && !this.norm2.hasBoundaryBefore(c = text.char32At(start)));
      if (start == limit && isIncremental && !this.norm2.hasBoundaryAfter(c)) {
        start = prev;
        break;
      } 
      this.norm2.normalize(segment, normalized);
      if (Normalizer2Impl.UTF16Plus.equal(segment, normalized))
        continue; 
      text.replace(prev, start, normalized.toString());
      int delta = normalized.length() - start - prev;
      start += delta;
      limit += delta;
    } while (start < limit);
    offsets.start = start;
    offsets.contextLimit += limit - offsets.limit;
    offsets.limit = limit;
  }
  
  static final Map<Normalizer2, SourceTargetUtility> SOURCE_CACHE = new HashMap<Normalizer2, SourceTargetUtility>();
  
  static class NormalizingTransform implements Transform<String, String> {
    final Normalizer2 norm2;
    
    public NormalizingTransform(Normalizer2 norm2) {
      this.norm2 = norm2;
    }
    
    public String transform(String source) {
      return this.norm2.normalize(source);
    }
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    SourceTargetUtility cache;
    synchronized (SOURCE_CACHE) {
      cache = SOURCE_CACHE.get(this.norm2);
      if (cache == null) {
        cache = new SourceTargetUtility(new NormalizingTransform(this.norm2), this.norm2);
        SOURCE_CACHE.put(this.norm2, cache);
      } 
    } 
    cache.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\NormalizationTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */