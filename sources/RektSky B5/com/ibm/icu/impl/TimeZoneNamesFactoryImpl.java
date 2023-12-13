/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.TimeZoneNamesImpl;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.ULocale;

public class TimeZoneNamesFactoryImpl
extends TimeZoneNames.Factory {
    @Override
    public TimeZoneNames getTimeZoneNames(ULocale locale) {
        return new TimeZoneNamesImpl(locale);
    }
}

