package com.ibm.icu.impl.locale;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class UnicodeLocaleExtension extends Extension {
  public static final char SINGLETON = 'u';
  
  private static final SortedSet<String> EMPTY_SORTED_SET = new TreeSet<String>();
  
  private static final SortedMap<String, String> EMPTY_SORTED_MAP = new TreeMap<String, String>();
  
  private SortedSet<String> _attributes = EMPTY_SORTED_SET;
  
  private SortedMap<String, String> _keywords = EMPTY_SORTED_MAP;
  
  public static final UnicodeLocaleExtension CA_JAPANESE = new UnicodeLocaleExtension();
  
  public static final UnicodeLocaleExtension NU_THAI;
  
  static {
    CA_JAPANESE._keywords = new TreeMap<String, String>();
    CA_JAPANESE._keywords.put("ca", "japanese");
    CA_JAPANESE._value = "ca-japanese";
    NU_THAI = new UnicodeLocaleExtension();
    NU_THAI._keywords = new TreeMap<String, String>();
    NU_THAI._keywords.put("nu", "thai");
    NU_THAI._value = "nu-thai";
  }
  
  private UnicodeLocaleExtension() {
    super('u');
  }
  
  UnicodeLocaleExtension(SortedSet<String> attributes, SortedMap<String, String> keywords) {
    this();
    if (attributes != null && attributes.size() > 0)
      this._attributes = attributes; 
    if (keywords != null && keywords.size() > 0)
      this._keywords = keywords; 
    if (this._attributes.size() > 0 || this._keywords.size() > 0) {
      StringBuilder sb = new StringBuilder();
      for (String attribute : this._attributes)
        sb.append("-").append(attribute); 
      for (Map.Entry<String, String> keyword : this._keywords.entrySet()) {
        String key = keyword.getKey();
        String value = keyword.getValue();
        sb.append("-").append(key);
        if (value.length() > 0)
          sb.append("-").append(value); 
      } 
      this._value = sb.substring(1);
    } 
  }
  
  public Set<String> getUnicodeLocaleAttributes() {
    return Collections.unmodifiableSet(this._attributes);
  }
  
  public Set<String> getUnicodeLocaleKeys() {
    return Collections.unmodifiableSet(this._keywords.keySet());
  }
  
  public String getUnicodeLocaleType(String unicodeLocaleKey) {
    return this._keywords.get(unicodeLocaleKey);
  }
  
  public static boolean isSingletonChar(char c) {
    return ('u' == AsciiUtil.toLower(c));
  }
  
  public static boolean isAttribute(String s) {
    return (s.length() >= 3 && s.length() <= 8 && AsciiUtil.isAlphaNumericString(s));
  }
  
  public static boolean isKey(String s) {
    return (s.length() == 2 && AsciiUtil.isAlphaNumericString(s));
  }
  
  public static boolean isTypeSubtag(String s) {
    return (s.length() >= 3 && s.length() <= 8 && AsciiUtil.isAlphaNumericString(s));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\locale\UnicodeLocaleExtension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */