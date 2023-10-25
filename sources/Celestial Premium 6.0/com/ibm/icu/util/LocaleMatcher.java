/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.Row;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.LocalePriorityList;
import com.ibm.icu.util.ULocale;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class LocaleMatcher {
    private static final boolean DEBUG = false;
    private static final double DEFAULT_THRESHOLD = 0.5;
    private final ULocale defaultLanguage;
    Map<ULocale, Row.R2<ULocale, Double>> maximizedLanguageToWeight = new LinkedHashMap<ULocale, Row.R2<ULocale, Double>>();
    LanguageMatcherData matcherData;
    private static LanguageMatcherData defaultWritten = LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$100(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(new LanguageMatcherData().addDistance("no", "nb", 100, "The language no is normally taken as nb in content; we might alias this for lookup."), "nn", "nb", 96), "nn", "no", 96).addDistance("da", "no", 90, "Danish and norwegian are reasonably close."), "da", "nb", 90).addDistance("hr", "br", 96, "Serbo-croatian variants are all very close."), "sh", "br", 96), "sr", "br", 96), "sh", "hr", 96), "sr", "hr", 96), "sh", "sr", 96).addDistance("sr-Latn", "sr-Cyrl", 90, "Most serbs can read either script."), "*-Hans", "*-Hant", 85, true, "Readers of simplified can read traditional much better than reverse.").addDistance("*-Hant", "*-Hans", 75, true).addDistance("en-*-US", "en-*-CA", 98, "US is different than others, and Canadian is inbetween."), "en-*-US", "en-*-*", 97), "en-*-CA", "en-*-*", 98), "en-*-*", "en-*-*", 99).addDistance("es-*-ES", "es-*-ES", 100, "Latin American Spanishes are closer to each other. Approximate by having es-ES be further from everything else."), "es-*-ES", "es-*-*", 93).addDistance("*", "*", 1, "[Default value -- must be at end!] Normally there is no comprehension of different languages.").addDistance("*-*", "*-*", 20, "[Default value -- must be at end!] Normally there is little comprehension of different scripts.").addDistance("*-*-*", "*-*-*", 96, "[Default value -- must be at end!] Normally there are small differences across regions.").freeze();
    private static HashMap<String, String> canonicalMap = new HashMap();

    public LocaleMatcher(LocalePriorityList languagePriorityList) {
        this(languagePriorityList, defaultWritten);
    }

    public LocaleMatcher(String languagePriorityListString) {
        this(LocalePriorityList.add(languagePriorityListString).build());
    }

    public LocaleMatcher(LocalePriorityList languagePriorityList, LanguageMatcherData matcherData) {
        this.matcherData = matcherData;
        for (ULocale language : languagePriorityList) {
            this.add(language, languagePriorityList.getWeight(language));
        }
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
        if (lang2 != null || script2 != null || region2 != null) {
            return new ULocale(lang2 == null ? lang : lang2, script2 == null ? script : script2, region2 == null ? region : region2);
        }
        return ulocale;
    }

    public ULocale getBestMatch(LocalePriorityList languageList) {
        double bestWeight = 0.0;
        ULocale bestTableMatch = null;
        for (ULocale language : languageList) {
            Row.R2<ULocale, Double> matchRow = this.getBestMatchInternal(language);
            double weight = (Double)matchRow.get1() * languageList.getWeight(language);
            if (!(weight > bestWeight)) continue;
            bestWeight = weight;
            bestTableMatch = (ULocale)matchRow.get0();
        }
        if (bestWeight < 0.5) {
            bestTableMatch = this.defaultLanguage;
        }
        return bestTableMatch;
    }

    public ULocale getBestMatch(String languageList) {
        return this.getBestMatch(LocalePriorityList.add(languageList).build());
    }

    public ULocale getBestMatch(ULocale ulocale) {
        return (ULocale)this.getBestMatchInternal(ulocale).get0();
    }

    public String toString() {
        return "{" + this.defaultLanguage + ", " + this.maximizedLanguageToWeight + "}";
    }

    private Row.R2<ULocale, Double> getBestMatchInternal(ULocale languageCode) {
        languageCode = this.canonicalize(languageCode);
        ULocale maximized = this.addLikelySubtags(languageCode);
        double bestWeight = 0.0;
        ULocale bestTableMatch = null;
        for (ULocale tableKey : this.maximizedLanguageToWeight.keySet()) {
            Row.R2<ULocale, Double> row;
            double match = this.match(languageCode, maximized, tableKey, (ULocale)(row = this.maximizedLanguageToWeight.get(tableKey)).get0());
            double weight = match * (Double)row.get1();
            if (!(weight > bestWeight)) continue;
            bestWeight = weight;
            bestTableMatch = tableKey;
        }
        if (bestWeight < 0.5) {
            bestTableMatch = this.defaultLanguage;
        }
        return Row.R2.of(bestTableMatch, bestWeight);
    }

    private void add(ULocale language, Double weight) {
        language = this.canonicalize(language);
        Row.R2<ULocale, Double> row = Row.of(this.addLikelySubtags(language), weight);
        this.maximizedLanguageToWeight.put(language, row);
    }

    private ULocale addLikelySubtags(ULocale languageCode) {
        ULocale result = ULocale.addLikelySubtags(languageCode);
        if (result == null || result.equals(languageCode)) {
            String language = languageCode.getLanguage();
            String script = languageCode.getScript();
            String region = languageCode.getCountry();
            return new ULocale((language.length() == 0 ? "und" : language) + "_" + (script.length() == 0 ? "Zzzz" : script) + "_" + (region.length() == 0 ? "ZZ" : region));
        }
        return result;
    }

    static {
        canonicalMap.put("iw", "he");
        canonicalMap.put("mo", "ro");
        canonicalMap.put("tl", "fil");
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class LanguageMatcherData
    implements Freezable<LanguageMatcherData> {
        ScoreData languageScores = new ScoreData(Level.language);
        ScoreData scriptScores = new ScoreData(Level.script);
        ScoreData regionScores = new ScoreData(Level.region);
        private boolean frozen = false;

        public double match(ULocale a, ULocale aMax, ULocale b, ULocale bMax) {
            double diff = 0.0;
            diff += this.languageScores.getScore(a, aMax, a.getLanguage(), aMax.getLanguage(), b, bMax, b.getLanguage(), bMax.getLanguage());
            diff += this.scriptScores.getScore(a, aMax, a.getScript(), aMax.getScript(), b, bMax, b.getScript(), bMax.getScript());
            diff += this.regionScores.getScore(a, aMax, a.getCountry(), aMax.getCountry(), b, bMax, b.getCountry(), bMax.getCountry());
            if (!a.getVariant().equals(b.getVariant())) {
                diff += 1.0;
            }
            if (diff < 0.0) {
                diff = 0.0;
            } else if (diff > 1.0) {
                diff = 1.0;
            }
            return 1.0 - diff;
        }

        private LanguageMatcherData addDistance(String desired, String supported, int percent) {
            return this.addDistance(desired, supported, percent, false, null);
        }

        public LanguageMatcherData addDistance(String desired, String supported, int percent, String comment) {
            return this.addDistance(desired, supported, percent, false, comment);
        }

        public LanguageMatcherData addDistance(String desired, String supported, int percent, boolean oneway) {
            return this.addDistance(desired, supported, percent, oneway, null);
        }

        private LanguageMatcherData addDistance(String desired, String supported, int percent, boolean oneway, String comment) {
            LocalePatternMatcher supportedMatcher;
            Level supportedLen;
            double score = 1.0 - (double)percent / 100.0;
            LocalePatternMatcher desiredMatcher = new LocalePatternMatcher(desired);
            Level desiredLen = desiredMatcher.getLevel();
            if (desiredLen != (supportedLen = (supportedMatcher = new LocalePatternMatcher(supported)).getLevel())) {
                throw new IllegalArgumentException();
            }
            Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> data = Row.of(desiredMatcher, supportedMatcher, score);
            Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> data2 = oneway ? null : Row.of(supportedMatcher, desiredMatcher, score);
            switch (desiredLen) {
                case language: {
                    String dlanguage = desiredMatcher.getLanguage();
                    String slanguage = supportedMatcher.getLanguage();
                    this.languageScores.addDataToScores(dlanguage, slanguage, data);
                    if (oneway) break;
                    this.languageScores.addDataToScores(slanguage, dlanguage, data2);
                    break;
                }
                case script: {
                    String dscript = desiredMatcher.getScript();
                    String sscript = supportedMatcher.getScript();
                    this.scriptScores.addDataToScores(dscript, sscript, data);
                    if (oneway) break;
                    this.scriptScores.addDataToScores(sscript, dscript, data2);
                    break;
                }
                case region: {
                    String dregion = desiredMatcher.getRegion();
                    String sregion = supportedMatcher.getRegion();
                    this.regionScores.addDataToScores(dregion, sregion, data);
                    if (oneway) break;
                    this.regionScores.addDataToScores(sregion, dregion, data2);
                }
            }
            return this;
        }

        @Override
        public LanguageMatcherData cloneAsThawed() {
            try {
                LanguageMatcherData result = (LanguageMatcherData)this.clone();
                result.languageScores = this.languageScores.cloneAsThawed();
                result.scriptScores = this.scriptScores.cloneAsThawed();
                result.regionScores = this.regionScores.cloneAsThawed();
                result.frozen = false;
                return result;
            }
            catch (CloneNotSupportedException e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public LanguageMatcherData freeze() {
            return this;
        }

        @Override
        public boolean isFrozen() {
            return this.frozen;
        }

        static /* synthetic */ LanguageMatcherData access$000(LanguageMatcherData x0, String x1, String x2, int x3) {
            return x0.addDistance(x1, x2, x3);
        }

        static /* synthetic */ LanguageMatcherData access$100(LanguageMatcherData x0, String x1, String x2, int x3, boolean x4, String x5) {
            return x0.addDistance(x1, x2, x3, x4, x5);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class ScoreData
    implements Freezable<ScoreData> {
        LinkedHashSet<Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double>> scores = new LinkedHashSet();
        final double worst;
        final Level level;
        private boolean frozen = false;

        public ScoreData(Level level) {
            this.level = level;
            this.worst = (double)(1 - (level == Level.language ? 90 : (level == Level.script ? 20 : 4))) / 100.0;
        }

        void addDataToScores(String desired, String supported, Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> data) {
            this.scores.add(data);
        }

        double getScore(ULocale desiredLocale, ULocale dMax, String desiredRaw, String desiredMax, ULocale supportedLocale, ULocale sMax, String supportedRaw, String supportedMax) {
            double distance;
            boolean desiredChange = desiredRaw.equals(desiredMax);
            boolean supportedChange = supportedRaw.equals(supportedMax);
            if (!desiredMax.equals(supportedMax)) {
                distance = this.getRawScore(dMax, sMax);
                if (desiredChange == supportedChange) {
                    distance *= 0.75;
                } else if (desiredChange) {
                    distance *= 0.5;
                }
            } else {
                distance = desiredChange == supportedChange ? 0.0 : 0.25 * this.worst;
            }
            return distance;
        }

        private double getRawScore(ULocale desiredLocale, ULocale supportedLocale) {
            for (Row.R3 r3 : this.scores) {
                if (!((LocalePatternMatcher)r3.get0()).matches(desiredLocale) || !((LocalePatternMatcher)r3.get1()).matches(supportedLocale)) continue;
                return (Double)r3.get2();
            }
            return this.worst;
        }

        public String toString() {
            return (Object)((Object)this.level) + ", " + this.scores;
        }

        @Override
        public ScoreData cloneAsThawed() {
            try {
                ScoreData result = (ScoreData)this.clone();
                result.scores = (LinkedHashSet)result.scores.clone();
                result.frozen = false;
                return result;
            }
            catch (CloneNotSupportedException e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public ScoreData freeze() {
            return this;
        }

        @Override
        public boolean isFrozen() {
            return this.frozen;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static enum Level {
        language,
        script,
        region;

    }

    private static class LocalePatternMatcher {
        private String lang;
        private String script;
        private String region;
        private Level level;
        static Pattern pattern = Pattern.compile("([a-zA-Z]{1,8}|\\*)(?:-([a-zA-Z]{4}|\\*))?(?:-([a-zA-Z]{2}|[0-9]{3}|\\*))?");

        public LocalePatternMatcher(String toMatch) {
            Matcher matcher = pattern.matcher(toMatch);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Bad pattern: " + toMatch);
            }
            this.lang = matcher.group(1);
            this.script = matcher.group(2);
            this.region = matcher.group(3);
            Level level = this.region != null ? Level.region : (this.level = this.script != null ? Level.script : Level.language);
            if (this.lang.equals("*")) {
                this.lang = null;
            }
            if (this.script != null && this.script.equals("*")) {
                this.script = null;
            }
            if (this.region != null && this.region.equals("*")) {
                this.region = null;
            }
        }

        boolean matches(ULocale ulocale) {
            if (this.lang != null && !this.lang.equals(ulocale.getLanguage())) {
                return false;
            }
            if (this.script != null && !this.script.equals(ulocale.getScript())) {
                return false;
            }
            return this.region == null || this.region.equals(ulocale.getCountry());
        }

        public Level getLevel() {
            return this.level;
        }

        public String getLanguage() {
            return this.lang == null ? "*" : this.lang;
        }

        public String getScript() {
            return this.script == null ? "*" : this.script;
        }

        public String getRegion() {
            return this.region == null ? "*" : this.region;
        }

        public String toString() {
            String result = this.getLanguage();
            if (this.level != Level.language) {
                result = result + "-" + this.getScript();
                if (this.level != Level.script) {
                    result = result + "-" + this.getRegion();
                }
            }
            return result;
        }
    }
}

