/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.text.DateFormatSymbols;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.ULocale;
import java.util.Locale;

@Deprecated
public class ChineseDateFormatSymbols
extends DateFormatSymbols {
    static final long serialVersionUID = 6827816119783952890L;
    String[] isLeapMonth;

    @Deprecated
    public ChineseDateFormatSymbols() {
        this(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    @Deprecated
    public ChineseDateFormatSymbols(Locale locale) {
        super(ChineseCalendar.class, ULocale.forLocale(locale));
    }

    @Deprecated
    public ChineseDateFormatSymbols(ULocale locale) {
        super(ChineseCalendar.class, locale);
    }

    @Deprecated
    public ChineseDateFormatSymbols(Calendar cal, Locale locale) {
        super(cal.getClass(), locale);
    }

    @Deprecated
    public ChineseDateFormatSymbols(Calendar cal, ULocale locale) {
        super(cal.getClass(), locale);
    }

    @Deprecated
    public String getLeapMonth(int leap) {
        return this.isLeapMonth[leap];
    }

    @Override
    @Deprecated
    protected void initializeData(ULocale loc, ICUResourceBundle b2, String calendarType) {
        super.initializeData(loc, b2, calendarType);
        this.initializeIsLeapMonth();
    }

    @Override
    void initializeData(DateFormatSymbols dfs) {
        super.initializeData(dfs);
        if (dfs instanceof ChineseDateFormatSymbols) {
            this.isLeapMonth = ((ChineseDateFormatSymbols)dfs).isLeapMonth;
        } else {
            this.initializeIsLeapMonth();
        }
    }

    private void initializeIsLeapMonth() {
        this.isLeapMonth = new String[2];
        this.isLeapMonth[0] = "";
        this.isLeapMonth[1] = this.leapMonthPatterns != null ? this.leapMonthPatterns[0].replace("{0}", "") : "";
    }
}

