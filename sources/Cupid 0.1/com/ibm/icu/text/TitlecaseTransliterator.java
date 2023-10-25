package com.ibm.icu.text;

import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.ULocale;

class TitlecaseTransliterator extends Transliterator {
  static final String _ID = "Any-Title";
  
  private ULocale locale;
  
  private UCaseProps csp;
  
  private ReplaceableContextIterator iter;
  
  private StringBuilder result;
  
  private int[] locCache;
  
  SourceTargetUtility sourceTargetUtility;
  
  static void register() {
    Transliterator.registerFactory("Any-Title", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new TitlecaseTransliterator(ULocale.US);
          }
        });
    registerSpecialInverse("Title", "Lower", false);
  }
  
  public TitlecaseTransliterator(ULocale loc) {
    super("Any-Title", null);
    this.sourceTargetUtility = null;
    this.locale = loc;
    setMaximumContextLength(2);
    this.csp = UCaseProps.INSTANCE;
    this.iter = new ReplaceableContextIterator();
    this.result = new StringBuilder();
    this.locCache = new int[1];
    this.locCache[0] = 0;
  }
  
  protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental) {
    if (offsets.start >= offsets.limit)
      return; 
    boolean doTitle = true;
    int start;
    for (start = offsets.start - 1; start >= offsets.contextStart; start -= UTF16.getCharCount(i)) {
      int i = text.char32At(start);
      int type = this.csp.getTypeOrIgnorable(i);
      if (type > 0) {
        doTitle = false;
        break;
      } 
      if (type == 0)
        break; 
    } 
    this.iter.setText(text);
    this.iter.setIndex(offsets.start);
    this.iter.setLimit(offsets.limit);
    this.iter.setContextLimits(offsets.contextStart, offsets.contextLimit);
    this.result.setLength(0);
    int c;
    while ((c = this.iter.nextCaseMapCP()) >= 0) {
      int type = this.csp.getTypeOrIgnorable(c);
      if (type >= 0) {
        int delta;
        if (doTitle) {
          c = this.csp.toFullTitle(c, this.iter, this.result, this.locale, this.locCache);
        } else {
          c = this.csp.toFullLower(c, this.iter, this.result, this.locale, this.locCache);
        } 
        doTitle = (type == 0);
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
    } 
    offsets.start = offsets.limit;
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    synchronized (this) {
      if (this.sourceTargetUtility == null)
        this.sourceTargetUtility = new SourceTargetUtility(new Transform<String, String>() {
              public String transform(String source) {
                return UCharacter.toTitleCase(TitlecaseTransliterator.this.locale, source, null);
              }
            }); 
    } 
    this.sourceTargetUtility.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\TitlecaseTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */