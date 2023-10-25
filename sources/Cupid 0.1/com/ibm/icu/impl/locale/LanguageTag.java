package com.ibm.icu.impl.locale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LanguageTag {
  private static final boolean JDKIMPL = false;
  
  public static final String SEP = "-";
  
  public static final String PRIVATEUSE = "x";
  
  public static String UNDETERMINED = "und";
  
  public static final String PRIVUSE_VARIANT_PREFIX = "lvariant";
  
  private String _language = "";
  
  private String _script = "";
  
  private String _region = "";
  
  private String _privateuse = "";
  
  private List<String> _extlangs = Collections.emptyList();
  
  private List<String> _variants = Collections.emptyList();
  
  private List<String> _extensions = Collections.emptyList();
  
  private static final Map<AsciiUtil.CaseInsensitiveKey, String[]> GRANDFATHERED = (Map)new HashMap<AsciiUtil.CaseInsensitiveKey, String>();
  
  static {
    String[][] entries = { 
        { "art-lojban", "jbo" }, { "cel-gaulish", "xtg-x-cel-gaulish" }, { "en-GB-oed", "en-GB-x-oed" }, { "i-ami", "ami" }, { "i-bnn", "bnn" }, { "i-default", "en-x-i-default" }, { "i-enochian", "und-x-i-enochian" }, { "i-hak", "hak" }, { "i-klingon", "tlh" }, { "i-lux", "lb" }, 
        { "i-mingo", "see-x-i-mingo" }, { "i-navajo", "nv" }, { "i-pwn", "pwn" }, { "i-tao", "tao" }, { "i-tay", "tay" }, { "i-tsu", "tsu" }, { "no-bok", "nb" }, { "no-nyn", "nn" }, { "sgn-BE-FR", "sfb" }, { "sgn-BE-NL", "vgt" }, 
        { "sgn-CH-DE", "sgg" }, { "zh-guoyu", "cmn" }, { "zh-hakka", "hak" }, { "zh-min", "nan-x-zh-min" }, { "zh-min-nan", "nan" }, { "zh-xiang", "hsn" } };
    for (String[] e : entries)
      GRANDFATHERED.put(new AsciiUtil.CaseInsensitiveKey(e[0]), e); 
  }
  
  public static LanguageTag parse(String languageTag, ParseStatus sts) {
    StringTokenIterator itr;
    if (sts == null) {
      sts = new ParseStatus();
    } else {
      sts.reset();
    } 
    String[] gfmap = GRANDFATHERED.get(new AsciiUtil.CaseInsensitiveKey(languageTag));
    if (gfmap != null) {
      itr = new StringTokenIterator(gfmap[1], "-");
    } else {
      itr = new StringTokenIterator(languageTag, "-");
    } 
    LanguageTag tag = new LanguageTag();
    if (tag.parseLanguage(itr, sts)) {
      tag.parseExtlangs(itr, sts);
      tag.parseScript(itr, sts);
      tag.parseRegion(itr, sts);
      tag.parseVariants(itr, sts);
      tag.parseExtensions(itr, sts);
    } 
    tag.parsePrivateuse(itr, sts);
    if (!itr.isDone() && !sts.isError()) {
      String s = itr.current();
      sts._errorIndex = itr.currentStart();
      if (s.length() == 0) {
        sts._errorMsg = "Empty subtag";
      } else {
        sts._errorMsg = "Invalid subtag: " + s;
      } 
    } 
    return tag;
  }
  
  private boolean parseLanguage(StringTokenIterator itr, ParseStatus sts) {
    if (itr.isDone() || sts.isError())
      return false; 
    boolean found = false;
    String s = itr.current();
    if (isLanguage(s)) {
      found = true;
      this._language = s;
      sts._parseLength = itr.currentEnd();
      itr.next();
    } 
    return found;
  }
  
  private boolean parseExtlangs(StringTokenIterator itr, ParseStatus sts) {
    if (itr.isDone() || sts.isError())
      return false; 
    boolean found = false;
    while (!itr.isDone()) {
      String s = itr.current();
      if (!isExtlang(s))
        break; 
      found = true;
      if (this._extlangs.isEmpty())
        this._extlangs = new ArrayList<String>(3); 
      this._extlangs.add(s);
      sts._parseLength = itr.currentEnd();
      itr.next();
      if (this._extlangs.size() == 3)
        break; 
    } 
    return found;
  }
  
  private boolean parseScript(StringTokenIterator itr, ParseStatus sts) {
    if (itr.isDone() || sts.isError())
      return false; 
    boolean found = false;
    String s = itr.current();
    if (isScript(s)) {
      found = true;
      this._script = s;
      sts._parseLength = itr.currentEnd();
      itr.next();
    } 
    return found;
  }
  
  private boolean parseRegion(StringTokenIterator itr, ParseStatus sts) {
    if (itr.isDone() || sts.isError())
      return false; 
    boolean found = false;
    String s = itr.current();
    if (isRegion(s)) {
      found = true;
      this._region = s;
      sts._parseLength = itr.currentEnd();
      itr.next();
    } 
    return found;
  }
  
  private boolean parseVariants(StringTokenIterator itr, ParseStatus sts) {
    if (itr.isDone() || sts.isError())
      return false; 
    boolean found = false;
    while (!itr.isDone()) {
      String s = itr.current();
      if (!isVariant(s))
        break; 
      found = true;
      if (this._variants.isEmpty())
        this._variants = new ArrayList<String>(3); 
      this._variants.add(s);
      sts._parseLength = itr.currentEnd();
      itr.next();
    } 
    return found;
  }
  
  private boolean parseExtensions(StringTokenIterator itr, ParseStatus sts) {
    if (itr.isDone() || sts.isError())
      return false; 
    boolean found = false;
    while (!itr.isDone()) {
      String s = itr.current();
      if (isExtensionSingleton(s)) {
        int start = itr.currentStart();
        String singleton = s;
        StringBuilder sb = new StringBuilder(singleton);
        itr.next();
        while (!itr.isDone()) {
          s = itr.current();
          if (isExtensionSubtag(s)) {
            sb.append("-").append(s);
            sts._parseLength = itr.currentEnd();
            itr.next();
          } 
        } 
        if (sts._parseLength <= start) {
          sts._errorIndex = start;
          sts._errorMsg = "Incomplete extension '" + singleton + "'";
          break;
        } 
        if (this._extensions.size() == 0)
          this._extensions = new ArrayList<String>(4); 
        this._extensions.add(sb.toString());
        found = true;
      } 
    } 
    return found;
  }
  
  private boolean parsePrivateuse(StringTokenIterator itr, ParseStatus sts) {
    if (itr.isDone() || sts.isError())
      return false; 
    boolean found = false;
    String s = itr.current();
    if (isPrivateusePrefix(s)) {
      int start = itr.currentStart();
      StringBuilder sb = new StringBuilder(s);
      itr.next();
      while (!itr.isDone()) {
        s = itr.current();
        if (!isPrivateuseSubtag(s))
          break; 
        sb.append("-").append(s);
        sts._parseLength = itr.currentEnd();
        itr.next();
      } 
      if (sts._parseLength <= start) {
        sts._errorIndex = start;
        sts._errorMsg = "Incomplete privateuse";
      } else {
        this._privateuse = sb.toString();
        found = true;
      } 
    } 
    return found;
  }
  
  public static LanguageTag parseLocale(BaseLocale baseLocale, LocaleExtensions localeExtensions) {
    LanguageTag tag = new LanguageTag();
    String language = baseLocale.getLanguage();
    String script = baseLocale.getScript();
    String region = baseLocale.getRegion();
    String variant = baseLocale.getVariant();
    boolean hasSubtag = false;
    String privuseVar = null;
    if (language.length() > 0 && isLanguage(language)) {
      if (language.equals("iw")) {
        language = "he";
      } else if (language.equals("ji")) {
        language = "yi";
      } else if (language.equals("in")) {
        language = "id";
      } 
      tag._language = language;
    } 
    if (script.length() > 0 && isScript(script)) {
      tag._script = canonicalizeScript(script);
      hasSubtag = true;
    } 
    if (region.length() > 0 && isRegion(region)) {
      tag._region = canonicalizeRegion(region);
      hasSubtag = true;
    } 
    if (variant.length() > 0) {
      List<String> variants = null;
      StringTokenIterator varitr = new StringTokenIterator(variant, "_");
      while (!varitr.isDone()) {
        String var = varitr.current();
        if (!isVariant(var))
          break; 
        if (variants == null)
          variants = new ArrayList<String>(); 
        variants.add(canonicalizeVariant(var));
        varitr.next();
      } 
      if (variants != null) {
        tag._variants = variants;
        hasSubtag = true;
      } 
      if (!varitr.isDone()) {
        StringBuilder buf = new StringBuilder();
        while (!varitr.isDone()) {
          String prvv = varitr.current();
          if (!isPrivateuseSubtag(prvv))
            break; 
          if (buf.length() > 0)
            buf.append("-"); 
          prvv = AsciiUtil.toLowerString(prvv);
          buf.append(prvv);
          varitr.next();
        } 
        if (buf.length() > 0)
          privuseVar = buf.toString(); 
      } 
    } 
    List<String> extensions = null;
    String privateuse = null;
    Set<Character> locextKeys = localeExtensions.getKeys();
    for (Character locextKey : locextKeys) {
      Extension ext = localeExtensions.getExtension(locextKey);
      if (isPrivateusePrefixChar(locextKey.charValue())) {
        privateuse = ext.getValue();
        continue;
      } 
      if (extensions == null)
        extensions = new ArrayList<String>(); 
      extensions.add(locextKey.toString() + "-" + ext.getValue());
    } 
    if (extensions != null) {
      tag._extensions = extensions;
      hasSubtag = true;
    } 
    if (privuseVar != null)
      if (privateuse == null) {
        privateuse = "lvariant-" + privuseVar;
      } else {
        privateuse = privateuse + "-" + "lvariant" + "-" + privuseVar.replace("_", "-");
      }  
    if (privateuse != null)
      tag._privateuse = privateuse; 
    if (tag._language.length() == 0 && (hasSubtag || privateuse == null))
      tag._language = UNDETERMINED; 
    return tag;
  }
  
  public String getLanguage() {
    return this._language;
  }
  
  public List<String> getExtlangs() {
    return Collections.unmodifiableList(this._extlangs);
  }
  
  public String getScript() {
    return this._script;
  }
  
  public String getRegion() {
    return this._region;
  }
  
  public List<String> getVariants() {
    return Collections.unmodifiableList(this._variants);
  }
  
  public List<String> getExtensions() {
    return Collections.unmodifiableList(this._extensions);
  }
  
  public String getPrivateuse() {
    return this._privateuse;
  }
  
  public static boolean isLanguage(String s) {
    return (s.length() >= 2 && s.length() <= 8 && AsciiUtil.isAlphaString(s));
  }
  
  public static boolean isExtlang(String s) {
    return (s.length() == 3 && AsciiUtil.isAlphaString(s));
  }
  
  public static boolean isScript(String s) {
    return (s.length() == 4 && AsciiUtil.isAlphaString(s));
  }
  
  public static boolean isRegion(String s) {
    return ((s.length() == 2 && AsciiUtil.isAlphaString(s)) || (s.length() == 3 && AsciiUtil.isNumericString(s)));
  }
  
  public static boolean isVariant(String s) {
    int len = s.length();
    if (len >= 5 && len <= 8)
      return AsciiUtil.isAlphaNumericString(s); 
    if (len == 4)
      return (AsciiUtil.isNumeric(s.charAt(0)) && AsciiUtil.isAlphaNumeric(s.charAt(1)) && AsciiUtil.isAlphaNumeric(s.charAt(2)) && AsciiUtil.isAlphaNumeric(s.charAt(3))); 
    return false;
  }
  
  public static boolean isExtensionSingleton(String s) {
    return (s.length() == 1 && AsciiUtil.isAlphaString(s) && !AsciiUtil.caseIgnoreMatch("x", s));
  }
  
  public static boolean isExtensionSingletonChar(char c) {
    return isExtensionSingleton(String.valueOf(c));
  }
  
  public static boolean isExtensionSubtag(String s) {
    return (s.length() >= 2 && s.length() <= 8 && AsciiUtil.isAlphaNumericString(s));
  }
  
  public static boolean isPrivateusePrefix(String s) {
    return (s.length() == 1 && AsciiUtil.caseIgnoreMatch("x", s));
  }
  
  public static boolean isPrivateusePrefixChar(char c) {
    return AsciiUtil.caseIgnoreMatch("x", String.valueOf(c));
  }
  
  public static boolean isPrivateuseSubtag(String s) {
    return (s.length() >= 1 && s.length() <= 8 && AsciiUtil.isAlphaNumericString(s));
  }
  
  public static String canonicalizeLanguage(String s) {
    return AsciiUtil.toLowerString(s);
  }
  
  public static String canonicalizeExtlang(String s) {
    return AsciiUtil.toLowerString(s);
  }
  
  public static String canonicalizeScript(String s) {
    return AsciiUtil.toTitleString(s);
  }
  
  public static String canonicalizeRegion(String s) {
    return AsciiUtil.toUpperString(s);
  }
  
  public static String canonicalizeVariant(String s) {
    return AsciiUtil.toLowerString(s);
  }
  
  public static String canonicalizeExtension(String s) {
    return AsciiUtil.toLowerString(s);
  }
  
  public static String canonicalizeExtensionSingleton(String s) {
    return AsciiUtil.toLowerString(s);
  }
  
  public static String canonicalizeExtensionSubtag(String s) {
    return AsciiUtil.toLowerString(s);
  }
  
  public static String canonicalizePrivateuse(String s) {
    return AsciiUtil.toLowerString(s);
  }
  
  public static String canonicalizePrivateuseSubtag(String s) {
    return AsciiUtil.toLowerString(s);
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (this._language.length() > 0) {
      sb.append(this._language);
      for (String extlang : this._extlangs)
        sb.append("-").append(extlang); 
      if (this._script.length() > 0)
        sb.append("-").append(this._script); 
      if (this._region.length() > 0)
        sb.append("-").append(this._region); 
      for (String variant : this._extlangs)
        sb.append("-").append(variant); 
      for (String extension : this._extensions)
        sb.append("-").append(extension); 
    } 
    if (this._privateuse.length() > 0) {
      if (sb.length() > 0)
        sb.append("-"); 
      sb.append(this._privateuse);
    } 
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\locale\LanguageTag.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */