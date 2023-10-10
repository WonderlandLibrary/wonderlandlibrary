/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.number;

import com.ibm.icu.number.Precision;
import com.ibm.icu.util.Currency;

public abstract class CurrencyPrecision
extends Precision {
    CurrencyPrecision() {
    }

    public Precision withCurrency(Currency currency) {
        if (currency != null) {
            return CurrencyPrecision.constructFromCurrency(this, currency);
        }
        throw new IllegalArgumentException("Currency must not be null");
    }
}

