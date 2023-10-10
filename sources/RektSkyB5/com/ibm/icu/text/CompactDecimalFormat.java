/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.number.DecimalFormatProperties;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.util.CurrencyAmount;
import com.ibm.icu.util.ULocale;
import java.text.ParsePosition;
import java.util.Locale;

public class CompactDecimalFormat
extends DecimalFormat {
    private static final long serialVersionUID = 4716293295276629682L;

    public static CompactDecimalFormat getInstance(ULocale locale, CompactStyle style) {
        return new CompactDecimalFormat(locale, style);
    }

    public static CompactDecimalFormat getInstance(Locale locale, CompactStyle style) {
        return new CompactDecimalFormat(ULocale.forLocale(locale), style);
    }

    CompactDecimalFormat(ULocale locale, CompactStyle style) {
        this.symbols = DecimalFormatSymbols.getInstance(locale);
        this.properties = new DecimalFormatProperties();
        this.properties.setCompactStyle(style);
        this.properties.setGroupingSize(-2);
        this.properties.setMinimumGroupingDigits(2);
        this.exportedProperties = new DecimalFormatProperties();
        this.refreshFormatter();
    }

    @Override
    public Number parse(String text, ParsePosition parsePosition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CurrencyAmount parseCurrency(CharSequence text, ParsePosition parsePosition) {
        throw new UnsupportedOperationException();
    }

    public static enum CompactStyle {
        SHORT,
        LONG;

    }
}

