/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.ULocale;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

public class ICUCurrencyDisplayInfoProvider
implements CurrencyData.CurrencyDisplayInfoProvider {
    private volatile ICUCurrencyDisplayInfo currencyDisplayInfoCache = null;

    @Override
    public CurrencyData.CurrencyDisplayInfo getInstance(ULocale locale, boolean withFallback) {
        ICUCurrencyDisplayInfo instance;
        if (locale == null) {
            locale = ULocale.ROOT;
        }
        if ((instance = this.currencyDisplayInfoCache) == null || !instance.locale.equals(locale) || instance.fallback != withFallback) {
            ICUResourceBundle rb;
            if (withFallback) {
                rb = ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/curr", locale, ICUResourceBundle.OpenType.LOCALE_DEFAULT_ROOT);
            } else {
                try {
                    rb = ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/curr", locale, ICUResourceBundle.OpenType.LOCALE_ONLY);
                }
                catch (MissingResourceException e2) {
                    return null;
                }
            }
            this.currencyDisplayInfoCache = instance = new ICUCurrencyDisplayInfo(locale, rb, withFallback);
        }
        return instance;
    }

    @Override
    public boolean hasData() {
        return true;
    }

    static class ICUCurrencyDisplayInfo
    extends CurrencyData.CurrencyDisplayInfo {
        final ULocale locale;
        final boolean fallback;
        private final ICUResourceBundle rb;
        private volatile FormattingData formattingDataCache = null;
        private volatile NarrowSymbol narrowSymbolCache = null;
        private volatile String[] pluralsDataCache = null;
        private volatile SoftReference<ParsingData> parsingDataCache = new SoftReference<Object>(null);
        private volatile Map<String, String> unitPatternsCache = null;
        private volatile CurrencyData.CurrencySpacingInfo spacingInfoCache = null;

        public ICUCurrencyDisplayInfo(ULocale locale, ICUResourceBundle rb, boolean fallback) {
            this.locale = locale;
            this.fallback = fallback;
            this.rb = rb;
        }

        @Override
        public ULocale getULocale() {
            return this.rb.getULocale();
        }

        @Override
        public String getName(String isoCode) {
            FormattingData formattingData = this.fetchFormattingData(isoCode);
            if (formattingData.displayName == null && this.fallback) {
                return isoCode;
            }
            return formattingData.displayName;
        }

        @Override
        public String getSymbol(String isoCode) {
            FormattingData formattingData = this.fetchFormattingData(isoCode);
            if (formattingData.symbol == null && this.fallback) {
                return isoCode;
            }
            return formattingData.symbol;
        }

        @Override
        public String getNarrowSymbol(String isoCode) {
            NarrowSymbol narrowSymbol = this.fetchNarrowSymbol(isoCode);
            if (narrowSymbol.narrowSymbol == null && this.fallback) {
                return isoCode;
            }
            return narrowSymbol.narrowSymbol;
        }

        @Override
        public String getPluralName(String isoCode, String pluralKey) {
            StandardPlural plural = StandardPlural.orNullFromString(pluralKey);
            String[] pluralsData = this.fetchPluralsData(isoCode);
            String result = null;
            if (plural != null) {
                result = pluralsData[1 + plural.ordinal()];
            }
            if (result == null && this.fallback) {
                result = pluralsData[1 + StandardPlural.OTHER.ordinal()];
            }
            if (result == null && this.fallback) {
                FormattingData formattingData = this.fetchFormattingData(isoCode);
                result = formattingData.displayName;
            }
            if (result == null && this.fallback) {
                result = isoCode;
            }
            return result;
        }

        @Override
        public Map<String, String> symbolMap() {
            ParsingData parsingData = this.fetchParsingData();
            return parsingData.symbolToIsoCode;
        }

        @Override
        public Map<String, String> nameMap() {
            ParsingData parsingData = this.fetchParsingData();
            return parsingData.nameToIsoCode;
        }

        @Override
        public Map<String, String> getUnitPatterns() {
            Map<String, String> unitPatterns = this.fetchUnitPatterns();
            return unitPatterns;
        }

        @Override
        public CurrencyData.CurrencyFormatInfo getFormatInfo(String isoCode) {
            FormattingData formattingData = this.fetchFormattingData(isoCode);
            return formattingData.formatInfo;
        }

        @Override
        public CurrencyData.CurrencySpacingInfo getSpacingInfo() {
            CurrencyData.CurrencySpacingInfo spacingInfo = this.fetchSpacingInfo();
            if (!(spacingInfo.hasBeforeCurrency && spacingInfo.hasAfterCurrency || !this.fallback)) {
                return CurrencyData.CurrencySpacingInfo.DEFAULT;
            }
            return spacingInfo;
        }

        FormattingData fetchFormattingData(String isoCode) {
            FormattingData result = this.formattingDataCache;
            if (result == null || !result.isoCode.equals(isoCode)) {
                result = new FormattingData(isoCode);
                CurrencySink sink = new CurrencySink(!this.fallback, CurrencySink.EntrypointTable.CURRENCIES);
                sink.formattingData = result;
                this.rb.getAllItemsWithFallbackNoFail("Currencies/" + isoCode, sink);
                this.formattingDataCache = result;
            }
            return result;
        }

        NarrowSymbol fetchNarrowSymbol(String isoCode) {
            NarrowSymbol result = this.narrowSymbolCache;
            if (result == null || !result.isoCode.equals(isoCode)) {
                result = new NarrowSymbol(isoCode);
                CurrencySink sink = new CurrencySink(!this.fallback, CurrencySink.EntrypointTable.CURRENCY_NARROW);
                sink.narrowSymbol = result;
                this.rb.getAllItemsWithFallbackNoFail("Currencies%narrow/" + isoCode, sink);
                this.narrowSymbolCache = result;
            }
            return result;
        }

        String[] fetchPluralsData(String isoCode) {
            String[] result = this.pluralsDataCache;
            if (result == null || !result[0].equals(isoCode)) {
                result = new String[1 + StandardPlural.COUNT];
                result[0] = isoCode;
                CurrencySink sink = new CurrencySink(!this.fallback, CurrencySink.EntrypointTable.CURRENCY_PLURALS);
                sink.pluralsData = result;
                this.rb.getAllItemsWithFallbackNoFail("CurrencyPlurals/" + isoCode, sink);
                this.pluralsDataCache = result;
            }
            return result;
        }

        ParsingData fetchParsingData() {
            ParsingData result = this.parsingDataCache.get();
            if (result == null) {
                result = new ParsingData();
                CurrencySink sink = new CurrencySink(!this.fallback, CurrencySink.EntrypointTable.TOP);
                sink.parsingData = result;
                this.rb.getAllItemsWithFallback("", sink);
                this.parsingDataCache = new SoftReference<ParsingData>(result);
            }
            return result;
        }

        Map<String, String> fetchUnitPatterns() {
            Map<String, String> result = this.unitPatternsCache;
            if (result == null) {
                result = new HashMap<String, String>();
                CurrencySink sink = new CurrencySink(!this.fallback, CurrencySink.EntrypointTable.CURRENCY_UNIT_PATTERNS);
                sink.unitPatterns = result;
                this.rb.getAllItemsWithFallback("CurrencyUnitPatterns", sink);
                this.unitPatternsCache = result;
            }
            return result;
        }

        CurrencyData.CurrencySpacingInfo fetchSpacingInfo() {
            CurrencyData.CurrencySpacingInfo result = this.spacingInfoCache;
            if (result == null) {
                result = new CurrencyData.CurrencySpacingInfo();
                CurrencySink sink = new CurrencySink(!this.fallback, CurrencySink.EntrypointTable.CURRENCY_SPACING);
                sink.spacingInfo = result;
                this.rb.getAllItemsWithFallback("currencySpacing", sink);
                this.spacingInfoCache = result;
            }
            return result;
        }

        private static final class CurrencySink
        extends UResource.Sink {
            final boolean noRoot;
            final EntrypointTable entrypointTable;
            FormattingData formattingData = null;
            String[] pluralsData = null;
            ParsingData parsingData = null;
            Map<String, String> unitPatterns = null;
            CurrencyData.CurrencySpacingInfo spacingInfo = null;
            NarrowSymbol narrowSymbol = null;

            CurrencySink(boolean noRoot, EntrypointTable entrypointTable) {
                this.noRoot = noRoot;
                this.entrypointTable = entrypointTable;
            }

            @Override
            public void put(UResource.Key key, UResource.Value value, boolean isRoot) {
                if (this.noRoot && isRoot) {
                    return;
                }
                switch (this.entrypointTable) {
                    case TOP: {
                        this.consumeTopTable(key, value);
                        break;
                    }
                    case CURRENCIES: {
                        this.consumeCurrenciesEntry(key, value);
                        break;
                    }
                    case CURRENCY_PLURALS: {
                        this.consumeCurrencyPluralsEntry(key, value);
                        break;
                    }
                    case CURRENCY_NARROW: {
                        this.consumeCurrenciesNarrowEntry(key, value);
                        break;
                    }
                    case CURRENCY_SPACING: {
                        this.consumeCurrencySpacingTable(key, value);
                        break;
                    }
                    case CURRENCY_UNIT_PATTERNS: {
                        this.consumeCurrencyUnitPatternsTable(key, value);
                    }
                }
            }

            private void consumeTopTable(UResource.Key key, UResource.Value value) {
                UResource.Table table = value.getTable();
                int i2 = 0;
                while (table.getKeyAndValue(i2, key, value)) {
                    if (key.contentEquals("Currencies")) {
                        this.consumeCurrenciesTable(key, value);
                    } else if (key.contentEquals("Currencies%variant")) {
                        this.consumeCurrenciesVariantTable(key, value);
                    } else if (key.contentEquals("CurrencyPlurals")) {
                        this.consumeCurrencyPluralsTable(key, value);
                    }
                    ++i2;
                }
            }

            void consumeCurrenciesTable(UResource.Key key, UResource.Value value) {
                assert (this.parsingData != null);
                UResource.Table table = value.getTable();
                int i2 = 0;
                while (table.getKeyAndValue(i2, key, value)) {
                    String isoCode = key.toString();
                    if (value.getType() != 8) {
                        throw new ICUException("Unexpected data type in Currencies table for " + isoCode);
                    }
                    UResource.Array array = value.getArray();
                    this.parsingData.symbolToIsoCode.put(isoCode, isoCode);
                    array.getValue(0, value);
                    this.parsingData.symbolToIsoCode.put(value.getString(), isoCode);
                    array.getValue(1, value);
                    this.parsingData.nameToIsoCode.put(value.getString(), isoCode);
                    ++i2;
                }
            }

            void consumeCurrenciesEntry(UResource.Key key, UResource.Value value) {
                assert (this.formattingData != null);
                String isoCode = key.toString();
                if (value.getType() != 8) {
                    throw new ICUException("Unexpected data type in Currencies table for " + isoCode);
                }
                UResource.Array array = value.getArray();
                if (this.formattingData.symbol == null) {
                    array.getValue(0, value);
                    this.formattingData.symbol = value.getString();
                }
                if (this.formattingData.displayName == null) {
                    array.getValue(1, value);
                    this.formattingData.displayName = value.getString();
                }
                if (array.getSize() > 2 && this.formattingData.formatInfo == null) {
                    array.getValue(2, value);
                    UResource.Array formatArray = value.getArray();
                    formatArray.getValue(0, value);
                    String formatPattern = value.getString();
                    formatArray.getValue(1, value);
                    String decimalSeparator = value.getString();
                    formatArray.getValue(2, value);
                    String groupingSeparator = value.getString();
                    this.formattingData.formatInfo = new CurrencyData.CurrencyFormatInfo(isoCode, formatPattern, decimalSeparator, groupingSeparator);
                }
            }

            void consumeCurrenciesNarrowEntry(UResource.Key key, UResource.Value value) {
                assert (this.narrowSymbol != null);
                if (this.narrowSymbol.narrowSymbol == null) {
                    this.narrowSymbol.narrowSymbol = value.getString();
                }
            }

            void consumeCurrenciesVariantTable(UResource.Key key, UResource.Value value) {
                assert (this.parsingData != null);
                UResource.Table table = value.getTable();
                int i2 = 0;
                while (table.getKeyAndValue(i2, key, value)) {
                    String isoCode = key.toString();
                    this.parsingData.symbolToIsoCode.put(value.getString(), isoCode);
                    ++i2;
                }
            }

            void consumeCurrencyPluralsTable(UResource.Key key, UResource.Value value) {
                assert (this.parsingData != null);
                UResource.Table table = value.getTable();
                int i2 = 0;
                while (table.getKeyAndValue(i2, key, value)) {
                    String isoCode = key.toString();
                    UResource.Table pluralsTable = value.getTable();
                    int j2 = 0;
                    while (pluralsTable.getKeyAndValue(j2, key, value)) {
                        StandardPlural plural = StandardPlural.orNullFromString(key.toString());
                        if (plural == null) {
                            throw new ICUException("Could not make StandardPlural from keyword " + key);
                        }
                        this.parsingData.nameToIsoCode.put(value.getString(), isoCode);
                        ++j2;
                    }
                    ++i2;
                }
            }

            void consumeCurrencyPluralsEntry(UResource.Key key, UResource.Value value) {
                assert (this.pluralsData != null);
                UResource.Table pluralsTable = value.getTable();
                int j2 = 0;
                while (pluralsTable.getKeyAndValue(j2, key, value)) {
                    StandardPlural plural = StandardPlural.orNullFromString(key.toString());
                    if (plural == null) {
                        throw new ICUException("Could not make StandardPlural from keyword " + key);
                    }
                    if (this.pluralsData[1 + plural.ordinal()] == null) {
                        this.pluralsData[1 + plural.ordinal()] = value.getString();
                    }
                    ++j2;
                }
            }

            void consumeCurrencySpacingTable(UResource.Key key, UResource.Value value) {
                assert (this.spacingInfo != null);
                UResource.Table spacingTypesTable = value.getTable();
                int i2 = 0;
                while (spacingTypesTable.getKeyAndValue(i2, key, value)) {
                    block8: {
                        CurrencyData.CurrencySpacingInfo.SpacingType type;
                        block7: {
                            block6: {
                                if (!key.contentEquals("beforeCurrency")) break block6;
                                type = CurrencyData.CurrencySpacingInfo.SpacingType.BEFORE;
                                this.spacingInfo.hasBeforeCurrency = true;
                                break block7;
                            }
                            if (!key.contentEquals("afterCurrency")) break block8;
                            type = CurrencyData.CurrencySpacingInfo.SpacingType.AFTER;
                            this.spacingInfo.hasAfterCurrency = true;
                        }
                        UResource.Table patternsTable = value.getTable();
                        int j2 = 0;
                        while (patternsTable.getKeyAndValue(j2, key, value)) {
                            block12: {
                                CurrencyData.CurrencySpacingInfo.SpacingPattern pattern;
                                block10: {
                                    block11: {
                                        block9: {
                                            if (!key.contentEquals("currencyMatch")) break block9;
                                            pattern = CurrencyData.CurrencySpacingInfo.SpacingPattern.CURRENCY_MATCH;
                                            break block10;
                                        }
                                        if (!key.contentEquals("surroundingMatch")) break block11;
                                        pattern = CurrencyData.CurrencySpacingInfo.SpacingPattern.SURROUNDING_MATCH;
                                        break block10;
                                    }
                                    if (!key.contentEquals("insertBetween")) break block12;
                                    pattern = CurrencyData.CurrencySpacingInfo.SpacingPattern.INSERT_BETWEEN;
                                }
                                this.spacingInfo.setSymbolIfNull(type, pattern, value.getString());
                            }
                            ++j2;
                        }
                    }
                    ++i2;
                }
            }

            void consumeCurrencyUnitPatternsTable(UResource.Key key, UResource.Value value) {
                assert (this.unitPatterns != null);
                UResource.Table table = value.getTable();
                int i2 = 0;
                while (table.getKeyAndValue(i2, key, value)) {
                    String pluralKeyword = key.toString();
                    if (this.unitPatterns.get(pluralKeyword) == null) {
                        this.unitPatterns.put(pluralKeyword, value.getString());
                    }
                    ++i2;
                }
            }

            static enum EntrypointTable {
                TOP,
                CURRENCIES,
                CURRENCY_PLURALS,
                CURRENCY_NARROW,
                CURRENCY_SPACING,
                CURRENCY_UNIT_PATTERNS;

            }
        }

        static class ParsingData {
            Map<String, String> symbolToIsoCode = new HashMap<String, String>();
            Map<String, String> nameToIsoCode = new HashMap<String, String>();

            ParsingData() {
            }
        }

        static class NarrowSymbol {
            final String isoCode;
            String narrowSymbol = null;

            NarrowSymbol(String isoCode) {
                this.isoCode = isoCode;
            }
        }

        static class FormattingData {
            final String isoCode;
            String displayName = null;
            String symbol = null;
            CurrencyData.CurrencyFormatInfo formatInfo = null;

            FormattingData(String isoCode) {
                this.isoCode = isoCode;
            }
        }
    }
}

