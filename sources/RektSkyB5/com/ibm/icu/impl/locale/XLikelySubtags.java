/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.locale;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.locale.XCldrStub;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class XLikelySubtags {
    private static final XLikelySubtags DEFAULT = new XLikelySubtags();
    final Map<String, Map<String, Map<String, LSR>>> langTable;

    public static final XLikelySubtags getDefault() {
        return DEFAULT;
    }

    public XLikelySubtags() {
        this(XLikelySubtags.getDefaultRawData(), true);
    }

    private static Map<String, String> getDefaultRawData() {
        TreeMap<String, String> rawData = new TreeMap<String, String>();
        UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "likelySubtags");
        Enumeration<String> enumer = bundle.getKeys();
        while (enumer.hasMoreElements()) {
            String key = enumer.nextElement();
            rawData.put(key, bundle.getString(key));
        }
        return rawData;
    }

    public XLikelySubtags(Map<String, String> rawData, boolean skipNoncanonical) {
        this.langTable = this.init(rawData, skipNoncanonical);
    }

    private Map<String, Map<String, Map<String, LSR>>> init(Map<String, String> rawData, boolean skipNoncanonical) {
        Maker maker = Maker.TREEMAP;
        Map result = (Map)maker.make();
        HashMap<LSR, LSR> internCache = new HashMap<LSR, LSR>();
        for (Map.Entry<String, String> sourceTarget : rawData.entrySet()) {
            LSR ltp = LSR.from(sourceTarget.getKey());
            String language = ltp.language;
            String script = ltp.script;
            String region = ltp.region;
            ltp = LSR.from(sourceTarget.getValue());
            String languageTarget = ltp.language;
            String scriptTarget = ltp.script;
            String regionTarget = ltp.region;
            this.set(result, language, script, region, languageTarget, scriptTarget, regionTarget, internCache);
            Set<String> languageAliases = LSR.LANGUAGE_ALIASES.getAliases(language);
            Set<String> regionAliases = LSR.REGION_ALIASES.getAliases(region);
            for (String languageAlias : languageAliases) {
                for (String regionAlias : regionAliases) {
                    if (languageAlias.equals(language) && regionAlias.equals(region)) continue;
                    this.set(result, languageAlias, script, regionAlias, languageTarget, scriptTarget, regionTarget, internCache);
                }
            }
        }
        this.set(result, "und", "Latn", "", "en", "Latn", "US", internCache);
        Map undScriptMap = (Map)result.get("und");
        Map undEmptyRegionMap = (Map)undScriptMap.get("");
        for (Map.Entry regionEntry : undEmptyRegionMap.entrySet()) {
            LSR value = (LSR)regionEntry.getValue();
            this.set(result, "und", value.script, value.region, value);
        }
        if (!result.containsKey("und")) {
            throw new IllegalArgumentException("failure: base");
        }
        for (Map.Entry langEntry : result.entrySet()) {
            String lang = (String)langEntry.getKey();
            Map scriptMap = (Map)langEntry.getValue();
            if (!scriptMap.containsKey("")) {
                throw new IllegalArgumentException("failure: " + lang);
            }
            for (Map.Entry scriptEntry : scriptMap.entrySet()) {
                String script = (String)scriptEntry.getKey();
                Map regionMap = (Map)scriptEntry.getValue();
                if (regionMap.containsKey("")) continue;
                throw new IllegalArgumentException("failure: " + lang + "-" + script);
            }
        }
        return result;
    }

    private void set(Map<String, Map<String, Map<String, LSR>>> langTable, String language, String script, String region, String languageTarget, String scriptTarget, String regionTarget, Map<LSR, LSR> internCache) {
        LSR newValue = new LSR(languageTarget, scriptTarget, regionTarget);
        LSR oldValue = internCache.get(newValue);
        if (oldValue == null) {
            internCache.put(newValue, newValue);
            oldValue = newValue;
        }
        this.set(langTable, language, script, region, oldValue);
    }

    private void set(Map<String, Map<String, Map<String, LSR>>> langTable, String language, String script, String region, LSR newValue) {
        Map<String, Map<String, LSR>> scriptTable = Maker.TREEMAP.getSubtable(langTable, language);
        Map<String, LSR> regionTable = Maker.TREEMAP.getSubtable(scriptTable, script);
        regionTable.put(region, newValue);
    }

    public LSR maximize(String source) {
        return this.maximize(ULocale.forLanguageTag(source));
    }

    public LSR maximize(ULocale source) {
        return this.maximize(source.getLanguage(), source.getScript(), source.getCountry());
    }

    public LSR maximize(LSR source) {
        return this.maximize(source.language, source.script, source.region);
    }

    public LSR maximize(String language, String script, String region) {
        LSR result;
        Map<String, LSR> regionTable;
        int retainOldMask = 0;
        Map<String, Map<String, LSR>> scriptTable = this.langTable.get(language);
        if (scriptTable == null) {
            retainOldMask |= 4;
            scriptTable = this.langTable.get("und");
        } else if (!language.equals("und")) {
            retainOldMask |= 4;
        }
        if (script.equals("Zzzz")) {
            script = "";
        }
        if ((regionTable = scriptTable.get(script)) == null) {
            retainOldMask |= 2;
            regionTable = scriptTable.get("");
        } else if (!script.isEmpty()) {
            retainOldMask |= 2;
        }
        if (region.equals("ZZ")) {
            region = "";
        }
        if ((result = regionTable.get(region)) == null) {
            retainOldMask |= 1;
            result = regionTable.get("");
            if (result == null) {
                return null;
            }
        } else if (!region.isEmpty()) {
            retainOldMask |= 1;
        }
        switch (retainOldMask) {
            default: {
                return result;
            }
            case 1: {
                return result.replace(null, null, region);
            }
            case 2: {
                return result.replace(null, script, null);
            }
            case 3: {
                return result.replace(null, script, region);
            }
            case 4: {
                return result.replace(language, null, null);
            }
            case 5: {
                return result.replace(language, null, region);
            }
            case 6: {
                return result.replace(language, script, null);
            }
            case 7: 
        }
        return result.replace(language, script, region);
    }

    private LSR minimizeSubtags(String languageIn, String scriptIn, String regionIn, ULocale.Minimize fieldToFavor) {
        LSR result2;
        LSR result = this.maximize(languageIn, scriptIn, regionIn);
        Map<String, Map<String, LSR>> scriptTable = this.langTable.get(result.language);
        Map<String, LSR> regionTable0 = scriptTable.get("");
        LSR value00 = regionTable0.get("");
        boolean favorRegionOk = false;
        if (result.script.equals(value00.script)) {
            if (result.region.equals(value00.region)) {
                return result.replace(null, "", "");
            }
            if (fieldToFavor == ULocale.Minimize.FAVOR_REGION) {
                return result.replace(null, "", null);
            }
            favorRegionOk = true;
        }
        if ((result2 = this.maximize(languageIn, scriptIn, "")).equals(result)) {
            return result.replace(null, null, "");
        }
        if (favorRegionOk) {
            return result.replace(null, "", null);
        }
        return result;
    }

    private static StringBuilder show(Map<?, ?> map, String indent, StringBuilder output) {
        String first = indent.isEmpty() ? "" : "\t";
        for (Map.Entry<?, ?> e2 : map.entrySet()) {
            String key = e2.getKey().toString();
            Object value = e2.getValue();
            output.append(first + (key.isEmpty() ? "\u2205" : key));
            if (value instanceof Map) {
                XLikelySubtags.show((Map)value, indent + "\t", output);
            } else {
                output.append("\t" + Utility.toString(value)).append("\n");
            }
            first = indent;
        }
        return output;
    }

    public String toString() {
        return XLikelySubtags.show(this.langTable, "", new StringBuilder()).toString();
    }

    public static class LSR {
        public final String language;
        public final String script;
        public final String region;
        public static Aliases LANGUAGE_ALIASES = new Aliases("language");
        public static Aliases REGION_ALIASES = new Aliases("territory");

        public static LSR from(String language, String script, String region) {
            return new LSR(language, script, region);
        }

        static LSR from(String languageIdentifier) {
            String[] parts = languageIdentifier.split("[-_]");
            if (parts.length < 1 || parts.length > 3) {
                throw new ICUException("too many subtags");
            }
            String lang = parts[0].toLowerCase();
            String p2 = parts.length < 2 ? "" : parts[1];
            String p3 = parts.length < 3 ? "" : parts[2];
            return p2.length() < 4 ? new LSR(lang, "", p2) : new LSR(lang, p2, p3);
        }

        public static LSR from(ULocale locale) {
            return new LSR(locale.getLanguage(), locale.getScript(), locale.getCountry());
        }

        public static LSR fromMaximalized(ULocale locale) {
            return LSR.fromMaximalized(locale.getLanguage(), locale.getScript(), locale.getCountry());
        }

        public static LSR fromMaximalized(String language, String script, String region) {
            String canonicalLanguage = LANGUAGE_ALIASES.getCanonical(language);
            String canonicalRegion = REGION_ALIASES.getCanonical(region);
            return DEFAULT.maximize(canonicalLanguage, script, canonicalRegion);
        }

        public LSR(String language, String script, String region) {
            this.language = language;
            this.script = script;
            this.region = region;
        }

        public String toString() {
            StringBuilder result = new StringBuilder(this.language);
            if (!this.script.isEmpty()) {
                result.append('-').append(this.script);
            }
            if (!this.region.isEmpty()) {
                result.append('-').append(this.region);
            }
            return result.toString();
        }

        public LSR replace(String language2, String script2, String region2) {
            if (language2 == null && script2 == null && region2 == null) {
                return this;
            }
            return new LSR(language2 == null ? this.language : language2, script2 == null ? this.script : script2, region2 == null ? this.region : region2);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (obj.getClass() != this.getClass()) return false;
            LSR other = (LSR)obj;
            if (!this.language.equals(other.language)) return false;
            if (!this.script.equals(other.script)) return false;
            if (!this.region.equals(other.region)) return false;
            return true;
        }

        public int hashCode() {
            return Utility.hash(this.language, this.script, this.region);
        }
    }

    public static class Aliases {
        final Map<String, String> toCanonical;
        final XCldrStub.Multimap<String, String> toAliases;

        public String getCanonical(String alias) {
            String canonical = this.toCanonical.get(alias);
            return canonical == null ? alias : canonical;
        }

        public Set<String> getAliases(String canonical) {
            Set<String> aliases = this.toAliases.get(canonical);
            return aliases == null ? Collections.singleton(canonical) : aliases;
        }

        public Aliases(String key) {
            UResourceBundle metadata = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "metadata", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            UResourceBundle metadataAlias = metadata.get("alias");
            UResourceBundle territoryAlias = metadataAlias.get(key);
            HashMap<String, String> toCanonical1 = new HashMap<String, String>();
            for (int i2 = 0; i2 < territoryAlias.getSize(); ++i2) {
                String aliasFirst;
                String aliasReason;
                UResourceBundle res = territoryAlias.get(i2);
                String aliasFrom = res.getKey();
                if (aliasFrom.contains("_") || (aliasReason = res.get("reason").getString()).equals("overlong")) continue;
                String aliasTo = res.get("replacement").getString();
                int spacePos = aliasTo.indexOf(32);
                String string = aliasFirst = spacePos < 0 ? aliasTo : aliasTo.substring(0, spacePos);
                if (aliasFirst.contains("_")) continue;
                toCanonical1.put(aliasFrom, aliasFirst);
            }
            if (key.equals("language")) {
                toCanonical1.put("mo", "ro");
            }
            this.toCanonical = Collections.unmodifiableMap(toCanonical1);
            this.toAliases = XCldrStub.Multimaps.invertFrom(toCanonical1, XCldrStub.HashMultimap.create());
        }
    }

    static abstract class Maker {
        static final Maker HASHMAP = new Maker(){

            public Map<Object, Object> make() {
                return new HashMap<Object, Object>();
            }
        };
        static final Maker TREEMAP = new Maker(){

            public Map<Object, Object> make() {
                return new TreeMap<Object, Object>();
            }
        };

        Maker() {
        }

        abstract <V> V make();

        public <K, V> V getSubtable(Map<K, V> langTable, K language) {
            V scriptTable = langTable.get(language);
            if (scriptTable == null) {
                scriptTable = this.make();
                langTable.put(language, scriptTable);
            }
            return scriptTable;
        }
    }
}

