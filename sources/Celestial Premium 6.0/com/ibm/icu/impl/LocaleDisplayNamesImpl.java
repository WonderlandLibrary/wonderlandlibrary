/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUResourceTableAccess;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.DisplayContext;
import com.ibm.icu.text.LocaleDisplayNames;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

public class LocaleDisplayNamesImpl
extends LocaleDisplayNames {
    private final ULocale locale;
    private final LocaleDisplayNames.DialectHandling dialectHandling;
    private final DisplayContext capitalization;
    private final DataTable langData;
    private final DataTable regionData;
    private final Appender appender;
    private final MessageFormat format;
    private final MessageFormat keyTypeFormat;
    private static final Cache cache = new Cache();
    private Map<CapitalizationContextUsage, boolean[]> capitalizationUsage = null;
    private static final Map<String, CapitalizationContextUsage> contextUsageTypeMap = new HashMap<String, CapitalizationContextUsage>();

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
        block6: for (DisplayContext contextItem : contexts) {
            switch (contextItem.type()) {
                case DIALECT_HANDLING: {
                    dialectHandling = contextItem.value() == DisplayContext.STANDARD_NAMES.value() ? LocaleDisplayNames.DialectHandling.STANDARD_NAMES : LocaleDisplayNames.DialectHandling.DIALECT_NAMES;
                    continue block6;
                }
                case CAPITALIZATION: {
                    capitalization = contextItem;
                    continue block6;
                }
            }
        }
        this.dialectHandling = dialectHandling;
        this.capitalization = capitalization;
        this.langData = LangDataTables.impl.get(locale);
        this.regionData = RegionDataTables.impl.get(locale);
        this.locale = ULocale.ROOT.equals(this.langData.getLocale()) ? this.regionData.getLocale() : this.langData.getLocale();
        String sep = this.langData.get("localeDisplayPattern", "separator");
        if ("separator".equals(sep)) {
            sep = ", ";
        }
        this.appender = new Appender(sep);
        String pattern = this.langData.get("localeDisplayPattern", "pattern");
        if ("pattern".equals(pattern)) {
            pattern = "{0} ({1})";
        }
        this.format = new MessageFormat(pattern);
        String keyTypePattern = this.langData.get("localeDisplayPattern", "keyTypePattern");
        if ("keyTypePattern".equals(keyTypePattern)) {
            keyTypePattern = "{0}={1}";
        }
        this.keyTypeFormat = new MessageFormat(keyTypePattern);
        if (capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU || capitalization == DisplayContext.CAPITALIZATION_FOR_STANDALONE) {
            CapitalizationContextUsage[] allUsages;
            this.capitalizationUsage = new HashMap<CapitalizationContextUsage, boolean[]>();
            boolean[] noTransforms = new boolean[]{false, false};
            for (CapitalizationContextUsage usage : allUsages = CapitalizationContextUsage.values()) {
                this.capitalizationUsage.put(usage, noTransforms);
            }
            ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale);
            ICUResourceBundle contextTransformsBundle = null;
            try {
                contextTransformsBundle = rb.getWithFallback("contextTransforms");
            }
            catch (MissingResourceException e) {
                contextTransformsBundle = null;
            }
            if (contextTransformsBundle != null) {
                UResourceBundleIterator ctIterator = contextTransformsBundle.getIterator();
                while (ctIterator.hasNext()) {
                    String usageKey;
                    CapitalizationContextUsage usage;
                    UResourceBundle contextTransformUsage = ctIterator.next();
                    int[] intVector = contextTransformUsage.getIntVector();
                    if (intVector.length < 2 || (usage = contextUsageTypeMap.get(usageKey = contextTransformUsage.getKey())) == null) continue;
                    boolean[] transforms = new boolean[]{intVector[0] != 0, intVector[1] != 0};
                    this.capitalizationUsage.put(usage, transforms);
                }
            }
        }
    }

    public ULocale getLocale() {
        return this.locale;
    }

    public LocaleDisplayNames.DialectHandling getDialectHandling() {
        return this.dialectHandling;
    }

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
            default: {
                result = DisplayContext.STANDARD_NAMES;
            }
        }
        return result;
    }

    private String adjustForUsageAndContext(CapitalizationContextUsage usage, String name) {
        String result = name;
        boolean titlecase = false;
        switch (this.capitalization) {
            case CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE: {
                titlecase = true;
                break;
            }
            case CAPITALIZATION_FOR_UI_LIST_OR_MENU: 
            case CAPITALIZATION_FOR_STANDALONE: {
                if (this.capitalizationUsage == null) break;
                boolean[] transforms = this.capitalizationUsage.get((Object)usage);
                titlecase = this.capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU ? transforms[0] : transforms[1];
                break;
            }
        }
        if (titlecase) {
            int ch;
            int stopPos;
            int stopPosLimit = 8;
            int len = name.length();
            if (stopPosLimit > len) {
                stopPosLimit = len;
            }
            for (stopPos = 0; !(stopPos >= stopPosLimit || (ch = name.codePointAt(stopPos)) < 65 || ch > 90 && ch < 97 || ch > 122 && ch < 192); ++stopPos) {
                if (ch < 65536) continue;
                ++stopPos;
            }
            if (stopPos > 0 && stopPos < len) {
                String firstWord = name.substring(0, stopPos);
                String firstWordTitleCase = UCharacter.toTitleCase(this.locale, firstWord, null, 768);
                result = firstWordTitleCase.concat(name.substring(stopPos));
            } else {
                result = UCharacter.toTitleCase(this.locale, name, null, 768);
            }
        }
        return result;
    }

    public String localeDisplayName(ULocale locale) {
        return this.localeDisplayNameInternal(locale);
    }

    public String localeDisplayName(Locale locale) {
        return this.localeDisplayNameInternal(ULocale.forLocale(locale));
    }

    public String localeDisplayName(String localeId) {
        return this.localeDisplayNameInternal(new ULocale(localeId));
    }

    private String localeDisplayNameInternal(ULocale locale) {
        Iterator<String> keys;
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
            String result;
            if (hasScript && hasCountry && !(result = this.localeIdName(langScriptCountry = lang + '_' + script + '_' + country)).equals(langScriptCountry)) {
                resultName = result;
                hasScript = false;
                hasCountry = false;
            } else if (hasScript && !(result = this.localeIdName(langScript = lang + '_' + script)).equals(langScript)) {
                resultName = result;
                hasScript = false;
            } else if (hasCountry && !(result = this.localeIdName(langCountry = lang + '_' + country)).equals(langCountry)) {
                resultName = result;
                hasCountry = false;
            }
        }
        if (resultName == null) {
            resultName = this.localeIdName(lang);
        }
        StringBuilder buf = new StringBuilder();
        if (hasScript) {
            buf.append(this.scriptDisplayNameInContext(script));
        }
        if (hasCountry) {
            this.appender.append(this.regionDisplayName(country), buf);
        }
        if (hasVariant) {
            this.appender.append(this.variantDisplayName(variant), buf);
        }
        if ((keys = locale.getKeywords()) != null) {
            while (keys.hasNext()) {
                String key = keys.next();
                String value = locale.getKeywordValue(key);
                String keyDisplayName = this.keyDisplayName(key);
                String valueDisplayName = this.keyValueDisplayName(key, value);
                if (!valueDisplayName.equals(value)) {
                    this.appender.append(valueDisplayName, buf);
                    continue;
                }
                if (!key.equals(keyDisplayName)) {
                    String keyValue = this.keyTypeFormat.format(new String[]{keyDisplayName, valueDisplayName});
                    this.appender.append(keyValue, buf);
                    continue;
                }
                this.appender.append(keyDisplayName, buf).append("=").append(valueDisplayName);
            }
        }
        String resultRemainder = null;
        if (buf.length() > 0) {
            resultRemainder = buf.toString();
        }
        if (resultRemainder != null) {
            resultName = this.format.format(new Object[]{resultName, resultRemainder});
        }
        return this.adjustForUsageAndContext(CapitalizationContextUsage.LANGUAGE, resultName);
    }

    private String localeIdName(String localeId) {
        return this.langData.get("Languages", localeId);
    }

    public String languageDisplayName(String lang) {
        if (lang.equals("root") || lang.indexOf(95) != -1) {
            return lang;
        }
        return this.adjustForUsageAndContext(CapitalizationContextUsage.LANGUAGE, this.langData.get("Languages", lang));
    }

    public String scriptDisplayName(String script) {
        String str = this.langData.get("Scripts%stand-alone", script);
        if (str.equals(script)) {
            str = this.langData.get("Scripts", script);
        }
        return this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, str);
    }

    public String scriptDisplayNameInContext(String script) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, this.langData.get("Scripts", script));
    }

    public String scriptDisplayName(int scriptCode) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, this.scriptDisplayName(UScript.getShortName(scriptCode)));
    }

    public String regionDisplayName(String region) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.TERRITORY, this.regionData.get("Countries", region));
    }

    public String variantDisplayName(String variant) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.VARIANT, this.langData.get("Variants", variant));
    }

    public String keyDisplayName(String key) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.KEY, this.langData.get("Keys", key));
    }

    public String keyValueDisplayName(String key, String value) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.TYPE, this.langData.get("Types", key, value));
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

    static {
        contextUsageTypeMap.put("languages", CapitalizationContextUsage.LANGUAGE);
        contextUsageTypeMap.put("script", CapitalizationContextUsage.SCRIPT);
        contextUsageTypeMap.put("territory", CapitalizationContextUsage.TERRITORY);
        contextUsageTypeMap.put("variant", CapitalizationContextUsage.VARIANT);
        contextUsageTypeMap.put("key", CapitalizationContextUsage.KEY);
        contextUsageTypeMap.put("type", CapitalizationContextUsage.TYPE);
    }

    private static class Cache {
        private ULocale locale;
        private LocaleDisplayNames.DialectHandling dialectHandling;
        private DisplayContext capitalization;
        private LocaleDisplayNames cache;

        private Cache() {
        }

        public LocaleDisplayNames get(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling) {
            if (dialectHandling != this.dialectHandling || DisplayContext.CAPITALIZATION_NONE != this.capitalization || !locale.equals(this.locale)) {
                this.locale = locale;
                this.dialectHandling = dialectHandling;
                this.capitalization = DisplayContext.CAPITALIZATION_NONE;
                this.cache = new LocaleDisplayNamesImpl(locale, dialectHandling);
            }
            return this.cache;
        }

        public LocaleDisplayNames get(ULocale locale, DisplayContext ... contexts) {
            LocaleDisplayNames.DialectHandling dialectHandlingIn = LocaleDisplayNames.DialectHandling.STANDARD_NAMES;
            DisplayContext capitalizationIn = DisplayContext.CAPITALIZATION_NONE;
            block4: for (DisplayContext contextItem : contexts) {
                switch (contextItem.type()) {
                    case DIALECT_HANDLING: {
                        dialectHandlingIn = contextItem.value() == DisplayContext.STANDARD_NAMES.value() ? LocaleDisplayNames.DialectHandling.STANDARD_NAMES : LocaleDisplayNames.DialectHandling.DIALECT_NAMES;
                        continue block4;
                    }
                    case CAPITALIZATION: {
                        capitalizationIn = contextItem;
                        continue block4;
                    }
                }
            }
            if (dialectHandlingIn != this.dialectHandling || capitalizationIn != this.capitalization || !locale.equals(this.locale)) {
                this.locale = locale;
                this.dialectHandling = dialectHandlingIn;
                this.capitalization = capitalizationIn;
                this.cache = new LocaleDisplayNamesImpl(locale, contexts);
            }
            return this.cache;
        }
    }

    static class Appender {
        private final String sep;

        Appender(String sep) {
            this.sep = sep;
        }

        StringBuilder append(String s, StringBuilder b) {
            if (b.length() > 0) {
                b.append(this.sep);
            }
            b.append(s);
            return b;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
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

        public DataTable get(ULocale locale) {
            return new ICUDataTable(this.path, locale);
        }
    }

    static abstract class DataTables {
        DataTables() {
        }

        public abstract DataTable get(ULocale var1);

        public static DataTables load(String className) {
            try {
                return (DataTables)Class.forName(className).newInstance();
            }
            catch (Throwable t) {
                final DataTable NO_OP = new DataTable();
                return new DataTables(){

                    public DataTable get(ULocale locale) {
                        return NO_OP;
                    }
                };
            }
        }
    }

    static class ICUDataTable
    extends DataTable {
        private final ICUResourceBundle bundle;

        public ICUDataTable(String path, ULocale locale) {
            this.bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(path, locale.getBaseName());
        }

        public ULocale getLocale() {
            return this.bundle.getULocale();
        }

        public String get(String tableName, String subTableName, String code) {
            return ICUResourceTableAccess.getTableString(this.bundle, tableName, subTableName, code);
        }
    }

    public static class DataTable {
        ULocale getLocale() {
            return ULocale.ROOT;
        }

        String get(String tableName, String code) {
            return this.get(tableName, null, code);
        }

        String get(String tableName, String subTableName, String code) {
            return code;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static enum CapitalizationContextUsage {
        LANGUAGE,
        SCRIPT,
        TERRITORY,
        VARIANT,
        KEY,
        TYPE;

    }
}

