package com.ibm.icu.text;

class FunctionReplacer implements UnicodeReplacer {
  private Transliterator translit;
  
  private UnicodeReplacer replacer;
  
  public FunctionReplacer(Transliterator theTranslit, UnicodeReplacer theReplacer) {
    this.translit = theTranslit;
    this.replacer = theReplacer;
  }
  
  public int replace(Replaceable text, int start, int limit, int[] cursor) {
    int len = this.replacer.replace(text, start, limit, cursor);
    limit = start + len;
    limit = this.translit.transliterate(text, start, limit);
    return limit - start;
  }
  
  public String toReplacerPattern(boolean escapeUnprintable) {
    StringBuilder rule = new StringBuilder("&");
    rule.append(this.translit.getID());
    rule.append("( ");
    rule.append(this.replacer.toReplacerPattern(escapeUnprintable));
    rule.append(" )");
    return rule.toString();
  }
  
  public void addReplacementSetTo(UnicodeSet toUnionTo) {
    toUnionTo.addAll(this.translit.getTargetSet());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\FunctionReplacer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */