package com.ibm.icu.util;

import com.ibm.icu.impl.Row;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocaleMatcher {
  private static final boolean DEBUG = false;
  
  private static final double DEFAULT_THRESHOLD = 0.5D;
  
  private final ULocale defaultLanguage;
  
  Map<ULocale, Row.R2<ULocale, Double>> maximizedLanguageToWeight;
  
  LanguageMatcherData matcherData;
  
  public LocaleMatcher(LocalePriorityList languagePriorityList) {
    this(languagePriorityList, defaultWritten);
  }
  
  public LocaleMatcher(String languagePriorityListString) {
    this(LocalePriorityList.add(languagePriorityListString).build());
  }
  
  public LocaleMatcher(LocalePriorityList languagePriorityList, LanguageMatcherData matcherData) {
    this.maximizedLanguageToWeight = new LinkedHashMap<ULocale, Row.R2<ULocale, Double>>();
    this.matcherData = matcherData;
    for (ULocale language : languagePriorityList)
      add(language, languagePriorityList.getWeight(language)); 
    Iterator<ULocale> it = languagePriorityList.iterator();
    this.defaultLanguage = it.hasNext() ? it.next() : null;
  }
  
  public double match(ULocale desired, ULocale desiredMax, ULocale supported, ULocale supportedMax) {
    return this.matcherData.match(desired, desiredMax, supported, supportedMax);
  }
  
  public ULocale canonicalize(ULocale ulocale) {
    String lang = ulocale.getLanguage();
    String lang2 = canonicalMap.get(lang);
    String script = ulocale.getScript();
    String script2 = canonicalMap.get(script);
    String region = ulocale.getCountry();
    String region2 = canonicalMap.get(region);
    if (lang2 != null || script2 != null || region2 != null)
      return new ULocale((lang2 == null) ? lang : lang2, (script2 == null) ? script : script2, (region2 == null) ? region : region2); 
    return ulocale;
  }
  
  public ULocale getBestMatch(LocalePriorityList languageList) {
    double bestWeight = 0.0D;
    ULocale bestTableMatch = null;
    for (ULocale language : languageList) {
      Row.R2<ULocale, Double> matchRow = getBestMatchInternal(language);
      double weight = ((Double)matchRow.get1()).doubleValue() * languageList.getWeight(language).doubleValue();
      if (weight > bestWeight) {
        bestWeight = weight;
        bestTableMatch = (ULocale)matchRow.get0();
      } 
    } 
    if (bestWeight < 0.5D)
      bestTableMatch = this.defaultLanguage; 
    return bestTableMatch;
  }
  
  public ULocale getBestMatch(String languageList) {
    return getBestMatch(LocalePriorityList.add(languageList).build());
  }
  
  public ULocale getBestMatch(ULocale ulocale) {
    return (ULocale)getBestMatchInternal(ulocale).get0();
  }
  
  public String toString() {
    return "{" + this.defaultLanguage + ", " + this.maximizedLanguageToWeight + "}";
  }
  
  private Row.R2<ULocale, Double> getBestMatchInternal(ULocale languageCode) {
    languageCode = canonicalize(languageCode);
    ULocale maximized = addLikelySubtags(languageCode);
    double bestWeight = 0.0D;
    ULocale bestTableMatch = null;
    for (ULocale tableKey : this.maximizedLanguageToWeight.keySet()) {
      Row.R2<ULocale, Double> row = this.maximizedLanguageToWeight.get(tableKey);
      double match = match(languageCode, maximized, tableKey, (ULocale)row.get0());
      double weight = match * ((Double)row.get1()).doubleValue();
      if (weight > bestWeight) {
        bestWeight = weight;
        bestTableMatch = tableKey;
      } 
    } 
    if (bestWeight < 0.5D)
      bestTableMatch = this.defaultLanguage; 
    return Row.R2.of(bestTableMatch, Double.valueOf(bestWeight));
  }
  
  private void add(ULocale language, Double weight) {
    language = canonicalize(language);
    Row.R2<ULocale, Double> row = Row.of(addLikelySubtags(language), weight);
    this.maximizedLanguageToWeight.put(language, row);
  }
  
  private ULocale addLikelySubtags(ULocale languageCode) {
    ULocale result = ULocale.addLikelySubtags(languageCode);
    if (result == null || result.equals(languageCode)) {
      String language = languageCode.getLanguage();
      String script = languageCode.getScript();
      String region = languageCode.getCountry();
      return new ULocale(((language.length() == 0) ? "und" : language) + "_" + ((script.length() == 0) ? "Zzzz" : script) + "_" + ((region.length() == 0) ? "ZZ" : region));
    } 
    return result;
  }
  
  private static class LocalePatternMatcher {
    private String lang;
    
    private String script;
    
    private String region;
    
    private LocaleMatcher.Level level;
    
    static Pattern pattern = Pattern.compile("([a-zA-Z]{1,8}|\\*)(?:-([a-zA-Z]{4}|\\*))?(?:-([a-zA-Z]{2}|[0-9]{3}|\\*))?");
    
    public LocalePatternMatcher(String toMatch) {
      Matcher matcher = pattern.matcher(toMatch);
      if (!matcher.matches())
        throw new IllegalArgumentException("Bad pattern: " + toMatch); 
      this.lang = matcher.group(1);
      this.script = matcher.group(2);
      this.region = matcher.group(3);
      this.level = (this.region != null) ? LocaleMatcher.Level.region : ((this.script != null) ? LocaleMatcher.Level.script : LocaleMatcher.Level.language);
      if (this.lang.equals("*"))
        this.lang = null; 
      if (this.script != null && this.script.equals("*"))
        this.script = null; 
      if (this.region != null && this.region.equals("*"))
        this.region = null; 
    }
    
    boolean matches(ULocale ulocale) {
      if (this.lang != null && !this.lang.equals(ulocale.getLanguage()))
        return false; 
      if (this.script != null && !this.script.equals(ulocale.getScript()))
        return false; 
      if (this.region != null && !this.region.equals(ulocale.getCountry()))
        return false; 
      return true;
    }
    
    public LocaleMatcher.Level getLevel() {
      return this.level;
    }
    
    public String getLanguage() {
      return (this.lang == null) ? "*" : this.lang;
    }
    
    public String getScript() {
      return (this.script == null) ? "*" : this.script;
    }
    
    public String getRegion() {
      return (this.region == null) ? "*" : this.region;
    }
    
    public String toString() {
      String result = getLanguage();
      if (this.level != LocaleMatcher.Level.language) {
        result = result + "-" + getScript();
        if (this.level != LocaleMatcher.Level.script)
          result = result + "-" + getRegion(); 
      } 
      return result;
    }
  }
  
  enum Level {
    language, script, region;
  }
  
  private static class ScoreData implements Freezable<ScoreData> {
    LinkedHashSet<Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double>> scores = new LinkedHashSet<Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double>>();
    
    final double worst;
    
    final LocaleMatcher.Level level;
    
    private boolean frozen;
    
    void addDataToScores(String desired, String supported, Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double> data) {
      this.scores.add(data);
    }
    
    double getScore(ULocale desiredLocale, ULocale dMax, String desiredRaw, String desiredMax, ULocale supportedLocale, ULocale sMax, String supportedRaw, String supportedMax) {
      double distance;
      boolean desiredChange = desiredRaw.equals(desiredMax);
      boolean supportedChange = supportedRaw.equals(supportedMax);
      if (!desiredMax.equals(supportedMax)) {
        distance = getRawScore(dMax, sMax);
        if (desiredChange == supportedChange) {
          distance *= 0.75D;
        } else if (desiredChange) {
          distance *= 0.5D;
        } 
      } else if (desiredChange == supportedChange) {
        distance = 0.0D;
      } else {
        distance = 0.25D * this.worst;
      } 
      return distance;
    }
    
    private double getRawScore(ULocale desiredLocale, ULocale supportedLocale) {
      for (Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double> datum : this.scores) {
        if (((LocaleMatcher.LocalePatternMatcher)datum.get0()).matches(desiredLocale) && ((LocaleMatcher.LocalePatternMatcher)datum.get1()).matches(supportedLocale))
          return ((Double)datum.get2()).doubleValue(); 
      } 
      return this.worst;
    }
    
    public String toString() {
      return this.level + ", " + this.scores;
    }
    
    public ScoreData cloneAsThawed() {
      try {
        ScoreData result = (ScoreData)clone();
        result.scores = (LinkedHashSet<Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double>>)result.scores.clone();
        result.frozen = false;
        return result;
      } catch (CloneNotSupportedException e) {
        throw new IllegalArgumentException(e);
      } 
    }
    
    public ScoreData(LocaleMatcher.Level level) {
      this.frozen = false;
      this.level = level;
      this.worst = (1 - ((level == LocaleMatcher.Level.language) ? 90 : ((level == LocaleMatcher.Level.script) ? 20 : 4))) / 100.0D;
    }
    
    public ScoreData freeze() {
      return this;
    }
    
    public boolean isFrozen() {
      return this.frozen;
    }
  }
  
  public static class LanguageMatcherData implements Freezable<LanguageMatcherData> {
    LocaleMatcher.ScoreData languageScores = new LocaleMatcher.ScoreData(LocaleMatcher.Level.language);
    
    LocaleMatcher.ScoreData scriptScores = new LocaleMatcher.ScoreData(LocaleMatcher.Level.script);
    
    LocaleMatcher.ScoreData regionScores = new LocaleMatcher.ScoreData(LocaleMatcher.Level.region);
    
    private boolean frozen;
    
    public double match(ULocale a, ULocale aMax, ULocale b, ULocale bMax) {
      double diff = 0.0D;
      diff += this.languageScores.getScore(a, aMax, a.getLanguage(), aMax.getLanguage(), b, bMax, b.getLanguage(), bMax.getLanguage());
      diff += this.scriptScores.getScore(a, aMax, a.getScript(), aMax.getScript(), b, bMax, b.getScript(), bMax.getScript());
      diff += this.regionScores.getScore(a, aMax, a.getCountry(), aMax.getCountry(), b, bMax, b.getCountry(), bMax.getCountry());
      if (!a.getVariant().equals(b.getVariant()))
        diff++; 
      if (diff < 0.0D) {
        diff = 0.0D;
      } else if (diff > 1.0D) {
        diff = 1.0D;
      } 
      return 1.0D - diff;
    }
    
    private LanguageMatcherData addDistance(String desired, String supported, int percent) {
      return addDistance(desired, supported, percent, false, null);
    }
    
    public LanguageMatcherData addDistance(String desired, String supported, int percent, String comment) {
      return addDistance(desired, supported, percent, false, comment);
    }
    
    public LanguageMatcherData addDistance(String desired, String supported, int percent, boolean oneway) {
      return addDistance(desired, supported, percent, oneway, null);
    }
    
    private LanguageMatcherData addDistance(String desired, String supported, int percent, boolean oneway, String comment) {
      String dlanguage, slanguage, dscript, sscript, dregion, sregion;
      double score = 1.0D - percent / 100.0D;
      LocaleMatcher.LocalePatternMatcher desiredMatcher = new LocaleMatcher.LocalePatternMatcher(desired);
      LocaleMatcher.Level desiredLen = desiredMatcher.getLevel();
      LocaleMatcher.LocalePatternMatcher supportedMatcher = new LocaleMatcher.LocalePatternMatcher(supported);
      LocaleMatcher.Level supportedLen = supportedMatcher.getLevel();
      if (desiredLen != supportedLen)
        throw new IllegalArgumentException(); 
      Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double> data = Row.of(desiredMatcher, supportedMatcher, Double.valueOf(score));
      Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double> data2 = oneway ? null : Row.of(supportedMatcher, desiredMatcher, Double.valueOf(score));
      switch (desiredLen) {
        case language:
          dlanguage = desiredMatcher.getLanguage();
          slanguage = supportedMatcher.getLanguage();
          this.languageScores.addDataToScores(dlanguage, slanguage, data);
          if (!oneway)
            this.languageScores.addDataToScores(slanguage, dlanguage, data2); 
          break;
        case script:
          dscript = desiredMatcher.getScript();
          sscript = supportedMatcher.getScript();
          this.scriptScores.addDataToScores(dscript, sscript, data);
          if (!oneway)
            this.scriptScores.addDataToScores(sscript, dscript, data2); 
          break;
        case region:
          dregion = desiredMatcher.getRegion();
          sregion = supportedMatcher.getRegion();
          this.regionScores.addDataToScores(dregion, sregion, data);
          if (!oneway)
            this.regionScores.addDataToScores(sregion, dregion, data2); 
          break;
      } 
      return this;
    }
    
    public LanguageMatcherData cloneAsThawed() {
      try {
        LanguageMatcherData result = (LanguageMatcherData)clone();
        result.languageScores = this.languageScores.cloneAsThawed();
        result.scriptScores = this.scriptScores.cloneAsThawed();
        result.regionScores = this.regionScores.cloneAsThawed();
        result.frozen = false;
        return result;
      } catch (CloneNotSupportedException e) {
        throw new IllegalArgumentException(e);
      } 
    }
    
    public LanguageMatcherData() {
      this.frozen = false;
    }
    
    public LanguageMatcherData freeze() {
      return this;
    }
    
    public boolean isFrozen() {
      return this.frozen;
    }
  }
  
  private static LanguageMatcherData defaultWritten = (new LanguageMatcherData()).addDistance("no", "nb", 100, "The language no is normally taken as nb in content; we might alias this for lookup.").addDistance("nn", "nb", 96).addDistance("nn", "no", 96).addDistance("da", "no", 90, "Danish and norwegian are reasonably close.").addDistance("da", "nb", 90).addDistance("hr", "br", 96, "Serbo-croatian variants are all very close.").addDistance("sh", "br", 96).addDistance("sr", "br", 96).addDistance("sh", "hr", 96).addDistance("sr", "hr", 96).addDistance("sh", "sr", 96).addDistance("sr-Latn", "sr-Cyrl", 90, "Most serbs can read either script.").addDistance("*-Hans", "*-Hant", 85, true, "Readers of simplified can read traditional much better than reverse.").addDistance("*-Hant", "*-Hans", 75, true).addDistance("en-*-US", "en-*-CA", 98, "US is different than others, and Canadian is inbetween.").addDistance("en-*-US", "en-*-*", 97).addDistance("en-*-CA", "en-*-*", 98).addDistance("en-*-*", "en-*-*", 99).addDistance("es-*-ES", "es-*-ES", 100, "Latin American Spanishes are closer to each other. Approximate by having es-ES be further from everything else.").addDistance("es-*-ES", "es-*-*", 93).addDistance("*", "*", 1, "[Default value -- must be at end!] Normally there is no comprehension of different languages.").addDistance("*-*", "*-*", 20, "[Default value -- must be at end!] Normally there is little comprehension of different scripts.").addDistance("*-*-*", "*-*-*", 96, "[Default value -- must be at end!] Normally there are small differences across regions.").freeze();
  
  private static HashMap<String, String> canonicalMap = new HashMap<String, String>();
  
  static {
    canonicalMap.put("iw", "he");
    canonicalMap.put("mo", "ro");
    canonicalMap.put("tl", "fil");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\LocaleMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */