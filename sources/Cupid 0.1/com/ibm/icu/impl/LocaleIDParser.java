package com.ibm.icu.impl;

import com.ibm.icu.impl.locale.AsciiUtil;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public final class LocaleIDParser {
  private char[] id;
  
  private int index;
  
  private StringBuilder buffer;
  
  private boolean canonicalize;
  
  private boolean hadCountry;
  
  Map<String, String> keywords;
  
  String baseName;
  
  private static final char KEYWORD_SEPARATOR = '@';
  
  private static final char HYPHEN = '-';
  
  private static final char KEYWORD_ASSIGN = '=';
  
  private static final char COMMA = ',';
  
  private static final char ITEM_SEPARATOR = ';';
  
  private static final char DOT = '.';
  
  private static final char UNDERSCORE = '_';
  
  private static final char DONE = '￿';
  
  public LocaleIDParser(String localeID) {
    this(localeID, false);
  }
  
  public LocaleIDParser(String localeID, boolean canonicalize) {
    this.id = localeID.toCharArray();
    this.index = 0;
    this.buffer = new StringBuilder(this.id.length + 5);
    this.canonicalize = canonicalize;
  }
  
  private void reset() {
    this.index = 0;
    this.buffer = new StringBuilder(this.id.length + 5);
  }
  
  private void append(char c) {
    this.buffer.append(c);
  }
  
  private void addSeparator() {
    append('_');
  }
  
  private String getString(int start) {
    return this.buffer.substring(start);
  }
  
  private void set(int pos, String s) {
    this.buffer.delete(pos, this.buffer.length());
    this.buffer.insert(pos, s);
  }
  
  private void append(String s) {
    this.buffer.append(s);
  }
  
  private char next() {
    if (this.index == this.id.length) {
      this.index++;
      return Character.MAX_VALUE;
    } 
    return this.id[this.index++];
  }
  
  private void skipUntilTerminatorOrIDSeparator() {
    while (!isTerminatorOrIDSeparator(next()));
    this.index--;
  }
  
  private boolean atTerminator() {
    return (this.index >= this.id.length || isTerminator(this.id[this.index]));
  }
  
  private boolean isTerminator(char c) {
    return (c == '@' || c == Character.MAX_VALUE || c == '.');
  }
  
  private boolean isTerminatorOrIDSeparator(char c) {
    return (c == '_' || c == '-' || isTerminator(c));
  }
  
  private boolean haveExperimentalLanguagePrefix() {
    if (this.id.length > 2) {
      char c = this.id[1];
      if (c == '-' || c == '_') {
        c = this.id[0];
        return (c == 'x' || c == 'X' || c == 'i' || c == 'I');
      } 
    } 
    return false;
  }
  
  private boolean haveKeywordAssign() {
    for (int i = this.index; i < this.id.length; i++) {
      if (this.id[i] == '=')
        return true; 
    } 
    return false;
  }
  
  private int parseLanguage() {
    int startLength = this.buffer.length();
    if (haveExperimentalLanguagePrefix()) {
      append(AsciiUtil.toLower(this.id[0]));
      append('-');
      this.index = 2;
    } 
    char c;
    while (!isTerminatorOrIDSeparator(c = next()))
      append(AsciiUtil.toLower(c)); 
    this.index--;
    if (this.buffer.length() - startLength == 3) {
      String lang = LocaleIDs.threeToTwoLetterLanguage(getString(0));
      if (lang != null)
        set(0, lang); 
    } 
    return 0;
  }
  
  private void skipLanguage() {
    if (haveExperimentalLanguagePrefix())
      this.index = 2; 
    skipUntilTerminatorOrIDSeparator();
  }
  
  private int parseScript() {
    if (!atTerminator()) {
      int oldIndex = this.index;
      this.index++;
      int oldBlen = this.buffer.length();
      boolean firstPass = true;
      char c;
      while (!isTerminatorOrIDSeparator(c = next()) && AsciiUtil.isAlpha(c)) {
        if (firstPass) {
          addSeparator();
          append(AsciiUtil.toUpper(c));
          firstPass = false;
          continue;
        } 
        append(AsciiUtil.toLower(c));
      } 
      this.index--;
      if (this.index - oldIndex != 5) {
        this.index = oldIndex;
        this.buffer.delete(oldBlen, this.buffer.length());
      } else {
        oldBlen++;
      } 
      return oldBlen;
    } 
    return this.buffer.length();
  }
  
  private void skipScript() {
    if (!atTerminator()) {
      int oldIndex = this.index;
      this.index++;
      char c;
      while (!isTerminatorOrIDSeparator(c = next()) && AsciiUtil.isAlpha(c));
      this.index--;
      if (this.index - oldIndex != 5)
        this.index = oldIndex; 
    } 
  }
  
  private int parseCountry() {
    if (!atTerminator()) {
      int oldIndex = this.index;
      this.index++;
      int oldBlen = this.buffer.length();
      boolean firstPass = true;
      char c;
      while (!isTerminatorOrIDSeparator(c = next())) {
        if (firstPass) {
          this.hadCountry = true;
          addSeparator();
          oldBlen++;
          firstPass = false;
        } 
        append(AsciiUtil.toUpper(c));
      } 
      this.index--;
      int charsAppended = this.buffer.length() - oldBlen;
      if (charsAppended != 0)
        if (charsAppended < 2 || charsAppended > 3) {
          this.index = oldIndex;
          oldBlen--;
          this.buffer.delete(oldBlen, this.buffer.length());
          this.hadCountry = false;
        } else if (charsAppended == 3) {
          String region = LocaleIDs.threeToTwoLetterRegion(getString(oldBlen));
          if (region != null)
            set(oldBlen, region); 
        }  
      return oldBlen;
    } 
    return this.buffer.length();
  }
  
  private void skipCountry() {
    if (!atTerminator()) {
      if (this.id[this.index] == '_' || this.id[this.index] == '-')
        this.index++; 
      int oldIndex = this.index;
      skipUntilTerminatorOrIDSeparator();
      int charsSkipped = this.index - oldIndex;
      if (charsSkipped < 2 || charsSkipped > 3)
        this.index = oldIndex; 
    } 
  }
  
  private int parseVariant() {
    int oldBlen = this.buffer.length();
    boolean start = true;
    boolean needSeparator = true;
    boolean skipping = false;
    boolean firstPass = true;
    char c;
    while ((c = next()) != Character.MAX_VALUE) {
      if (c == '.') {
        start = false;
        skipping = true;
        continue;
      } 
      if (c == '@') {
        if (haveKeywordAssign())
          break; 
        skipping = false;
        start = false;
        needSeparator = true;
        continue;
      } 
      if (start) {
        start = false;
        if (c != '_' && c != '-')
          this.index--; 
        continue;
      } 
      if (!skipping) {
        if (needSeparator) {
          needSeparator = false;
          if (firstPass && !this.hadCountry) {
            addSeparator();
            oldBlen++;
          } 
          addSeparator();
          if (firstPass) {
            oldBlen++;
            firstPass = false;
          } 
        } 
        c = AsciiUtil.toUpper(c);
        if (c == '-' || c == ',')
          c = '_'; 
        append(c);
      } 
    } 
    this.index--;
    return oldBlen;
  }
  
  public String getLanguage() {
    reset();
    return getString(parseLanguage());
  }
  
  public String getScript() {
    reset();
    skipLanguage();
    return getString(parseScript());
  }
  
  public String getCountry() {
    reset();
    skipLanguage();
    skipScript();
    return getString(parseCountry());
  }
  
  public String getVariant() {
    reset();
    skipLanguage();
    skipScript();
    skipCountry();
    return getString(parseVariant());
  }
  
  public String[] getLanguageScriptCountryVariant() {
    reset();
    return new String[] { getString(parseLanguage()), getString(parseScript()), getString(parseCountry()), getString(parseVariant()) };
  }
  
  public void setBaseName(String baseName) {
    this.baseName = baseName;
  }
  
  public void parseBaseName() {
    if (this.baseName != null) {
      set(0, this.baseName);
    } else {
      reset();
      parseLanguage();
      parseScript();
      parseCountry();
      parseVariant();
      int len = this.buffer.length();
      if (len > 0 && this.buffer.charAt(len - 1) == '_')
        this.buffer.deleteCharAt(len - 1); 
    } 
  }
  
  public String getBaseName() {
    if (this.baseName != null)
      return this.baseName; 
    parseBaseName();
    return getString(0);
  }
  
  public String getName() {
    parseBaseName();
    parseKeywords();
    return getString(0);
  }
  
  private boolean setToKeywordStart() {
    for (int i = this.index; i < this.id.length; i++) {
      if (this.id[i] == '@') {
        if (this.canonicalize) {
          for (int j = ++i; j < this.id.length; j++) {
            if (this.id[j] == '=') {
              this.index = i;
              return true;
            } 
          } 
          break;
        } 
        if (++i < this.id.length) {
          this.index = i;
          return true;
        } 
        break;
      } 
    } 
    return false;
  }
  
  private static boolean isDoneOrKeywordAssign(char c) {
    return (c == Character.MAX_VALUE || c == '=');
  }
  
  private static boolean isDoneOrItemSeparator(char c) {
    return (c == Character.MAX_VALUE || c == ';');
  }
  
  private String getKeyword() {
    int start = this.index;
    while (!isDoneOrKeywordAssign(next()));
    this.index--;
    return AsciiUtil.toLowerString((new String(this.id, start, this.index - start)).trim());
  }
  
  private String getValue() {
    int start = this.index;
    while (!isDoneOrItemSeparator(next()));
    this.index--;
    return (new String(this.id, start, this.index - start)).trim();
  }
  
  private Comparator<String> getKeyComparator() {
    Comparator<String> comp = new Comparator<String>() {
        public int compare(String lhs, String rhs) {
          return lhs.compareTo(rhs);
        }
      };
    return comp;
  }
  
  public Map<String, String> getKeywordMap() {
    if (this.keywords == null) {
      TreeMap<String, String> m = null;
      if (setToKeywordStart())
        label26: do {
          String key = getKeyword();
          if (key.length() == 0)
            break label26; 
          char c = next();
          if (c != '=') {
            if (c == Character.MAX_VALUE)
              break label26; 
            continue;
          } 
          String value = getValue();
          if (value.length() == 0)
            continue; 
          if (m == null) {
            m = new TreeMap<String, String>(getKeyComparator());
          } else if (m.containsKey(key)) {
            continue;
          } 
          m.put(key, value);
        } while (next() == ';'); 
      this.keywords = (m != null) ? m : Collections.<String, String>emptyMap();
    } 
    return this.keywords;
  }
  
  private int parseKeywords() {
    int oldBlen = this.buffer.length();
    Map<String, String> m = getKeywordMap();
    if (!m.isEmpty()) {
      boolean first = true;
      for (Map.Entry<String, String> e : m.entrySet()) {
        append(first ? 64 : 59);
        first = false;
        append(e.getKey());
        append('=');
        append(e.getValue());
      } 
      if (!first)
        oldBlen++; 
    } 
    return oldBlen;
  }
  
  public Iterator<String> getKeywords() {
    Map<String, String> m = getKeywordMap();
    return m.isEmpty() ? null : m.keySet().iterator();
  }
  
  public String getKeywordValue(String keywordName) {
    Map<String, String> m = getKeywordMap();
    return m.isEmpty() ? null : m.get(AsciiUtil.toLowerString(keywordName.trim()));
  }
  
  public void defaultKeywordValue(String keywordName, String value) {
    setKeywordValue(keywordName, value, false);
  }
  
  public void setKeywordValue(String keywordName, String value) {
    setKeywordValue(keywordName, value, true);
  }
  
  private void setKeywordValue(String keywordName, String value, boolean reset) {
    if (keywordName == null) {
      if (reset)
        this.keywords = Collections.emptyMap(); 
    } else {
      keywordName = AsciiUtil.toLowerString(keywordName.trim());
      if (keywordName.length() == 0)
        throw new IllegalArgumentException("keyword must not be empty"); 
      if (value != null) {
        value = value.trim();
        if (value.length() == 0)
          throw new IllegalArgumentException("value must not be empty"); 
      } 
      Map<String, String> m = getKeywordMap();
      if (m.isEmpty()) {
        if (value != null) {
          this.keywords = new TreeMap<String, String>(getKeyComparator());
          this.keywords.put(keywordName, value.trim());
        } 
      } else if (reset || !m.containsKey(keywordName)) {
        if (value != null) {
          m.put(keywordName, value);
        } else {
          m.remove(keywordName);
          if (m.isEmpty())
            this.keywords = Collections.emptyMap(); 
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\LocaleIDParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */