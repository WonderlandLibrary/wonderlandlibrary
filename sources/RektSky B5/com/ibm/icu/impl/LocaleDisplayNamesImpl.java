/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUResourceTableAccess;
import com.ibm.icu.impl.SimpleFormatterImpl;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.impl.locale.AsciiUtil;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.CaseMap;
import com.ibm.icu.text.DisplayContext;
import com.ibm.icu.text.LocaleDisplayNames;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

public class LocaleDisplayNamesImpl
extends LocaleDisplayNames {
    private final ULocale locale;
    private final LocaleDisplayNames.DialectHandling dialectHandling;
    private final DisplayContext capitalization;
    private final DisplayContext nameLength;
    private final DisplayContext substituteHandling;
    private final DataTable langData;
    private final DataTable regionData;
    private final String separatorFormat;
    private final String format;
    private final String keyTypeFormat;
    private final char formatOpenParen;
    private final char formatReplaceOpenParen;
    private final char formatCloseParen;
    private final char formatReplaceCloseParen;
    private final CurrencyData.CurrencyDisplayInfo currencyDisplayInfo;
    private static final Cache cache = new Cache();
    private boolean[] capitalizationUsage = null;
    private static final Map<String, CapitalizationContextUsage> contextUsageTypeMap = new HashMap<String, CapitalizationContextUsage>();
    private transient BreakIterator capitalizationBrkIter = null;
    private static final CaseMap.Title TO_TITLE_WHOLE_STRING_NO_LOWERCASE;

    private static String toTitleWholeStringNoLowercase(ULocale locale, String s2) {
        return TO_TITLE_WHOLE_STRING_NO_LOWERCASE.apply(locale.toLocale(), null, s2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static LocaleDisplayNames getInstance(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling) {
        Cache cache = LocaleDisplayNamesImpl.cache;
        synchronized (cache) {
            return LocaleDisplayNamesImpl.cache.get(locale, dialectHandling);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static LocaleDisplayNames getInstance(ULocale locale, DisplayContext ... contexts) {
        Cache cache = LocaleDisplayNamesImpl.cache;
        synchronized (cache) {
            return LocaleDisplayNamesImpl.cache.get(locale, contexts);
        }
    }

    public LocaleDisplayNamesImpl(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling) {
        this(locale, dialectHandling == LocaleDisplayNames.DialectHandling.STANDARD_NAMES ? DisplayContext.STANDARD_NAMES : DisplayContext.DIALECT_NAMES, DisplayContext.CAPITALIZATION_NONE);
    }

    public LocaleDisplayNamesImpl(ULocale locale, DisplayContext ... contexts) {
        LocaleDisplayNames.DialectHandling dialectHandling = LocaleDisplayNames.DialectHandling.STANDARD_NAMES;
        DisplayContext capitalization = DisplayContext.CAPITALIZATION_NONE;
        DisplayContext nameLength = DisplayContext.LENGTH_FULL;
        DisplayContext substituteHandling = DisplayContext.SUBSTITUTE;
        block8: for (DisplayContext contextItem : contexts) {
            switch (contextItem.type()) {
                case DIALECT_HANDLING: {
                    dialectHandling = contextItem.value() == DisplayContext.STANDARD_NAMES.value() ? LocaleDisplayNames.DialectHandling.STANDARD_NAMES : LocaleDisplayNames.DialectHandling.DIALECT_NAMES;
                    continue block8;
                }
                case CAPITALIZATION: {
                    capitalization = contextItem;
                    continue block8;
                }
                case DISPLAY_LENGTH: {
                    nameLength = contextItem;
                    continue block8;
                }
                case SUBSTITUTE_HANDLING: {
                    substituteHandling = contextItem;
                    continue block8;
                }
            }
        }
        this.dialectHandling = dialectHandling;
        this.capitalization = capitalization;
        this.nameLength = nameLength;
        this.substituteHandling = substituteHandling;
        this.langData = LangDataTables.impl.get(locale, substituteHandling == DisplayContext.NO_SUBSTITUTE);
        this.regionData = RegionDataTables.impl.get(locale, substituteHandling == DisplayContext.NO_SUBSTITUTE);
        this.locale = ULocale.ROOT.equals(this.langData.getLocale()) ? this.regionData.getLocale() : this.langData.getLocale();
        String sep = this.langData.get("localeDisplayPattern", "separator");
        if (sep == null || "separator".equals(sep)) {
            sep = "{0}, {1}";
        }
        StringBuilder sb = new StringBuilder();
        this.separatorFormat = SimpleFormatterImpl.compileToStringMinMaxArguments(sep, sb, 2, 2);
        String pattern = this.langData.get("localeDisplayPattern", "pattern");
        if (pattern == null || "pattern".equals(pattern)) {
            pattern = "{0} ({1})";
        }
        this.format = SimpleFormatterImpl.compileToStringMinMaxArguments(pattern, sb, 2, 2);
        if (pattern.contains("\uff08")) {
            this.formatOpenParen = (char)65288;
            this.formatCloseParen = (char)65289;
            this.formatReplaceOpenParen = (char)65339;
            this.formatReplaceCloseParen = (char)65341;
        } else {
            this.formatOpenParen = (char)40;
            this.formatCloseParen = (char)41;
            this.formatReplaceOpenParen = (char)91;
            this.formatReplaceCloseParen = (char)93;
        }
        String keyTypePattern = this.langData.get("localeDisplayPattern", "keyTypePattern");
        if (keyTypePattern == null || "keyTypePattern".equals(keyTypePattern)) {
            keyTypePattern = "{0}={1}";
        }
        this.keyTypeFormat = SimpleFormatterImpl.compileToStringMinMaxArguments(keyTypePattern, sb, 2, 2);
        boolean needBrkIter = false;
        if (capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU || capitalization == DisplayContext.CAPITALIZATION_FOR_STANDALONE) {
            this.capitalizationUsage = new boolean[CapitalizationContextUsage.values().length];
            ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", locale);
            CapitalizationContextSink sink = new CapitalizationContextSink();
            try {
                rb.getAllItemsWithFallback("contextTransforms", sink);
            }
            catch (MissingResourceException missingResourceException) {
                // empty catch block
            }
            needBrkIter = sink.hasCapitalizationUsage;
        }
        if (needBrkIter || capitalization == DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE) {
            this.capitalizationBrkIter = BreakIterator.getSentenceInstance(locale);
        }
        this.currencyDisplayInfo = CurrencyData.provider.getInstance(locale, false);
    }

    @Override
    public ULocale getLocale() {
        return this.locale;
    }

    @Override
    public LocaleDisplayNames.DialectHandling getDialectHandling() {
        return this.dialectHandling;
    }

    @Override
    public DisplayContext getContext(DisplayContext.Type type) {
        DisplayContext result;
        switch (type) {
            case DIALECT_HANDLING: {
                result = this.dialectHandling == LocaleDisplayNames.DialectHandling.STANDARD_NAMES ? DisplayContext.STANDARD_NAMES : DisplayContext.DIALECT_NAMES;
                break;
            }
            case CAPITALIZATION: {
                result = this.capitalization;
                break;
            }
            case DISPLAY_LENGTH: {
                result = this.nameLength;
                break;
            }
            case SUBSTITUTE_HANDLING: {
                result = this.substituteHandling;
                break;
            }
            default: {
                result = DisplayContext.STANDARD_NAMES;
            }
        }
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private String adjustForUsageAndContext(CapitalizationContextUsage usage, String name) {
        if (name != null && name.length() > 0 && UCharacter.isLowerCase(name.codePointAt(0)) && (this.capitalization == DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE || this.capitalizationUsage != null && this.capitalizationUsage[usage.ordinal()])) {
            LocaleDisplayNamesImpl localeDisplayNamesImpl = this;
            synchronized (localeDisplayNamesImpl) {
                if (this.capitalizationBrkIter == null) {
                    this.capitalizationBrkIter = BreakIterator.getSentenceInstance(this.locale);
                }
                return UCharacter.toTitleCase(this.locale, name, this.capitalizationBrkIter, 768);
            }
        }
        return name;
    }

    @Override
    public String localeDisplayName(ULocale locale) {
        return this.localeDisplayNameInternal(locale);
    }

    @Override
    public String localeDisplayName(Locale locale) {
        return this.localeDisplayNameInternal(ULocale.forLocale(locale));
    }

    @Override
    public String localeDisplayName(String localeId) {
        return this.localeDisplayNameInternal(new ULocale(localeId));
    }

    private String localeDisplayNameInternal(ULocale locale) {
        Iterator<String> keys;
        String result;
        boolean hasVariant;
        String resultName = null;
        String lang = locale.getLanguage();
        if (locale.getBaseName().length() == 0) {
            lang = "root";
        }
        String script = locale.getScript();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        boolean hasScript = script.length() > 0;
        boolean hasCountry = country.length() > 0;
        boolean bl = hasVariant = variant.length() > 0;
        if (this.dialectHandling == LocaleDisplayNames.DialectHandling.DIALECT_NAMES) {
            String langCountry;
            String langScript;
            String langScriptCountry;
            if (hasScript && hasCountry && (result = this.localeIdName(langScriptCountry = lang + '_' + script + '_' + country)) != null && !result.equals(langScriptCountry)) {
                resultName = result;
                hasScript = false;
                hasCountry = false;
            } else if (hasScript && (result = this.localeIdName(langScript = lang + '_' + script)) != null && !result.equals(langScript)) {
                resultName = result;
                hasScript = false;
            } else if (hasCountry && (result = this.localeIdName(langCountry = lang + '_' + country)) != null && !result.equals(langCountry)) {
                resultName = result;
                hasCountry = false;
            }
        }
        if (resultName == null) {
            String result2 = this.localeIdName(lang);
            if (result2 == null) {
                return null;
            }
            resultName = result2.replace(this.formatOpenParen, this.formatReplaceOpenParen).replace(this.formatCloseParen, this.formatReplaceCloseParen);
        }
        StringBuilder buf = new StringBuilder();
        if (hasScript) {
            result = this.scriptDisplayNameInContext(script, true);
            if (result == null) {
                return null;
            }
            buf.append(result.replace(this.formatOpenParen, this.formatReplaceOpenParen).replace(this.formatCloseParen, this.formatReplaceCloseParen));
        }
        if (hasCountry) {
            result = this.regionDisplayName(country, true);
            if (result == null) {
                return null;
            }
            this.appendWithSep(result.replace(this.formatOpenParen, this.formatReplaceOpenParen).replace(this.formatCloseParen, this.formatReplaceCloseParen), buf);
        }
        if (hasVariant) {
            result = this.variantDisplayName(variant, true);
            if (result == null) {
                return null;
            }
            this.appendWithSep(result.replace(this.formatOpenParen, this.formatReplaceOpenParen).replace(this.formatCloseParen, this.formatReplaceCloseParen), buf);
        }
        if ((keys = locale.getKeywords()) != null) {
            while (keys.hasNext()) {
                String key = keys.next();
                String value = locale.getKeywordValue(key);
                String keyDisplayName = this.keyDisplayName(key, true);
                if (keyDisplayName == null) {
                    return null;
                }
                keyDisplayName = keyDisplayName.replace(this.formatOpenParen, this.formatReplaceOpenParen).replace(this.formatCloseParen, this.formatReplaceCloseParen);
                String valueDisplayName = this.keyValueDisplayName(key, value, true);
                if (valueDisplayName == null) {
                    return null;
                }
                if (!(valueDisplayName = valueDisplayName.replace(this.formatOpenParen, this.formatReplaceOpenParen).replace(this.formatCloseParen, this.formatReplaceCloseParen)).equals(value)) {
                    this.appendWithSep(valueDisplayName, buf);
                    continue;
                }
                if (!key.equals(keyDisplayName)) {
                    String keyValue = SimpleFormatterImpl.formatCompiledPattern(this.keyTypeFormat, keyDisplayName, valueDisplayName);
                    this.appendWithSep(keyValue, buf);
                    continue;
                }
                this.appendWithSep(keyDisplayName, buf).append("=").append(valueDisplayName);
            }
        }
        String resultRemainder = null;
        if (buf.length() > 0) {
            resultRemainder = buf.toString();
        }
        if (resultRemainder != null) {
            resultName = SimpleFormatterImpl.formatCompiledPattern(this.format, resultName, resultRemainder);
        }
        return this.adjustForUsageAndContext(CapitalizationContextUsage.LANGUAGE, resultName);
    }

    private String localeIdName(String localeId) {
        String locIdName;
        if (this.nameLength == DisplayContext.LENGTH_SHORT && (locIdName = this.langData.get("Languages%short", localeId)) != null && !locIdName.equals(localeId)) {
            return locIdName;
        }
        return this.langData.get("Languages", localeId);
    }

    @Override
    public String languageDisplayName(String lang) {
        String langName;
        if (lang.equals("root") || lang.indexOf(95) != -1) {
            return this.substituteHandling == DisplayContext.SUBSTITUTE ? lang : null;
        }
        if (this.nameLength == DisplayContext.LENGTH_SHORT && (langName = this.langData.get("Languages%short", lang)) != null && !langName.equals(lang)) {
            return this.adjustForUsageAndContext(CapitalizationContextUsage.LANGUAGE, langName);
        }
        return this.adjustForUsageAndContext(CapitalizationContextUsage.LANGUAGE, this.langData.get("Languages", lang));
    }

    @Override
    public String scriptDisplayName(String script) {
        String str = this.langData.get("Scripts%stand-alone", script);
        if (str == null || str.equals(script)) {
            if (this.nameLength == DisplayContext.LENGTH_SHORT && (str = this.langData.get("Scripts%short", script)) != null && !str.equals(script)) {
                return this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, str);
            }
            str = this.langData.get("Scripts", script);
        }
        return this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, str);
    }

    private String scriptDisplayNameInContext(String script, boolean skipAdjust) {
        String scriptName;
        if (this.nameLength == DisplayContext.LENGTH_SHORT && (scriptName = this.langData.get("Scripts%short", script)) != null && !scriptName.equals(script)) {
            return skipAdjust ? scriptName : this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, scriptName);
        }
        scriptName = this.langData.get("Scripts", script);
        return skipAdjust ? scriptName : this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, scriptName);
    }

    @Override
    public String scriptDisplayNameInContext(String script) {
        return this.scriptDisplayNameInContext(script, false);
    }

    @Override
    public String scriptDisplayName(int scriptCode) {
        return this.scriptDisplayName(UScript.getShortName(scriptCode));
    }

    private String regionDisplayName(String region, boolean skipAdjust) {
        String regionName;
        if (this.nameLength == DisplayContext.LENGTH_SHORT && (regionName = this.regionData.get("Countries%short", region)) != null && !regionName.equals(region)) {
            return skipAdjust ? regionName : this.adjustForUsageAndContext(CapitalizationContextUsage.TERRITORY, regionName);
        }
        regionName = this.regionData.get("Countries", region);
        return skipAdjust ? regionName : this.adjustForUsageAndContext(CapitalizationContextUsage.TERRITORY, regionName);
    }

    @Override
    public String regionDisplayName(String region) {
        return this.regionDisplayName(region, false);
    }

    private String variantDisplayName(String variant, boolean skipAdjust) {
        String variantName = this.langData.get("Variants", variant);
        return skipAdjust ? variantName : this.adjustForUsageAndContext(CapitalizationContextUsage.VARIANT, variantName);
    }

    @Override
    public String variantDisplayName(String variant) {
        return this.variantDisplayName(variant, false);
    }

    private String keyDisplayName(String key, boolean skipAdjust) {
        String keyName = this.langData.get("Keys", key);
        return skipAdjust ? keyName : this.adjustForUsageAndContext(CapitalizationContextUsage.KEY, keyName);
    }

    @Override
    public String keyDisplayName(String key) {
        return this.keyDisplayName(key, false);
    }

    private String keyValueDisplayName(String key, String value, boolean skipAdjust) {
        String keyValueName = null;
        if (key.equals("currency")) {
            keyValueName = this.currencyDisplayInfo.getName(AsciiUtil.toUpperString(value));
            if (keyValueName == null) {
                keyValueName = value;
            }
        } else {
            String tmp;
            if (this.nameLength == DisplayContext.LENGTH_SHORT && (tmp = this.langData.get("Types%short", key, value)) != null && !tmp.equals(value)) {
                keyValueName = tmp;
            }
            if (keyValueName == null) {
                keyValueName = this.langData.get("Types", key, value);
            }
        }
        return skipAdjust ? keyValueName : this.adjustForUsageAndContext(CapitalizationContextUsage.KEYVALUE, keyValueName);
    }

    @Override
    public String keyValueDisplayName(String key, String value) {
        return this.keyValueDisplayName(key, value, false);
    }

    @Override
    public List<LocaleDisplayNames.UiListItem> getUiListCompareWholeItems(Set<ULocale> localeSet, Comparator<LocaleDisplayNames.UiListItem> comparator) {
        DisplayContext capContext = this.getContext(DisplayContext.Type.CAPITALIZATION);
        ArrayList<LocaleDisplayNames.UiListItem> result = new ArrayList<LocaleDisplayNames.UiListItem>();
        HashMap<ULocale, HashSet<ULocale>> baseToLocales = new HashMap<ULocale, HashSet<ULocale>>();
        ULocale.Builder builder = new ULocale.Builder();
        for (ULocale uLocale : localeSet) {
            builder.setLocale(uLocale);
            ULocale loc = ULocale.addLikelySubtags(uLocale);
            ULocale base = new ULocale(loc.getLanguage());
            HashSet<ULocale> locales = (HashSet<ULocale>)baseToLocales.get(base);
            if (locales == null) {
                locales = new HashSet<ULocale>();
                baseToLocales.put(base, locales);
            }
            locales.add(loc);
        }
        for (Map.Entry entry : baseToLocales.entrySet()) {
            ULocale base = (ULocale)entry.getKey();
            Set values = (Set)entry.getValue();
            if (values.size() == 1) {
                ULocale locale = (ULocale)values.iterator().next();
                result.add(this.newRow(ULocale.minimizeSubtags(locale, ULocale.Minimize.FAVOR_SCRIPT), capContext));
                continue;
            }
            HashSet<String> scripts = new HashSet<String>();
            HashSet<String> regions = new HashSet<String>();
            ULocale maxBase = ULocale.addLikelySubtags(base);
            scripts.add(maxBase.getScript());
            regions.add(maxBase.getCountry());
            for (ULocale locale : values) {
                scripts.add(locale.getScript());
                regions.add(locale.getCountry());
            }
            boolean hasScripts = scripts.size() > 1;
            boolean hasRegions = regions.size() > 1;
            for (ULocale locale : values) {
                ULocale.Builder modified = builder.setLocale(locale);
                if (!hasScripts) {
                    modified.setScript("");
                }
                if (!hasRegions) {
                    modified.setRegion("");
                }
                result.add(this.newRow(modified.build(), capContext));
            }
        }
        Collections.sort(result, comparator);
        return result;
    }

    private LocaleDisplayNames.UiListItem newRow(ULocale modified, DisplayContext capContext) {
        ULocale minimized = ULocale.minimizeSubtags(modified, ULocale.Minimize.FAVOR_SCRIPT);
        String tempName = modified.getDisplayName(this.locale);
        boolean titlecase = capContext == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU;
        String nameInDisplayLocale = titlecase ? LocaleDisplayNamesImpl.toTitleWholeStringNoLowercase(this.locale, tempName) : tempName;
        tempName = modified.getDisplayName(modified);
        String nameInSelf = capContext == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU ? LocaleDisplayNamesImpl.toTitleWholeStringNoLowercase(modified, tempName) : tempName;
        return new LocaleDisplayNames.UiListItem(minimized, modified, nameInDisplayLocale, nameInSelf);
    }

    public static boolean haveData(DataTableType type) {
        switch (type) {
            case LANG: {
                return LangDataTables.impl instanceof ICUDataTables;
            }
            case REGION: {
                return RegionDataTables.impl instanceof ICUDataTables;
            }
        }
        throw new IllegalArgumentException("unknown type: " + (Object)((Object)type));
    }

    private StringBuilder appendWithSep(String s2, StringBuilder b2) {
        if (b2.length() == 0) {
            b2.append(s2);
        } else {
            SimpleFormatterImpl.formatAndReplace(this.separatorFormat, b2, null, b2, s2);
        }
        return b2;
    }

    static {
        contextUsageTypeMap.put("languages", CapitalizationContextUsage.LANGUAGE);
        contextUsageTypeMap.put("script", CapitalizationContextUsage.SCRIPT);
        contextUsageTypeMap.put("territory", CapitalizationContextUsage.TERRITORY);
        contextUsageTypeMap.put("variant", CapitalizationContextUsage.VARIANT);
        contextUsageTypeMap.put("key", CapitalizationContextUsage.KEY);
        contextUsageTypeMap.put("keyValue", CapitalizationContextUsage.KEYVALUE);
        TO_TITLE_WHOLE_STRING_NO_LOWERCASE = CaseMap.toTitle().wholeString().noLowercase();
    }

    private static class Cache {
        private ULocale locale;
        private LocaleDisplayNames.DialectHandling dialectHandling;
        private DisplayContext capitalization;
        private DisplayContext nameLength;
        private DisplayContext substituteHandling;
        private LocaleDisplayNames cache;

        private Cache() {
        }

        public LocaleDisplayNames get(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling) {
            if (dialectHandling != this.dialectHandling || DisplayContext.CAPITALIZATION_NONE != this.capitalization || DisplayContext.LENGTH_FULL != this.nameLength || DisplayContext.SUBSTITUTE != this.substituteHandling || !locale.equals(this.locale)) {
                this.locale = locale;
                this.dialectHandling = dialectHandling;
                this.capitalization = DisplayContext.CAPITALIZATION_NONE;
                this.nameLength = DisplayContext.LENGTH_FULL;
                this.substituteHandling = DisplayContext.SUBSTITUTE;
                this.cache = new LocaleDisplayNamesImpl(locale, dialectHandling);
            }
            return this.cache;
        }

        public LocaleDisplayNames get(ULocale locale, DisplayContext ... contexts) {
            LocaleDisplayNames.DialectHandling dialectHandlingIn = LocaleDisplayNames.DialectHandling.STANDARD_NAMES;
            DisplayContext capitalizationIn = DisplayContext.CAPITALIZATION_NONE;
            DisplayContext nameLengthIn = DisplayContext.LENGTH_FULL;
            DisplayContext substituteHandling = DisplayContext.SUBSTITUTE;
            block6: for (DisplayContext contextItem : contexts) {
                switch (contextItem.type()) {
                    case DIALECT_HANDLING: {
                        dialectHandlingIn = contextItem.value() == DisplayContext.STANDARD_NAMES.value() ? LocaleDisplayNames.DialectHandling.STANDARD_NAMES : LocaleDisplayNames.DialectHandling.DIALECT_NAMES;
                        continue block6;
                    }
                    case CAPITALIZATION: {
                        capitalizationIn = contextItem;
                        continue block6;
                    }
                    case DISPLAY_LENGTH: {
                        nameLengthIn = contextItem;
                        continue block6;
                    }
                    case SUBSTITUTE_HANDLING: {
                        substituteHandling = contextItem;
                        continue block6;
                    }
                }
            }
            if (dialectHandlingIn != this.dialectHandling || capitalizationIn != this.capitalization || nameLengthIn != this.nameLength || substituteHandling != this.substituteHandling || !locale.equals(this.locale)) {
                this.locale = locale;
                this.dialectHandling = dialectHandlingIn;
                this.capitalization = capitalizationIn;
                this.nameLength = nameLengthIn;
                this.substituteHandling = substituteHandling;
                this.cache = new LocaleDisplayNamesImpl(locale, contexts);
            }
            return this.cache;
        }
    }

    public static enum DataTableType {
        LANG,
        REGION;

    }

    static class RegionDataTables {
        static final DataTables impl = DataTables.load("com.ibm.icu.impl.ICURegionDataTables");

        RegionDataTables() {
        }
    }

    static class LangDataTables {
        static final DataTables impl = DataTables.load("com.ibm.icu.impl.ICULangDataTables");

        LangDataTables() {
        }
    }

    static abstract class ICUDataTables
    extends DataTables {
        private final String path;

        protected ICUDataTables(String path) {
            this.path = path;
        }

        @Override
        public DataTable get(ULocale locale, boolean nullIfNotFound) {
            return new ICUDataTable(this.path, locale, nullIfNotFound);
        }
    }

    static abstract class DataTables {
        DataTables() {
        }

        public abstract DataTable get(ULocale var1, boolean var2);

        public static DataTables load(String className) {
            try {
                return (DataTables)Class.forName(className).newInstance();
            }
            catch (Throwable t2) {
                return new DataTables(){

                    @Override
                    public DataTable get(ULocale locale, boolean nullIfNotFound) {
                        return new DataTable(nullIfNotFound);
                    }
                };
            }
        }
    }

    static class ICUDataTable
    extends DataTable {
        private final ICUResourceBundle bundle;

        public ICUDataTable(String path, ULocale locale, boolean nullIfNotFound) {
            super(nullIfNotFound);
            this.bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(path, locale.getBaseName());
        }

        @Override
        public ULocale getLocale() {
            return this.bundle.getULocale();
        }

        @Override
        public String get(String tableName, String subTableName, String code) {
            return ICUResourceTableAccess.getTableString(this.bundle, tableName, subTableName, code, this.nullIfNotFound ? null : code);
        }
    }

    public static class DataTable {
        final boolean nullIfNotFound;

        DataTable(boolean nullIfNotFound) {
            this.nullIfNotFound = nullIfNotFound;
        }

        ULocale getLocale() {
            return ULocale.ROOT;
        }

        String get(String tableName, String code) {
            return this.get(tableName, null, code);
        }

        String get(String tableName, String subTableName, String code) {
            return this.nullIfNotFound ? null : code;
        }
    }

    private final class CapitalizationContextSink
    extends UResource.Sink {
        boolean hasCapitalizationUsage = false;

        private CapitalizationContextSink() {
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table contextsTable = value.getTable();
            int i2 = 0;
            while (contextsTable.getKeyAndValue(i2, key, value)) {
                int[] intVector;
                CapitalizationContextUsage usage = (CapitalizationContextUsage)((Object)contextUsageTypeMap.get(key.toString()));
                if (usage != null && (intVector = value.getIntVector()).length >= 2) {
                    int titlecaseInt;
                    int n2 = titlecaseInt = LocaleDisplayNamesImpl.this.capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU ? intVector[0] : intVector[1];
                    if (titlecaseInt != 0) {
                        ((LocaleDisplayNamesImpl)LocaleDisplayNamesImpl.this).capitalizationUsage[usage.ordinal()] = true;
                        this.hasCapitalizationUsage = true;
                    }
                }
                ++i2;
            }
        }
    }

    private static enum CapitalizationContextUsage {
        LANGUAGE,
        SCRIPT,
        TERRITORY,
        VARIANT,
        KEY,
        KEYVALUE;

    }
}

