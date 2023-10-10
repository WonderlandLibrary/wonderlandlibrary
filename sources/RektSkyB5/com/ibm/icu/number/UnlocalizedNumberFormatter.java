/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.number;

import com.ibm.icu.number.LocalizedNumberFormatter;
import com.ibm.icu.number.NumberFormatterSettings;
import com.ibm.icu.util.ULocale;
import java.util.Locale;

public class UnlocalizedNumberFormatter
extends NumberFormatterSettings<UnlocalizedNumberFormatter> {
    UnlocalizedNumberFormatter() {
        super(null, 14, new Long(3L));
    }

    UnlocalizedNumberFormatter(NumberFormatterSettings<?> parent, int key, Object value) {
        super(parent, key, value);
    }

    public LocalizedNumberFormatter locale(Locale locale) {
        return new LocalizedNumberFormatter(this, 1, ULocale.forLocale(locale));
    }

    public LocalizedNumberFormatter locale(ULocale locale) {
        return new LocalizedNumberFormatter(this, 1, locale);
    }

    @Override
    UnlocalizedNumberFormatter create(int key, Object value) {
        return new UnlocalizedNumberFormatter(this, key, value);
    }
}

