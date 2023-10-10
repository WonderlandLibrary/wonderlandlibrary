/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.number.parse.CombinedCurrencyMatcher;
import com.ibm.icu.impl.number.parse.IgnorablesMatcher;
import com.ibm.icu.impl.number.parse.MinusSignMatcher;
import com.ibm.icu.impl.number.parse.PercentMatcher;
import com.ibm.icu.impl.number.parse.PermilleMatcher;
import com.ibm.icu.impl.number.parse.PlusSignMatcher;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;

public class AffixTokenMatcherFactory {
    public Currency currency;
    public DecimalFormatSymbols symbols;
    public IgnorablesMatcher ignorables;
    public ULocale locale;
    public int parseFlags;

    public MinusSignMatcher minusSign() {
        return MinusSignMatcher.getInstance(this.symbols, true);
    }

    public PlusSignMatcher plusSign() {
        return PlusSignMatcher.getInstance(this.symbols, true);
    }

    public PercentMatcher percent() {
        return PercentMatcher.getInstance(this.symbols);
    }

    public PermilleMatcher permille() {
        return PermilleMatcher.getInstance(this.symbols);
    }

    public CombinedCurrencyMatcher currency() {
        return CombinedCurrencyMatcher.getInstance(this.currency, this.symbols, this.parseFlags);
    }

    public IgnorablesMatcher ignorables() {
        return this.ignorables;
    }
}

