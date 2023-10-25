package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;

class EscapeTransliterator extends Transliterator {
  private String prefix;
  
  private String suffix;
  
  private int radix;
  
  private int minDigits;
  
  private boolean grokSupplementals;
  
  private EscapeTransliterator supplementalHandler;
  
  static void register() {
    Transliterator.registerFactory("Any-Hex/Unicode", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new EscapeTransliterator("Any-Hex/Unicode", "U+", "", 16, 4, true, null);
          }
        });
    Transliterator.registerFactory("Any-Hex/Java", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new EscapeTransliterator("Any-Hex/Java", "\\u", "", 16, 4, false, null);
          }
        });
    Transliterator.registerFactory("Any-Hex/C", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new EscapeTransliterator("Any-Hex/C", "\\u", "", 16, 4, true, new EscapeTransliterator("", "\\U", "", 16, 8, true, null));
          }
        });
    Transliterator.registerFactory("Any-Hex/XML", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new EscapeTransliterator("Any-Hex/XML", "&#x", ";", 16, 1, true, null);
          }
        });
    Transliterator.registerFactory("Any-Hex/XML10", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new EscapeTransliterator("Any-Hex/XML10", "&#", ";", 10, 1, true, null);
          }
        });
    Transliterator.registerFactory("Any-Hex/Perl", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new EscapeTransliterator("Any-Hex/Perl", "\\x{", "}", 16, 1, true, null);
          }
        });
    Transliterator.registerFactory("Any-Hex/Plain", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new EscapeTransliterator("Any-Hex/Plain", "", "", 16, 4, true, null);
          }
        });
    Transliterator.registerFactory("Any-Hex", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new EscapeTransliterator("Any-Hex", "\\u", "", 16, 4, false, null);
          }
        });
  }
  
  EscapeTransliterator(String ID, String prefix, String suffix, int radix, int minDigits, boolean grokSupplementals, EscapeTransliterator supplementalHandler) {
    super(ID, null);
    this.prefix = prefix;
    this.suffix = suffix;
    this.radix = radix;
    this.minDigits = minDigits;
    this.grokSupplementals = grokSupplementals;
    this.supplementalHandler = supplementalHandler;
  }
  
  protected void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean incremental) {
    int start = pos.start;
    int limit = pos.limit;
    StringBuilder buf = new StringBuilder(this.prefix);
    int prefixLen = this.prefix.length();
    boolean redoPrefix = false;
    while (start < limit) {
      int c = this.grokSupplementals ? text.char32At(start) : text.charAt(start);
      int charLen = this.grokSupplementals ? UTF16.getCharCount(c) : 1;
      if ((c & 0xFFFF0000) != 0 && this.supplementalHandler != null) {
        buf.setLength(0);
        buf.append(this.supplementalHandler.prefix);
        Utility.appendNumber(buf, c, this.supplementalHandler.radix, this.supplementalHandler.minDigits);
        buf.append(this.supplementalHandler.suffix);
        redoPrefix = true;
      } else {
        if (redoPrefix) {
          buf.setLength(0);
          buf.append(this.prefix);
          redoPrefix = false;
        } else {
          buf.setLength(prefixLen);
        } 
        Utility.appendNumber(buf, c, this.radix, this.minDigits);
        buf.append(this.suffix);
      } 
      text.replace(start, start + charLen, buf.toString());
      start += buf.length();
      limit += buf.length() - charLen;
    } 
    pos.contextLimit += limit - pos.limit;
    pos.limit = limit;
    pos.start = start;
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    sourceSet.addAll(getFilterAsUnicodeSet(inputFilter));
    for (EscapeTransliterator it = this; it != null; it = it.supplementalHandler) {
      if (inputFilter.size() != 0) {
        targetSet.addAll(it.prefix);
        targetSet.addAll(it.suffix);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < it.radix; i++)
          Utility.appendNumber(buffer, i, it.radix, it.minDigits); 
        targetSet.addAll(buffer.toString());
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\EscapeTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */