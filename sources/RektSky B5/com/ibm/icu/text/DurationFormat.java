/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.duration.BasicDurationFormat;
import com.ibm.icu.text.UFormat;
import com.ibm.icu.util.ULocale;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

@Deprecated
public abstract class DurationFormat
extends UFormat {
    private static final long serialVersionUID = -2076961954727774282L;

    @Deprecated
    public static DurationFormat getInstance(ULocale locale) {
        return BasicDurationFormat.getInstance(locale);
    }

    @Deprecated
    protected DurationFormat() {
    }

    @Deprecated
    protected DurationFormat(ULocale locale) {
        this.setLocale(locale, locale);
    }

    @Override
    @Deprecated
    public abstract StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3);

    @Override
    @Deprecated
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public abstract String formatDurationFromNowTo(Date var1);

    @Deprecated
    public abstract String formatDurationFromNow(long var1);

    @Deprecated
    public abstract String formatDurationFrom(long var1, long var3);
}

