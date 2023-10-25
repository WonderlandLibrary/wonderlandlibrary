package com.ibm.icu.text;

import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.ULocale;

class LowercaseTransliterator extends Transliterator {
  static final String _ID = "Any-Lower";
  
  private ULocale locale;
  
  private UCaseProps csp;
  
  private ReplaceableContextIterator iter;
  
  private StringBuilder result;
  
  private int[] locCache;
  
  SourceTargetUtility sourceTargetUtility;
  
  static void register() {
    Transliterator.registerFactory("Any-Lower", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new LowercaseTransliterator(ULocale.US);
          }
        });
    Transliterator.registerSpecialInverse("Lower", "Upper", true);
  }
  
  public LowercaseTransliterator(ULocale loc) {
    super("Any-Lower", null);
    this.sourceTargetUtility = null;
    this.locale = loc;
    this.csp = UCaseProps.INSTANCE;
    this.iter = new ReplaceableContextIterator();
    this.result = new StringBuilder();
    this.locCache = new int[1];
    this.locCache[0] = 0;
  }
  
  protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental) {
    if (this.csp == null)
      return; 
    if (offsets.start >= offsets.limit)
      return; 
    this.iter.setText(text);
    this.result.setLength(0);
    this.iter.setIndex(offsets.start);
    this.iter.setLimit(offsets.limit);
    this.iter.setContextLimits(offsets.contextStart, offsets.contextLimit);
    int c;
    while ((c = this.iter.nextCaseMapCP()) >= 0) {
      int delta;
      c = this.csp.toFullLower(c, this.iter, this.result, this.locale, this.locCache);
      if (this.iter.didReachLimit() && isIncremental) {
        offsets.start = this.iter.getCaseMapCPStart();
        return;
      } 
      if (c < 0)
        continue; 
      if (c <= 31) {
        delta = this.iter.replace(this.result.toString());
        this.result.setLength(0);
      } else {
        delta = this.iter.replace(UTF16.valueOf(c));
      } 
      if (delta != 0) {
        offsets.limit += delta;
        offsets.contextLimit += delta;
      } 
    } 
    offsets.start = offsets.limit;
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    synchronized (this) {
      if (this.sourceTargetUtility == null)
        this.sourceTargetUtility = new SourceTargetUtility(new Transform<String, String>() {
              public String transform(String source) {
                return UCharacter.toLowerCase(LowercaseTransliterator.this.locale, source);
              }
            }); 
    } 
    this.sourceTargetUtility.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\LowercaseTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */