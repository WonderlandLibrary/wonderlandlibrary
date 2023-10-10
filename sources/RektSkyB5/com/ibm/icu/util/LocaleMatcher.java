/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.Relation;
import com.ibm.icu.impl.Row;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.locale.XLocaleDistance;
import com.ibm.icu.impl.locale.XLocaleMatcher;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.LocalePriorityList;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocaleMatcher {
    @Deprecated
    public static final boolean DEBUG = false;
    private static final ULocale UNKNOWN_LOCALE = new ULocale("und");
    private static final double DEFAULT_THRESHOLD = 0.5;
    private final ULocale defaultLanguage;
    private final double threshold;
    Set<Row.R3<ULocale, ULocale, Double>> localeToMaxLocaleAndWeight = new LinkedHashSet<Row.R3<ULocale, ULocale, Double>>();
    Map<String, Set<Row.R3<ULocale, ULocale, Double>>> desiredLanguageToPossibleLocalesToMaxLocaleToData = new LinkedHashMap<String, Set<Row.R3<ULocale, ULocale, Double>>>();
    LanguageMatcherData matcherData;
    LocalePriorityList languagePriorityList;
    private static final LanguageMatcherData defaultWritten;
    private static HashMap<String, String> canonicalMap;
    transient XLocaleMatcher xLocaleMatcher = null;
    transient ULocale xDefaultLanguage = null;
    transient boolean xFavorScript = false;

    public LocaleMatcher(LocalePriorityList languagePriorityList) {
        this(languagePriorityList, defaultWritten);
    }

    public LocaleMatcher(String languagePriorityListString) {
        this(LocalePriorityList.add(languagePriorityListString).build());
    }

    @Deprecated
    public LocaleMatcher(LocalePriorityList languagePriorityList, LanguageMatcherData matcherData) {
        this(languagePriorityList, matcherData, 0.5);
    }

    @Deprecated
    public LocaleMatcher(LocalePriorityList languagePriorityList, LanguageMatcherData matcherData, double threshold) {
        this.matcherData = matcherData == null ? defaultWritten : matcherData.freeze();
        this.languagePriorityList = languagePriorityList;
        for (ULocale language : languagePriorityList) {
            this.add(language, languagePriorityList.getWeight(language));
        }
        this.processMapping();
        Iterator<ULocale> it = languagePriorityList.iterator();
        this.defaultLanguage = it.hasNext() ? it.next() : null;
        this.threshold = threshold;
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
        double penalty = 0.0;
        OutputDouble matchWeight = new OutputDouble();
        for (ULocale language : languageList) {
            ULocale matchLocale = this.getBestMatchInternal(language, matchWeight);
            double weight = matchWeight.value * languageList.getWeight(language) - penalty;
            if (weight > bestWeight) {
                bestWeight = weight;
                bestTableMatch = matchLocale;
            }
            penalty += 0.07000001;
        }
        if (bestWeight < this.threshold) {
            bestTableMatch = this.defaultLanguage;
        }
        return bestTableMatch;
    }

    public ULocale getBestMatch(String languageList) {
        return this.getBestMatch(LocalePriorityList.add(languageList).build());
    }

    public ULocale getBestMatch(ULocale ulocale) {
        return this.getBestMatchInternal(ulocale, null);
    }

    @Deprecated
    public ULocale getBestMatch(ULocale ... ulocales) {
        return this.getBestMatch(LocalePriorityList.add(ulocales).build());
    }

    public String toString() {
        return "{" + this.defaultLanguage + ", " + this.localeToMaxLocaleAndWeight + "}";
    }

    private ULocale getBestMatchInternal(ULocale languageCode, OutputDouble outputWeight) {
        languageCode = this.canonicalize(languageCode);
        ULocale maximized = this.addLikelySubtags(languageCode);
        double bestWeight = 0.0;
        ULocale bestTableMatch = null;
        String baseLanguage = maximized.getLanguage();
        Set<Row.R3<ULocale, ULocale, Double>> searchTable = this.desiredLanguageToPossibleLocalesToMaxLocaleToData.get(baseLanguage);
        if (searchTable != null) {
            for (Row.R3<ULocale, ULocale, Double> tableKeyValue : searchTable) {
                ULocale tableKey = (ULocale)tableKeyValue.get0();
                ULocale maxLocale = (ULocale)tableKeyValue.get1();
                Double matchedWeight = (Double)tableKeyValue.get2();
                double match = this.match(languageCode, maximized, tableKey, maxLocale);
                double weight = match * matchedWeight;
                if (!(weight > bestWeight)) continue;
                bestWeight = weight;
                bestTableMatch = tableKey;
                if (!(weight > 0.999)) continue;
                break;
            }
        }
        if (bestWeight < this.threshold) {
            bestTableMatch = this.defaultLanguage;
        }
        if (outputWeight != null) {
            outputWeight.value = bestWeight;
        }
        return bestTableMatch;
    }

    private void add(ULocale language, Double weight) {
        language = this.canonicalize(language);
        Row.R3<ULocale, ULocale, Double> row = Row.of(language, this.addLikelySubtags(language), weight);
        row.freeze();
        this.localeToMaxLocaleAndWeight.add(row);
    }

    private void processMapping() {
        for (Map.Entry<String, Set<String>> entry : this.matcherData.matchingLanguages().keyValuesSet()) {
            String desired = entry.getKey();
            Set<String> supported = entry.getValue();
            for (Row.R3<ULocale, ULocale, Double> localeToMaxAndWeight : this.localeToMaxLocaleAndWeight) {
                ULocale key = (ULocale)localeToMaxAndWeight.get0();
                String lang = key.getLanguage();
                if (!supported.contains(lang)) continue;
                this.addFiltered(desired, localeToMaxAndWeight);
            }
        }
        for (Row.R3 r3 : this.localeToMaxLocaleAndWeight) {
            ULocale key = (ULocale)r3.get0();
            String lang = key.getLanguage();
            this.addFiltered(lang, r3);
        }
    }

    private void addFiltered(String desired, Row.R3<ULocale, ULocale, Double> localeToMaxAndWeight) {
        Set<Row.R3<ULocale, ULocale, Double>> map = this.desiredLanguageToPossibleLocalesToMaxLocaleToData.get(desired);
        if (map == null) {
            map = new LinkedHashSet<Row.R3<ULocale, ULocale, Double>>();
            this.desiredLanguageToPossibleLocalesToMaxLocaleToData.put(desired, map);
        }
        map.add(localeToMaxAndWeight);
    }

    private ULocale addLikelySubtags(ULocale languageCode) {
        if (languageCode.equals(UNKNOWN_LOCALE)) {
            return UNKNOWN_LOCALE;
        }
        ULocale result = ULocale.addLikelySubtags(languageCode);
        if (result == null || result.equals(languageCode)) {
            String language = languageCode.getLanguage();
            String script = languageCode.getScript();
            String region = languageCode.getCountry();
            return new ULocale((language.length() == 0 ? "und" : language) + "_" + (script.length() == 0 ? "Zzzz" : script) + "_" + (region.length() == 0 ? "ZZ" : region));
        }
        return result;
    }

    @Deprecated
    public static ICUResourceBundle getICUSupplementalData() {
        ICUResourceBundle suppData = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        return suppData;
    }

    @Deprecated
    public static double match(ULocale a2, ULocale b2) {
        LocaleMatcher matcher = new LocaleMatcher("");
        return matcher.match(a2, matcher.addLikelySubtags(a2), b2, matcher.addLikelySubtags(b2));
    }

    @Deprecated
    public int distance(ULocale desired, ULocale supported) {
        return this.getLocaleMatcher().distance(desired, supported);
    }

    private synchronized XLocaleMatcher getLocaleMatcher() {
        if (this.xLocaleMatcher == null) {
            XLocaleMatcher.Builder builder = XLocaleMatcher.builder();
            builder.setSupportedLocales(this.languagePriorityList);
            if (this.xDefaultLanguage != null) {
                builder.setDefaultLanguage(this.xDefaultLanguage);
            }
            if (this.xFavorScript) {
                builder.setDistanceOption(XLocaleDistance.DistanceOption.SCRIPT_FIRST);
            }
            this.xLocaleMatcher = builder.build();
        }
        return this.xLocaleMatcher;
    }

    @Deprecated
    public ULocale getBestMatch(LinkedHashSet<ULocale> desiredLanguages, Output<ULocale> outputBestDesired) {
        return this.getLocaleMatcher().getBestMatch(desiredLanguages, outputBestDesired);
    }

    @Deprecated
    public synchronized LocaleMatcher setDefaultLanguage(ULocale defaultLanguage) {
        this.xDefaultLanguage = defaultLanguage;
        this.xLocaleMatcher = null;
        return this;
    }

    @Deprecated
    public synchronized LocaleMatcher setFavorScript(boolean favorScript) {
        this.xFavorScript = favorScript;
        this.xLocaleMatcher = null;
        return this;
    }

    static {
        canonicalMap = new HashMap();
        canonicalMap.put("iw", "he");
        canonicalMap.put("mo", "ro");
        canonicalMap.put("tl", "fil");
        ICUResourceBundle suppData = LocaleMatcher.getICUSupplementalData();
        ICUResourceBundle languageMatching = suppData.findTopLevel("languageMatching");
        ICUResourceBundle written = (ICUResourceBundle)languageMatching.get("written");
        defaultWritten = new LanguageMatcherData();
        UResourceBundleIterator iter = written.getIterator();
        while (iter.hasNext()) {
            ICUResourceBundle item = (ICUResourceBundle)iter.next();
            boolean oneway = item.getSize() > 3 && "1".equals(item.getString(3));
            defaultWritten.addDistance(item.getString(0), item.getString(1), Integer.parseInt(item.getString(2)), oneway);
        }
        defaultWritten.freeze();
    }

    @Deprecated
    public static class LanguageMatcherData
    implements Freezable<LanguageMatcherData> {
        private ScoreData languageScores = new ScoreData(Level.language);
        private ScoreData scriptScores = new ScoreData(Level.script);
        private ScoreData regionScores = new ScoreData(Level.region);
        private Relation<String, String> matchingLanguages;
        private volatile boolean frozen = false;

        @Deprecated
        public LanguageMatcherData() {
        }

        @Deprecated
        public Relation<String, String> matchingLanguages() {
            return this.matchingLanguages;
        }

        @Deprecated
        public String toString() {
            return this.languageScores + "\n\t" + this.scriptScores + "\n\t" + this.regionScores;
        }

        @Deprecated
        public double match(ULocale a2, ULocale aMax, ULocale b2, ULocale bMax) {
            double diff = 0.0;
            if ((diff += this.languageScores.getScore(aMax, a2.getLanguage(), aMax.getLanguage(), bMax, b2.getLanguage(), bMax.getLanguage())) > 0.999) {
                return 0.0;
            }
            diff += this.scriptScores.getScore(aMax, a2.getScript(), aMax.getScript(), bMax, b2.getScript(), bMax.getScript());
            diff += this.regionScores.getScore(aMax, a2.getCountry(), aMax.getCountry(), bMax, b2.getCountry(), bMax.getCountry());
            if (!a2.getVariant().equals(b2.getVariant())) {
                diff += 0.01;
            }
            if (diff < 0.0) {
                diff = 0.0;
            } else if (diff > 1.0) {
                diff = 1.0;
            }
            return 1.0 - diff;
        }

        @Deprecated
        public LanguageMatcherData addDistance(String desired, String supported, int percent, String comment) {
            return this.addDistance(desired, supported, percent, false, comment);
        }

        @Deprecated
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
                throw new IllegalArgumentException("Lengths unequal: " + desired + ", " + supported);
            }
            Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> data = Row.of(desiredMatcher, supportedMatcher, score);
            Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> data2 = oneway ? null : Row.of(supportedMatcher, desiredMatcher, score);
            boolean desiredEqualsSupported = desiredMatcher.equals(supportedMatcher);
            switch (desiredLen) {
                case language: {
                    String dlanguage = desiredMatcher.getLanguage();
                    String slanguage = supportedMatcher.getLanguage();
                    this.languageScores.addDataToScores(dlanguage, slanguage, data);
                    if (oneway || desiredEqualsSupported) break;
                    this.languageScores.addDataToScores(slanguage, dlanguage, data2);
                    break;
                }
                case script: {
                    String dscript = desiredMatcher.getScript();
                    String sscript = supportedMatcher.getScript();
                    this.scriptScores.addDataToScores(dscript, sscript, data);
                    if (oneway || desiredEqualsSupported) break;
                    this.scriptScores.addDataToScores(sscript, dscript, data2);
                    break;
                }
                case region: {
                    String dregion = desiredMatcher.getRegion();
                    String sregion = supportedMatcher.getRegion();
                    this.regionScores.addDataToScores(dregion, sregion, data);
                    if (oneway || desiredEqualsSupported) break;
                    this.regionScores.addDataToScores(sregion, dregion, data2);
                }
            }
            return this;
        }

        @Override
        @Deprecated
        public LanguageMatcherData cloneAsThawed() {
            try {
                LanguageMatcherData result = (LanguageMatcherData)this.clone();
                result.languageScores = this.languageScores.cloneAsThawed();
                result.scriptScores = this.scriptScores.cloneAsThawed();
                result.regionScores = this.regionScores.cloneAsThawed();
                result.frozen = false;
                return result;
            }
            catch (CloneNotSupportedException e2) {
                throw new ICUCloneNotSupportedException(e2);
            }
        }

        @Override
        @Deprecated
        public LanguageMatcherData freeze() {
            this.languageScores.freeze();
            this.regionScores.freeze();
            this.scriptScores.freeze();
            this.matchingLanguages = this.languageScores.getMatchingLanguages();
            this.frozen = true;
            return this;
        }

        @Override
        @Deprecated
        public boolean isFrozen() {
            return this.frozen;
        }
    }

    private static class ScoreData
    implements Freezable<ScoreData> {
        private static final double maxUnequal_changeD_sameS = 0.5;
        private static final double maxUnequal_changeEqual = 0.75;
        LinkedHashSet<Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double>> scores = new LinkedHashSet();
        final Level level;
        private volatile boolean frozen = false;

        public ScoreData(Level level) {
            this.level = level;
        }

        void addDataToScores(String desired, String supported, Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> data) {
            boolean added = this.scores.add(data);
            if (!added) {
                throw new ICUException("trying to add duplicate data: " + data);
            }
        }

        double getScore(ULocale dMax, String desiredRaw, String desiredMax, ULocale sMax, String supportedRaw, String supportedMax) {
            double distance = 0.0;
            if (!desiredMax.equals(supportedMax)) {
                distance = this.getRawScore(dMax, sMax);
            } else if (!desiredRaw.equals(supportedRaw)) {
                distance += 0.001;
            }
            return distance;
        }

        private double getRawScore(ULocale desiredLocale, ULocale supportedLocale) {
            for (Row.R3 r3 : this.scores) {
                if (!((LocalePatternMatcher)r3.get0()).matches(desiredLocale) || !((LocalePatternMatcher)r3.get1()).matches(supportedLocale)) continue;
                return (Double)r3.get2();
            }
            return this.level.worst;
        }

        public String toString() {
            StringBuilder result = new StringBuilder().append((Object)this.level);
            for (Row.R3 r3 : this.scores) {
                result.append("\n\t\t").append(r3);
            }
            return result.toString();
        }

        @Override
        public ScoreData cloneAsThawed() {
            try {
                ScoreData result = (ScoreData)this.clone();
                result.scores = (LinkedHashSet)result.scores.clone();
                result.frozen = false;
                return result;
            }
            catch (CloneNotSupportedException e2) {
                throw new ICUCloneNotSupportedException(e2);
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

        public Relation<String, String> getMatchingLanguages() {
            Relation<String, String> desiredToSupported = Relation.of(new LinkedHashMap(), HashSet.class);
            for (Row.R3 r3 : this.scores) {
                LocalePatternMatcher desired = (LocalePatternMatcher)r3.get0();
                LocalePatternMatcher supported = (LocalePatternMatcher)r3.get1();
                if (desired.lang == null || supported.lang == null) continue;
                desiredToSupported.put(desired.lang, supported.lang);
            }
            desiredToSupported.freeze();
            return desiredToSupported;
        }
    }

    static enum Level {
        language(0.99),
        script(0.2),
        region(0.04);

        final double worst;

        private Level(double d2) {
            this.worst = d2;
        }
    }

    private static class LocalePatternMatcher {
        private String lang;
        private String script;
        private String region;
        private Level level;
        static Pattern pattern = Pattern.compile("([a-z]{1,8}|\\*)(?:[_-]([A-Z][a-z]{3}|\\*))?(?:[_-]([A-Z]{2}|[0-9]{3}|\\*))?");

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

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !(obj instanceof LocalePatternMatcher)) {
                return false;
            }
            LocalePatternMatcher other = (LocalePatternMatcher)obj;
            return Utility.objectEquals((Object)this.level, (Object)other.level) && Utility.objectEquals(this.lang, other.lang) && Utility.objectEquals(this.script, other.script) && Utility.objectEquals(this.region, other.region);
        }

        public int hashCode() {
            return this.level.ordinal() ^ (this.lang == null ? 0 : this.lang.hashCode()) ^ (this.script == null ? 0 : this.script.hashCode()) ^ (this.region == null ? 0 : this.region.hashCode());
        }
    }

    @Deprecated
    private static class OutputDouble {
        double value;

        private OutputDouble() {
        }
    }
}

