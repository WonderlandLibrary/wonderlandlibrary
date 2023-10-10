/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.DateRule;
import com.ibm.icu.util.GregorianCalendar;
import java.util.Date;

public class SimpleDateRule
implements DateRule {
    private Calendar calendar = new GregorianCalendar();
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;

    public SimpleDateRule(int month, int dayOfMonth) {
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = 0;
    }

    SimpleDateRule(int month, int dayOfMonth, Calendar cal) {
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = 0;
        this.calendar = cal;
    }

    public SimpleDateRule(int month, int dayOfMonth, int dayOfWeek, boolean after) {
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = after ? dayOfWeek : -dayOfWeek;
    }

    @Override
    public Date firstAfter(Date start) {
        return this.doFirstBetween(start, null);
    }

    @Override
    public Date firstBetween(Date start, Date end) {
        return this.doFirstBetween(start, end);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isOn(Date date) {
        Calendar c2;
        Calendar calendar = c2 = this.calendar;
        synchronized (calendar) {
            c2.setTime(date);
            int dayOfYear = c2.get(6);
            c2.setTime(this.computeInYear(c2.get(1), c2));
            return c2.get(6) == dayOfYear;
        }
    }

    @Override
    public boolean isBetween(Date start, Date end) {
        return this.firstBetween(start, end) != null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Date doFirstBetween(Date start, Date end) {
        Calendar c2;
        Calendar calendar = c2 = this.calendar;
        synchronized (calendar) {
            c2.setTime(start);
            int year = c2.get(1);
            int mon = c2.get(2);
            if (mon > this.month) {
                ++year;
            }
            Date result = this.computeInYear(year, c2);
            if (mon == this.month && result.before(start)) {
                result = this.computeInYear(year + 1, c2);
            }
            if (end != null && result.after(end)) {
                return null;
            }
            return result;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Date computeInYear(int year, Calendar c2) {
        Calendar calendar = c2;
        synchronized (calendar) {
            c2.clear();
            c2.set(0, c2.getMaximum(0));
            c2.set(1, year);
            c2.set(2, this.month);
            c2.set(5, this.dayOfMonth);
            if (this.dayOfWeek != 0) {
                c2.setTime(c2.getTime());
                int weekday = c2.get(7);
                int delta = 0;
                delta = this.dayOfWeek > 0 ? (this.dayOfWeek - weekday + 7) % 7 : -((this.dayOfWeek + weekday + 7) % 7);
                c2.add(5, delta);
            }
            return c2.getTime();
        }
    }
}

