/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.CurrencyFormat;
import com.ibm.icu.text.UFormat;
import com.ibm.icu.util.ULocale;

public abstract class MeasureFormat
extends UFormat {
    static final long serialVersionUID = -7182021401701778240L;

    protected MeasureFormat() {
    }

    public static MeasureFormat getCurrencyFormat(ULocale locale) {
        return new CurrencyFormat(locale);
    }

    public static MeasureFormat getCurrencyFormat() {
        return MeasureFormat.getCurrencyFormat(ULocale.getDefault(ULocale.Category.FORMAT));
    }
}

