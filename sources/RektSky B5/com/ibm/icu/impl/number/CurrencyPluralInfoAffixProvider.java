/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.number.AffixPatternProvider;
import com.ibm.icu.impl.number.DecimalFormatProperties;
import com.ibm.icu.impl.number.PatternStringParser;
import com.ibm.icu.impl.number.PropertiesAffixPatternProvider;
import com.ibm.icu.text.CurrencyPluralInfo;

public class CurrencyPluralInfoAffixProvider
implements AffixPatternProvider {
    private final PropertiesAffixPatternProvider[] affixesByPlural = new PropertiesAffixPatternProvider[StandardPlural.COUNT];

    public CurrencyPluralInfoAffixProvider(CurrencyPluralInfo cpi, DecimalFormatProperties properties) {
        DecimalFormatProperties pluralProperties = new DecimalFormatProperties();
        pluralProperties.copyFrom(properties);
        for (StandardPlural plural : StandardPlural.VALUES) {
            String pattern = cpi.getCurrencyPluralPattern(plural.getKeyword());
            PatternStringParser.parseToExistingProperties(pattern, pluralProperties);
            this.affixesByPlural[plural.ordinal()] = new PropertiesAffixPatternProvider(pluralProperties);
        }
    }

    @Override
    public char charAt(int flags, int i2) {
        int pluralOrdinal = flags & 0xFF;
        return this.affixesByPlural[pluralOrdinal].charAt(flags, i2);
    }

    @Override
    public int length(int flags) {
        int pluralOrdinal = flags & 0xFF;
        return this.affixesByPlural[pluralOrdinal].length(flags);
    }

    @Override
    public String getString(int flags) {
        int pluralOrdinal = flags & 0xFF;
        return this.affixesByPlural[pluralOrdinal].getString(flags);
    }

    @Override
    public boolean positiveHasPlusSign() {
        return this.affixesByPlural[StandardPlural.OTHER.ordinal()].positiveHasPlusSign();
    }

    @Override
    public boolean hasNegativeSubpattern() {
        return this.affixesByPlural[StandardPlural.OTHER.ordinal()].hasNegativeSubpattern();
    }

    @Override
    public boolean negativeHasMinusSign() {
        return this.affixesByPlural[StandardPlural.OTHER.ordinal()].negativeHasMinusSign();
    }

    @Override
    public boolean hasCurrencySign() {
        return this.affixesByPlural[StandardPlural.OTHER.ordinal()].hasCurrencySign();
    }

    @Override
    public boolean containsSymbolType(int type) {
        return this.affixesByPlural[StandardPlural.OTHER.ordinal()].containsSymbolType(type);
    }

    @Override
    public boolean hasBody() {
        return this.affixesByPlural[StandardPlural.OTHER.ordinal()].hasBody();
    }
}

