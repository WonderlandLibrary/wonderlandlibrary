/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.text.CurrencyDisplayNames;
import com.ibm.icu.util.ULocale;
import java.util.Collections;
import java.util.Map;

public class CurrencyData {
    public static final CurrencyDisplayInfoProvider provider;

    private CurrencyData() {
    }

    static {
        CurrencyDisplayInfoProvider temp = null;
        try {
            Class<?> clzz = Class.forName("com.ibm.icu.impl.ICUCurrencyDisplayInfoProvider");
            temp = (CurrencyDisplayInfoProvider)clzz.newInstance();
        }
        catch (Throwable t2) {
            temp = new CurrencyDisplayInfoProvider(){

                @Override
                public CurrencyDisplayInfo getInstance(ULocale locale, boolean withFallback) {
                    return DefaultInfo.getWithFallback(withFallback);
                }

                @Override
                public boolean hasData() {
                    return false;
                }
            };
        }
        provider = temp;
    }

    public static class DefaultInfo
    extends CurrencyDisplayInfo {
        private final boolean fallback;
        private static final CurrencyDisplayInfo FALLBACK_INSTANCE = new DefaultInfo(true);
        private static final CurrencyDisplayInfo NO_FALLBACK_INSTANCE = new DefaultInfo(false);

        private DefaultInfo(boolean fallback) {
            this.fallback = fallback;
        }

        public static final CurrencyDisplayInfo getWithFallback(boolean fallback) {
            return fallback ? FALLBACK_INSTANCE : NO_FALLBACK_INSTANCE;
        }

        @Override
        public String getName(String isoCode) {
            return this.fallback ? isoCode : null;
        }

        @Override
        public String getPluralName(String isoCode, String pluralType) {
            return this.fallback ? isoCode : null;
        }

        @Override
        public String getSymbol(String isoCode) {
            return this.fallback ? isoCode : null;
        }

        @Override
        public String getNarrowSymbol(String isoCode) {
            return this.fallback ? isoCode : null;
        }

        @Override
        public Map<String, String> symbolMap() {
            return Collections.emptyMap();
        }

        @Override
        public Map<String, String> nameMap() {
            return Collections.emptyMap();
        }

        @Override
        public ULocale getULocale() {
            return ULocale.ROOT;
        }

        @Override
        public Map<String, String> getUnitPatterns() {
            if (this.fallback) {
                return Collections.emptyMap();
            }
            return null;
        }

        @Override
        public CurrencyFormatInfo getFormatInfo(String isoCode) {
            return null;
        }

        @Override
        public CurrencySpacingInfo getSpacingInfo() {
            return this.fallback ? CurrencySpacingInfo.DEFAULT : null;
        }
    }

    public static final class CurrencySpacingInfo {
        private final String[][] symbols = new String[SpacingType.COUNT.ordinal()][SpacingPattern.COUNT.ordinal()];
        public boolean hasBeforeCurrency = false;
        public boolean hasAfterCurrency = false;
        private static final String DEFAULT_CUR_MATCH = "[:letter:]";
        private static final String DEFAULT_CTX_MATCH = "[:digit:]";
        private static final String DEFAULT_INSERT = " ";
        public static final CurrencySpacingInfo DEFAULT = new CurrencySpacingInfo("[:letter:]", "[:digit:]", " ", "[:letter:]", "[:digit:]", " ");

        public CurrencySpacingInfo() {
        }

        public CurrencySpacingInfo(String ... strings) {
            assert (strings.length == 6);
            int k2 = 0;
            for (int i2 = 0; i2 < SpacingType.COUNT.ordinal(); ++i2) {
                for (int j2 = 0; j2 < SpacingPattern.COUNT.ordinal(); ++j2) {
                    this.symbols[i2][j2] = strings[k2];
                    ++k2;
                }
            }
        }

        public void setSymbolIfNull(SpacingType type, SpacingPattern pattern, String value) {
            int j2;
            int i2 = type.ordinal();
            if (this.symbols[i2][j2 = pattern.ordinal()] == null) {
                this.symbols[i2][j2] = value;
            }
        }

        public String[] getBeforeSymbols() {
            return this.symbols[SpacingType.BEFORE.ordinal()];
        }

        public String[] getAfterSymbols() {
            return this.symbols[SpacingType.AFTER.ordinal()];
        }

        public static final class SpacingPattern
        extends Enum<SpacingPattern> {
            public static final /* enum */ SpacingPattern CURRENCY_MATCH = new SpacingPattern(0);
            public static final /* enum */ SpacingPattern SURROUNDING_MATCH = new SpacingPattern(1);
            public static final /* enum */ SpacingPattern INSERT_BETWEEN = new SpacingPattern(2);
            public static final /* enum */ SpacingPattern COUNT = new SpacingPattern();
            private static final /* synthetic */ SpacingPattern[] $VALUES;

            public static SpacingPattern[] values() {
                return (SpacingPattern[])$VALUES.clone();
            }

            public static SpacingPattern valueOf(String name) {
                return Enum.valueOf(SpacingPattern.class, name);
            }

            private SpacingPattern() {
            }

            private SpacingPattern(int value) {
                assert (value == this.ordinal());
            }

            static {
                $VALUES = new SpacingPattern[]{CURRENCY_MATCH, SURROUNDING_MATCH, INSERT_BETWEEN, COUNT};
            }
        }

        public static enum SpacingType {
            BEFORE,
            AFTER,
            COUNT;

        }
    }

    public static final class CurrencyFormatInfo {
        public final String isoCode;
        public final String currencyPattern;
        public final String monetaryDecimalSeparator;
        public final String monetaryGroupingSeparator;

        public CurrencyFormatInfo(String isoCode, String currencyPattern, String monetarySeparator, String monetaryGroupingSeparator) {
            this.isoCode = isoCode;
            this.currencyPattern = currencyPattern;
            this.monetaryDecimalSeparator = monetarySeparator;
            this.monetaryGroupingSeparator = monetaryGroupingSeparator;
        }
    }

    public static abstract class CurrencyDisplayInfo
    extends CurrencyDisplayNames {
        public abstract Map<String, String> getUnitPatterns();

        public abstract CurrencyFormatInfo getFormatInfo(String var1);

        public abstract CurrencySpacingInfo getSpacingInfo();
    }

    public static interface CurrencyDisplayInfoProvider {
        public CurrencyDisplayInfo getInstance(ULocale var1, boolean var2);

        public boolean hasData();
    }
}

