package com.ibm.icu.text;

class NullTransliterator extends Transliterator {
  static String SHORT_ID = "Null";
  
  static String _ID = "Any-Null";
  
  public NullTransliterator() {
    super(_ID, null);
  }
  
  protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean incremental) {
    offsets.start = offsets.limit;
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\NullTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */