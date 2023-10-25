package com.ibm.icu.impl.locale;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class LocaleExtensions {
  private SortedMap<Character, Extension> _map;
  
  private String _id;
  
  private static final SortedMap<Character, Extension> EMPTY_MAP = Collections.unmodifiableSortedMap(new TreeMap<Character, Extension>());
  
  public static final LocaleExtensions EMPTY_EXTENSIONS = new LocaleExtensions();
  
  public static final LocaleExtensions CALENDAR_JAPANESE;
  
  public static final LocaleExtensions NUMBER_THAI;
  
  static {
    EMPTY_EXTENSIONS._id = "";
    EMPTY_EXTENSIONS._map = EMPTY_MAP;
    CALENDAR_JAPANESE = new LocaleExtensions();
    CALENDAR_JAPANESE._id = "u-ca-japanese";
    CALENDAR_JAPANESE._map = new TreeMap<Character, Extension>();
    CALENDAR_JAPANESE._map.put(Character.valueOf('u'), UnicodeLocaleExtension.CA_JAPANESE);
    NUMBER_THAI = new LocaleExtensions();
    NUMBER_THAI._id = "u-nu-thai";
    NUMBER_THAI._map = new TreeMap<Character, Extension>();
    NUMBER_THAI._map.put(Character.valueOf('u'), UnicodeLocaleExtension.NU_THAI);
  }
  
  private LocaleExtensions() {}
  
  LocaleExtensions(Map<InternalLocaleBuilder.CaseInsensitiveChar, String> extensions, Set<InternalLocaleBuilder.CaseInsensitiveString> uattributes, Map<InternalLocaleBuilder.CaseInsensitiveString, String> ukeywords) {
    boolean hasExtension = (extensions != null && extensions.size() > 0);
    boolean hasUAttributes = (uattributes != null && uattributes.size() > 0);
    boolean hasUKeywords = (ukeywords != null && ukeywords.size() > 0);
    if (!hasExtension && !hasUAttributes && !hasUKeywords) {
      this._map = EMPTY_MAP;
      this._id = "";
      return;
    } 
    this._map = new TreeMap<Character, Extension>();
    if (hasExtension)
      for (Map.Entry<InternalLocaleBuilder.CaseInsensitiveChar, String> ext : extensions.entrySet()) {
        char key = AsciiUtil.toLower(((InternalLocaleBuilder.CaseInsensitiveChar)ext.getKey()).value());
        String value = ext.getValue();
        if (LanguageTag.isPrivateusePrefixChar(key)) {
          value = InternalLocaleBuilder.removePrivateuseVariant(value);
          if (value == null)
            continue; 
        } 
        Extension e = new Extension(key, AsciiUtil.toLowerString(value));
        this._map.put(Character.valueOf(key), e);
      }  
    if (hasUAttributes || hasUKeywords) {
      TreeSet<String> uaset = null;
      TreeMap<String, String> ukmap = null;
      if (hasUAttributes) {
        uaset = new TreeSet<String>();
        for (InternalLocaleBuilder.CaseInsensitiveString cis : uattributes)
          uaset.add(AsciiUtil.toLowerString(cis.value())); 
      } 
      if (hasUKeywords) {
        ukmap = new TreeMap<String, String>();
        for (Map.Entry<InternalLocaleBuilder.CaseInsensitiveString, String> kwd : ukeywords.entrySet()) {
          String key = AsciiUtil.toLowerString(((InternalLocaleBuilder.CaseInsensitiveString)kwd.getKey()).value());
          String type = AsciiUtil.toLowerString(kwd.getValue());
          ukmap.put(key, type);
        } 
      } 
      UnicodeLocaleExtension ule = new UnicodeLocaleExtension(uaset, ukmap);
      this._map.put(Character.valueOf('u'), ule);
    } 
    if (this._map.size() == 0) {
      this._map = EMPTY_MAP;
      this._id = "";
    } else {
      this._id = toID(this._map);
    } 
  }
  
  public Set<Character> getKeys() {
    return Collections.unmodifiableSet(this._map.keySet());
  }
  
  public Extension getExtension(Character key) {
    return this._map.get(Character.valueOf(AsciiUtil.toLower(key.charValue())));
  }
  
  public String getExtensionValue(Character key) {
    Extension ext = this._map.get(Character.valueOf(AsciiUtil.toLower(key.charValue())));
    if (ext == null)
      return null; 
    return ext.getValue();
  }
  
  public Set<String> getUnicodeLocaleAttributes() {
    Extension ext = this._map.get(Character.valueOf('u'));
    if (ext == null)
      return Collections.emptySet(); 
    assert ext instanceof UnicodeLocaleExtension;
    return ((UnicodeLocaleExtension)ext).getUnicodeLocaleAttributes();
  }
  
  public Set<String> getUnicodeLocaleKeys() {
    Extension ext = this._map.get(Character.valueOf('u'));
    if (ext == null)
      return Collections.emptySet(); 
    assert ext instanceof UnicodeLocaleExtension;
    return ((UnicodeLocaleExtension)ext).getUnicodeLocaleKeys();
  }
  
  public String getUnicodeLocaleType(String unicodeLocaleKey) {
    Extension ext = this._map.get(Character.valueOf('u'));
    if (ext == null)
      return null; 
    assert ext instanceof UnicodeLocaleExtension;
    return ((UnicodeLocaleExtension)ext).getUnicodeLocaleType(AsciiUtil.toLowerString(unicodeLocaleKey));
  }
  
  public boolean isEmpty() {
    return this._map.isEmpty();
  }
  
  public static boolean isValidKey(char c) {
    return (LanguageTag.isExtensionSingletonChar(c) || LanguageTag.isPrivateusePrefixChar(c));
  }
  
  public static boolean isValidUnicodeLocaleKey(String ukey) {
    return UnicodeLocaleExtension.isKey(ukey);
  }
  
  private static String toID(SortedMap<Character, Extension> map) {
    StringBuilder buf = new StringBuilder();
    Extension privuse = null;
    for (Map.Entry<Character, Extension> entry : map.entrySet()) {
      char singleton = ((Character)entry.getKey()).charValue();
      Extension extension = entry.getValue();
      if (LanguageTag.isPrivateusePrefixChar(singleton)) {
        privuse = extension;
        continue;
      } 
      if (buf.length() > 0)
        buf.append("-"); 
      buf.append(extension);
    } 
    if (privuse != null) {
      if (buf.length() > 0)
        buf.append("-"); 
      buf.append(privuse);
    } 
    return buf.toString();
  }
  
  public String toString() {
    return this._id;
  }
  
  public String getID() {
    return this._id;
  }
  
  public int hashCode() {
    return this._id.hashCode();
  }
  
  public boolean equals(Object other) {
    if (this == other)
      return true; 
    if (!(other instanceof LocaleExtensions))
      return false; 
    return this._id.equals(((LocaleExtensions)other)._id);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\locale\LocaleExtensions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */