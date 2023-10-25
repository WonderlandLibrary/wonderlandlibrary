package com.ibm.icu.text;

import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.lang.UCharacter;

class CaseFoldTransliterator extends Transliterator {
  static final String _ID = "Any-CaseFold";
  
  private UCaseProps csp;
  
  private ReplaceableContextIterator iter;
  
  private StringBuilder result;
  
  static void register() {
    Transliterator.registerFactory("Any-CaseFold", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new CaseFoldTransliterator();
          }
        });
    Transliterator.registerSpecialInverse("CaseFold", "Upper", false);
  }
  
  public CaseFoldTransliterator() {
    super("Any-CaseFold", null);
    this.csp = UCaseProps.INSTANCE;
    this.iter = new ReplaceableContextIterator();
    this.result = new StringBuilder();
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
      c = this.csp.toFullFolding(c, this.result, 0);
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
  
  static SourceTargetUtility sourceTargetUtility = null;
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    synchronized (UppercaseTransliterator.class) {
      if (sourceTargetUtility == null)
        sourceTargetUtility = new SourceTargetUtility(new Transform<String, String>() {
              public String transform(String source) {
                return UCharacter.foldCase(source, true);
              }
            }); 
    } 
    sourceTargetUtility.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CaseFoldTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */