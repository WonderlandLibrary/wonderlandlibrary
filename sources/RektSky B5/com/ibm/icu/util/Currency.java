/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.CacheBase;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.StaticUnicodeSets;
import com.ibm.icu.impl.TextTrieMap;
import com.ibm.icu.text.CurrencyDisplayNames;
import com.ibm.icu.text.CurrencyMetaInfo;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.MeasureUnit;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.ObjectStreamException;
import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

public class Currency
extends MeasureUnit {
    private static final long serialVersionUID = -5839973855554750484L;
    private static final boolean DEBUG = ICUDebug.enabled("currency");
    private static ICUCache<ULocale, List<TextTrieMap<CurrencyStringInfo>>> CURRENCY_NAME_CACHE = new SimpleCache<ULocale, List<TextTrieMap<CurrencyStringInfo>>>();
    public static final int SYMBOL_NAME = 0;
    public static final int LONG_NAME = 1;
    public static final int PLURAL_LONG_NAME = 2;
    public static final int NARROW_SYMBOL_NAME = 3;
    private static ServiceShim shim;
    private static final String EUR_STR = "EUR";
    private static final CacheBase<String, Currency, Void> regionCurrencyCache;
    private static final ULocale UND;
    private static final String[] EMPTY_STRING_ARRAY;
    private static final int[] POW10;
    private static SoftReference<List<String>> ALL_TENDER_CODES;
    private static SoftReference<Set<String>> ALL_CODES_AS_SET;
    private final String isoCode;

    private static ServiceShim getShim() {
        if (shim == null) {
            try {
                Class<?> cls = Class.forName("com.ibm.icu.util.CurrencyServiceShim");
                shim = (ServiceShim)cls.newInstance();
            }
            catch (Exception e2) {
                if (DEBUG) {
                    e2.printStackTrace();
                }
                throw new RuntimeException(e2.getMessage());
            }
        }
        return shim;
    }

    public static Currency getInstance(Locale locale) {
        return Currency.getInstance(ULocale.forLocale(locale));
    }

    public static Currency getInstance(ULocale locale) {
        String currency = locale.getKeywordValue("currency");
        if (currency != null) {
            return Currency.getInstance(currency);
        }
        if (shim == null) {
            return Currency.createCurrency(locale);
        }
        return shim.createInstance(locale);
    }

    public static String[] getAvailableCurrencyCodes(ULocale loc, Date d2) {
        String region = ULocale.getRegionForSupplementalData(loc, false);
        CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.onDate(d2).withRegion(region);
        List<String> list = Currency.getTenderCurrencies(filter);
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] getAvailableCurrencyCodes(Locale loc, Date d2) {
        return Currency.getAvailableCurrencyCodes(ULocale.forLocale(loc), d2);
    }

    public static Set<Currency> getAvailableCurrencies() {
        CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        List<String> list = info.currencies(CurrencyMetaInfo.CurrencyFilter.all());
        HashSet<Currency> resultSet = new HashSet<Currency>(list.size());
        for (String code : list) {
            resultSet.add(Currency.getInstance(code));
        }
        return resultSet;
    }

    static Currency createCurrency(ULocale loc) {
        String variant = loc.getVariant();
        if ("EURO".equals(variant)) {
            return Currency.getInstance(EUR_STR);
        }
        String key = ULocale.getRegionForSupplementalData(loc, false);
        if ("PREEURO".equals(variant)) {
            key = key + '-';
        }
        return regionCurrencyCache.getInstance(key, null);
    }

    private static Currency loadCurrency(String key) {
        boolean isPreEuro;
        String region;
        if (key.endsWith("-")) {
            region = key.substring(0, key.length() - 1);
            isPreEuro = true;
        } else {
            region = key;
            isPreEuro = false;
        }
        CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        List<String> list = info.currencies(CurrencyMetaInfo.CurrencyFilter.onRegion(region));
        if (!list.isEmpty()) {
            String code = list.get(0);
            if (isPreEuro && EUR_STR.equals(code)) {
                if (list.size() < 2) {
                    return null;
                }
                code = list.get(1);
            }
            return Currency.getInstance(code);
        }
        return null;
    }

    public static Currency getInstance(String theISOCode) {
        if (theISOCode == null) {
            throw new NullPointerException("The input currency code is null.");
        }
        if (!Currency.isAlpha3Code(theISOCode)) {
            throw new IllegalArgumentException("The input currency code is not 3-letter alphabetic code.");
        }
        return (Currency)MeasureUnit.internalGetInstance("currency", theISOCode.toUpperCase(Locale.ENGLISH));
    }

    private static boolean isAlpha3Code(String code) {
        if (code.length() != 3) {
            return false;
        }
        for (int i2 = 0; i2 < 3; ++i2) {
            char ch = code.charAt(i2);
            if (ch >= 'A' && (ch <= 'Z' || ch >= 'a') && ch <= 'z') continue;
            return false;
        }
        return true;
    }

    public static Currency fromJavaCurrency(java.util.Currency currency) {
        return Currency.getInstance(currency.getCurrencyCode());
    }

    public java.util.Currency toJavaCurrency() {
        return java.util.Currency.getInstance(this.getCurrencyCode());
    }

    public static Object registerInstance(Currency currency, ULocale locale) {
        return Currency.getShim().registerInstance(currency, locale);
    }

    public static boolean unregister(Object registryKey) {
        if (registryKey == null) {
            throw new IllegalArgumentException("registryKey must not be null");
        }
        if (shim == null) {
            return false;
        }
        return shim.unregister(registryKey);
    }

    public static Locale[] getAvailableLocales() {
        if (shim == null) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return shim.getAvailableLocales();
    }

    public static ULocale[] getAvailableULocales() {
        if (shim == null) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return shim.getAvailableULocales();
    }

    public static final String[] getKeywordValuesForLocale(String key, ULocale locale, boolean commonlyUsed) {
        if (!"currency".equals(key)) {
            return EMPTY_STRING_ARRAY;
        }
        if (!commonlyUsed) {
            return Currency.getAllTenderCurrencies().toArray(new String[0]);
        }
        if (UND.equals(locale)) {
            return EMPTY_STRING_ARRAY;
        }
        String prefRegion = ULocale.getRegionForSupplementalData(locale, true);
        CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.now().withRegion(prefRegion);
        List<String> result = Currency.getTenderCurrencies(filter);
        if (result.size() == 0) {
            return EMPTY_STRING_ARRAY;
        }
        return result.toArray(new String[result.size()]);
    }

    public String getCurrencyCode() {
        return this.subType;
    }

    public int getNumericCode() {
        int result = 0;
        try {
            UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "currencyNumericCodes", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            UResourceBundle codeMap = bundle.get("codeMap");
            UResourceBundle numCode = codeMap.get(this.subType);
            result = numCode.getInt();
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
        return result;
    }

    public String getSymbol() {
        return this.getSymbol(ULocale.getDefault(ULocale.Category.DISPLAY));
    }

    public String getSymbol(Locale loc) {
        return this.getSymbol(ULocale.forLocale(loc));
    }

    public String getSymbol(ULocale uloc) {
        return this.getName(uloc, 0, null);
    }

    public String getName(Locale locale, int nameStyle, boolean[] isChoiceFormat) {
        return this.getName(ULocale.forLocale(locale), nameStyle, isChoiceFormat);
    }

    public String getName(ULocale locale, int nameStyle, boolean[] isChoiceFormat) {
        if (isChoiceFormat != null) {
            isChoiceFormat[0] = false;
        }
        CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
        switch (nameStyle) {
            case 0: {
                return names.getSymbol(this.subType);
            }
            case 3: {
                return names.getNarrowSymbol(this.subType);
            }
            case 1: {
                return names.getName(this.subType);
            }
        }
        throw new IllegalArgumentException("bad name style: " + nameStyle);
    }

    public String getName(Locale locale, int nameStyle, String pluralCount, boolean[] isChoiceFormat) {
        return this.getName(ULocale.forLocale(locale), nameStyle, pluralCount, isChoiceFormat);
    }

    public String getName(ULocale locale, int nameStyle, String pluralCount, boolean[] isChoiceFormat) {
        if (nameStyle != 2) {
            return this.getName(locale, nameStyle, isChoiceFormat);
        }
        if (isChoiceFormat != null) {
            isChoiceFormat[0] = false;
        }
        CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
        return names.getPluralName(this.subType, pluralCount);
    }

    public String getDisplayName() {
        return this.getName(Locale.getDefault(), 1, null);
    }

    public String getDisplayName(Locale locale) {
        return this.getName(locale, 1, null);
    }

    @Deprecated
    public static String parse(ULocale locale, String text, int type, ParsePosition pos) {
        List<TextTrieMap<CurrencyStringInfo>> currencyTrieVec = Currency.getCurrencyTrieVec(locale);
        int maxLength = 0;
        String isoResult = null;
        TextTrieMap<CurrencyStringInfo> currencyNameTrie = currencyTrieVec.get(1);
        CurrencyNameResultHandler handler = new CurrencyNameResultHandler();
        currencyNameTrie.find(text, pos.getIndex(), handler);
        isoResult = handler.getBestCurrencyISOCode();
        maxLength = handler.getBestMatchLength();
        if (type != 1) {
            TextTrieMap<CurrencyStringInfo> currencySymbolTrie = currencyTrieVec.get(0);
            handler = new CurrencyNameResultHandler();
            currencySymbolTrie.find(text, pos.getIndex(), handler);
            if (handler.getBestMatchLength() > maxLength) {
                isoResult = handler.getBestCurrencyISOCode();
                maxLength = handler.getBestMatchLength();
            }
        }
        int start = pos.getIndex();
        pos.setIndex(start + maxLength);
        return isoResult;
    }

    @Deprecated
    public static TextTrieMap<CurrencyStringInfo> getParsingTrie(ULocale locale, int type) {
        List<TextTrieMap<CurrencyStringInfo>> currencyTrieVec = Currency.getCurrencyTrieVec(locale);
        if (type == 1) {
            return currencyTrieVec.get(1);
        }
        return currencyTrieVec.get(0);
    }

    private static List<TextTrieMap<CurrencyStringInfo>> getCurrencyTrieVec(ULocale locale) {
        List<TextTrieMap<CurrencyStringInfo>> currencyTrieVec = CURRENCY_NAME_CACHE.get(locale);
        if (currencyTrieVec == null) {
            TextTrieMap currencyNameTrie = new TextTrieMap(true);
            TextTrieMap currencySymbolTrie = new TextTrieMap(false);
            currencyTrieVec = new ArrayList<TextTrieMap<CurrencyStringInfo>>();
            currencyTrieVec.add(currencySymbolTrie);
            currencyTrieVec.add(currencyNameTrie);
            Currency.setupCurrencyTrieVec(locale, currencyTrieVec);
            CURRENCY_NAME_CACHE.put(locale, currencyTrieVec);
        }
        return currencyTrieVec;
    }

    private static void setupCurrencyTrieVec(ULocale locale, List<TextTrieMap<CurrencyStringInfo>> trieVec) {
        String isoCode;
        TextTrieMap<CurrencyStringInfo> symTrie = trieVec.get(0);
        TextTrieMap<CurrencyStringInfo> trie = trieVec.get(1);
        CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
        for (Map.Entry<String, String> e2 : names.symbolMap().entrySet()) {
            String symbol = e2.getKey();
            isoCode = e2.getValue();
            StaticUnicodeSets.Key key = StaticUnicodeSets.chooseCurrency(symbol);
            CurrencyStringInfo value = new CurrencyStringInfo(isoCode, symbol);
            if (key != null) {
                UnicodeSet equivalents = StaticUnicodeSets.get(key);
                for (String equivalentSymbol : equivalents) {
                    symTrie.put(equivalentSymbol, value);
                }
                continue;
            }
            symTrie.put(symbol, value);
        }
        for (Map.Entry<String, String> e2 : names.nameMap().entrySet()) {
            String name = e2.getKey();
            isoCode = e2.getValue();
            trie.put(name, new CurrencyStringInfo(isoCode, name));
        }
    }

    public int getDefaultFractionDigits() {
        return this.getDefaultFractionDigits(CurrencyUsage.STANDARD);
    }

    public int getDefaultFractionDigits(CurrencyUsage Usage2) {
        CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        CurrencyMetaInfo.CurrencyDigits digits = info.currencyDigits(this.subType, Usage2);
        return digits.fractionDigits;
    }

    public double getRoundingIncrement() {
        return this.getRoundingIncrement(CurrencyUsage.STANDARD);
    }

    public double getRoundingIncrement(CurrencyUsage Usage2) {
        CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        CurrencyMetaInfo.CurrencyDigits digits = info.currencyDigits(this.subType, Usage2);
        int data1 = digits.roundingIncrement;
        if (data1 == 0) {
            return 0.0;
        }
        int data0 = digits.fractionDigits;
        if (data0 < 0 || data0 >= POW10.length) {
            return 0.0;
        }
        return (double)data1 / (double)POW10[data0];
    }

    @Override
    public String toString() {
        return this.subType;
    }

    protected Currency(String theISOCode) {
        super("currency", theISOCode);
        this.isoCode = theISOCode;
    }

    private static synchronized List<String> getAllTenderCurrencies() {
        List<String> all;
        List<String> list = all = ALL_TENDER_CODES == null ? null : ALL_TENDER_CODES.get();
        if (all == null) {
            CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.all();
            all = Collections.unmodifiableList(Currency.getTenderCurrencies(filter));
            ALL_TENDER_CODES = new SoftReference<List<String>>(all);
        }
        return all;
    }

    private static synchronized Set<String> getAllCurrenciesAsSet() {
        Set<String> all;
        Set<String> set = all = ALL_CODES_AS_SET == null ? null : ALL_CODES_AS_SET.get();
        if (all == null) {
            CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
            all = Collections.unmodifiableSet(new HashSet<String>(info.currencies(CurrencyMetaInfo.CurrencyFilter.all())));
            ALL_CODES_AS_SET = new SoftReference<Set<String>>(all);
        }
        return all;
    }

    public static boolean isAvailable(String code, Date from, Date to) {
        if (!Currency.isAlpha3Code(code)) {
            return false;
        }
        if (from != null && to != null && from.after(to)) {
            throw new IllegalArgumentException("To is before from");
        }
        code = code.toUpperCase(Locale.ENGLISH);
        boolean isKnown = Currency.getAllCurrenciesAsSet().contains(code);
        if (!isKnown) {
            return false;
        }
        if (from == null && to == null) {
            return true;
        }
        CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        List<String> allActive = info.currencies(CurrencyMetaInfo.CurrencyFilter.onDateRange(from, to).withCurrency(code));
        return allActive.contains(code);
    }

    private static List<String> getTenderCurrencies(CurrencyMetaInfo.CurrencyFilter filter) {
        CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        return info.currencies(filter.withTender());
    }

    private Object writeReplace() throws ObjectStreamException {
        return new MeasureUnit.MeasureUnitProxy(this.type, this.subType);
    }

    private Object readResolve() throws ObjectStreamException {
        return Currency.getInstance(this.isoCode);
    }

    static {
        regionCurrencyCache = new SoftCache<String, Currency, Void>(){

            @Override
            protected Currency createInstance(String key, Void unused) {
                return Currency.loadCurrency(key);
            }
        };
        UND = new ULocale("und");
        EMPTY_STRING_ARRAY = new String[0];
        POW10 = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
    }

    private static class CurrencyNameResultHandler
    implements TextTrieMap.ResultHandler<CurrencyStringInfo> {
        private int bestMatchLength;
        private String bestCurrencyISOCode;

        private CurrencyNameResultHandler() {
        }

        @Override
        public boolean handlePrefixMatch(int matchLength, Iterator<CurrencyStringInfo> values) {
            if (values.hasNext()) {
                this.bestCurrencyISOCode = values.next().getISOCode();
                this.bestMatchLength = matchLength;
            }
            return true;
        }

        public String getBestCurrencyISOCode() {
            return this.bestCurrencyISOCode;
        }

        public int getBestMatchLength() {
            return this.bestMatchLength;
        }
    }

    @Deprecated
    public static final class CurrencyStringInfo {
        private String isoCode;
        private String currencyString;

        @Deprecated
        public CurrencyStringInfo(String isoCode, String currencyString) {
            this.isoCode = isoCode;
            this.currencyString = currencyString;
        }

        @Deprecated
        public String getISOCode() {
            return this.isoCode;
        }

        @Deprecated
        public String getCurrencyString() {
            return this.currencyString;
        }
    }

    static abstract class ServiceShim {
        ServiceShim() {
        }

        abstract ULocale[] getAvailableULocales();

        abstract Locale[] getAvailableLocales();

        abstract Currency createInstance(ULocale var1);

        abstract Object registerInstance(Currency var1, ULocale var2);

        abstract boolean unregister(Object var1);
    }

    public static enum CurrencyUsage {
        STANDARD,
        CASH;

    }
}

