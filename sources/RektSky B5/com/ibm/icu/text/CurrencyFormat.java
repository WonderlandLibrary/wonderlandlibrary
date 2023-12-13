/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.MeasureFormat;
import com.ibm.icu.util.CurrencyAmount;
import com.ibm.icu.util.ULocale;
import java.io.ObjectStreamException;
import java.text.FieldPosition;
import java.text.ParsePosition;

class CurrencyFormat
extends MeasureFormat {
    static final long serialVersionUID = -931679363692504634L;

    public CurrencyFormat(ULocale locale) {
        super(locale, MeasureFormat.FormatWidth.DEFAULT_CURRENCY);
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (!(obj instanceof CurrencyAmount)) {
            throw new IllegalArgumentException("Invalid type: " + obj.getClass().getName());
        }
        return super.format(obj, toAppendTo, pos);
    }

    @Override
    public CurrencyAmount parseObject(String source, ParsePosition pos) {
        return this.getNumberFormatInternal().parseCurrency(source, pos);
    }

    private Object writeReplace() throws ObjectStreamException {
        return this.toCurrencyProxy();
    }

    private Object readResolve() throws ObjectStreamException {
        return new CurrencyFormat(this.getLocale(ULocale.ACTUAL_LOCALE));
    }
}

