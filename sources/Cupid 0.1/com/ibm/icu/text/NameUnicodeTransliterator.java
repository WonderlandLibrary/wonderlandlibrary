package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.UCharacterName;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;

class NameUnicodeTransliterator extends Transliterator {
  static final String _ID = "Name-Any";
  
  static final String OPEN_PAT = "\\N~{~";
  
  static final char OPEN_DELIM = '\\';
  
  static final char CLOSE_DELIM = '}';
  
  static final char SPACE = ' ';
  
  static void register() {
    Transliterator.registerFactory("Name-Any", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new NameUnicodeTransliterator(null);
          }
        });
  }
  
  public NameUnicodeTransliterator(UnicodeFilter filter) {
    super("Name-Any", filter);
  }
  
  protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental) {
    int maxLen = UCharacterName.INSTANCE.getMaxCharNameLength() + 1;
    StringBuffer name = new StringBuffer(maxLen);
    UnicodeSet legal = new UnicodeSet();
    UCharacterName.INSTANCE.getCharNameCharacters(legal);
    int cursor = offsets.start;
    int limit = offsets.limit;
    int mode = 0;
    int openPos = -1;
    while (cursor < limit) {
      int c = text.char32At(cursor);
      switch (mode) {
        case 0:
          if (c == 92) {
            openPos = cursor;
            int i = Utility.parsePattern("\\N~{~", text, cursor, limit);
            if (i >= 0 && i < limit) {
              mode = 1;
              name.setLength(0);
              cursor = i;
              continue;
            } 
          } 
          break;
        case 1:
          if (PatternProps.isWhiteSpace(c)) {
            if (name.length() > 0 && name.charAt(name.length() - 1) != ' ') {
              name.append(' ');
              if (name.length() > maxLen)
                mode = 0; 
            } 
            break;
          } 
          if (c == 125) {
            int len = name.length();
            if (len > 0 && name.charAt(len - 1) == ' ')
              name.setLength(--len); 
            c = UCharacter.getCharFromExtendedName(name.toString());
            if (c != -1) {
              cursor++;
              String str = UTF16.valueOf(c);
              text.replace(openPos, cursor, str);
              int delta = cursor - openPos - str.length();
              cursor -= delta;
              limit -= delta;
            } 
            mode = 0;
            openPos = -1;
            continue;
          } 
          if (legal.contains(c)) {
            UTF16.append(name, c);
            if (name.length() >= maxLen)
              mode = 0; 
            break;
          } 
          cursor--;
          mode = 0;
          break;
      } 
      cursor += UTF16.getCharCount(c);
    } 
    offsets.contextLimit += limit - offsets.limit;
    offsets.limit = limit;
    offsets.start = (isIncremental && openPos >= 0) ? openPos : cursor;
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
    if (!myFilter.containsAll("\\N{") || !myFilter.contains(125))
      return; 
    UnicodeSet items = (new UnicodeSet()).addAll(48, 57).addAll(65, 70).addAll(97, 122).add(60).add(62).add(40).add(41).add(45).add(32).addAll("\\N{").add(125);
    items.retainAll(myFilter);
    if (items.size() > 0) {
      sourceSet.addAll(items);
      targetSet.addAll(0, 1114111);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\NameUnicodeTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */