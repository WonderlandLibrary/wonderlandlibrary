/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.DateRule;
import com.ibm.icu.util.GregorianCalendar;
import java.util.Date;

public class SimpleDateRule
implements DateRule {
    private static GregorianCalendar gCalendar = new GregorianCalendar();
    private Calendar calendar = gCalendar;
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

    public Date firstAfter(Date start) {
        return this.doFirstBetween(start, null);
    }

    public Date firstBetween(Date start, Date end) {
        return this.doFirstBetween(start, end);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isOn(Date date) {
        Calendar c;
        Calendar calendar = c = this.calendar;
        synchronized (calendar) {
            c.setTime(date);
            int dayOfYear = c.get(6);
            c.setTime(this.computeInYear(c.get(1), c));
            return c.get(6) == dayOfYear;
        }
    }

    public boolean isBetween(Date start, Date end) {
        return this.firstBetween(start, end) != null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Date doFirstBetween(Date start, Date end) {
        Calendar c;
        Calendar calendar = c = this.calendar;
        synchronized (calendar) {
            c.setTime(start);
            int year = c.get(1);
            int mon = c.get(2);
            if (mon > this.month) {
                ++year;
            }
            Date result = this.computeInYear(year, c);
            if (mon == this.month && result.before(start)) {
                result = this.computeInYear(year + 1, c);
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
    private Date computeInYear(int year, Calendar c) {
        Calendar calendar = c;
        synchronized (calendar) {
            c.clear();
            c.set(0, c.getMaximum(0));
            c.set(1, year);
            c.set(2, this.month);
            c.set(5, this.dayOfMonth);
            if (this.dayOfWeek != 0) {
                c.setTime(c.getTime());
                int weekday = c.get(7);
                int delta = 0;
                delta = this.dayOfWeek > 0 ? (this.dayOfWeek - weekday + 7) % 7 : -((this.dayOfWeek + weekday + 7) % 7);
                c.add(5, delta);
            }
            return c.getTime();
        }
    }
}

