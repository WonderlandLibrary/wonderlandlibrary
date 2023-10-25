package com.ibm.icu.text;

class RemoveTransliterator extends Transliterator {
  private static String _ID = "Any-Remove";
  
  static void register() {
    Transliterator.registerFactory(_ID, new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new RemoveTransliterator();
          }
        });
    Transliterator.registerSpecialInverse("Remove", "Null", false);
  }
  
  public RemoveTransliterator() {
    super(_ID, null);
  }
  
  protected void handleTransliterate(Replaceable text, Transliterator.Position index, boolean incremental) {
    text.replace(index.start, index.limit, "");
    int len = index.limit - index.start;
    index.contextLimit -= len;
    index.limit -= len;
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
    sourceSet.addAll(myFilter);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RemoveTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */