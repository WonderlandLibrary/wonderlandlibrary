/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.text.DateFormatSymbols;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.ULocale;
import java.util.Locale;

public class ChineseDateFormatSymbols
extends DateFormatSymbols {
    static final long serialVersionUID = 6827816119783952890L;
    String[] isLeapMonth;

    public ChineseDateFormatSymbols() {
        this(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public ChineseDateFormatSymbols(Locale locale) {
        super(ChineseCalendar.class, ULocale.forLocale(locale));
    }

    public ChineseDateFormatSymbols(ULocale locale) {
        super(ChineseCalendar.class, locale);
    }

    public ChineseDateFormatSymbols(Calendar cal, Locale locale) {
        super(cal == null ? null : cal.getClass(), locale);
    }

    public ChineseDateFormatSymbols(Calendar cal, ULocale locale) {
        super(cal == null ? null : cal.getClass(), locale);
    }

    public String getLeapMonth(int leap) {
        return this.isLeapMonth[leap];
    }

    protected void initializeData(ULocale loc, CalendarData calData) {
        super.initializeData(loc, calData);
        this.initializeIsLeapMonth();
    }

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

