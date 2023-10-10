/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.number;

import com.ibm.icu.impl.number.DecimalFormatProperties;
import com.ibm.icu.number.LocalizedNumberFormatter;
import com.ibm.icu.number.NumberPropertyMapper;
import com.ibm.icu.number.NumberSkeletonImpl;
import com.ibm.icu.number.UnlocalizedNumberFormatter;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.util.ULocale;
import java.util.Locale;

public final class NumberFormatter {
    private static final UnlocalizedNumberFormatter BASE = new UnlocalizedNumberFormatter();
    static final long DEFAULT_THRESHOLD = 3L;

    private NumberFormatter() {
    }

    public static UnlocalizedNumberFormatter with() {
        return BASE;
    }

    public static LocalizedNumberFormatter withLocale(Locale locale) {
        return BASE.locale(locale);
    }

    public static LocalizedNumberFormatter withLocale(ULocale locale) {
        return BASE.locale(locale);
    }

    public static UnlocalizedNumberFormatter forSkeleton(String skeleton) {
        return NumberSkeletonImpl.getOrCreate(skeleton);
    }

    @Deprecated
    public static UnlocalizedNumberFormatter fromDecimalFormat(DecimalFormatProperties properties, DecimalFormatSymbols symbols, DecimalFormatProperties exportedProperties) {
        return NumberPropertyMapper.create(properties, symbols, exportedProperties);
    }

    public static enum DecimalSeparatorDisplay {
        AUTO,
        ALWAYS;

    }

    public static enum SignDisplay {
        AUTO,
        ALWAYS,
        NEVER,
        ACCOUNTING,
        ACCOUNTING_ALWAYS,
        EXCEPT_ZERO,
        ACCOUNTING_EXCEPT_ZERO;

    }

    public static enum GroupingStrategy {
        OFF,
        MIN2,
        AUTO,
        ON_ALIGNED,
        THOUSANDS;

    }

    public static enum UnitWidth {
        NARROW,
        SHORT,
        FULL_NAME,
        ISO_CODE,
        HIDDEN;

    }
}

