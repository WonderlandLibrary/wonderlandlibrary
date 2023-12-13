/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.DateRule;
import com.ibm.icu.util.GregorianCalendar;
import java.util.Date;

class EasterRule
implements DateRule {
    private int daysAfterEaster;
    private GregorianCalendar calendar = new GregorianCalendar();

    public EasterRule(int daysAfterEaster, boolean isOrthodox) {
        this.daysAfterEaster = daysAfterEaster;
        if (isOrthodox) {
            this.calendar.setGregorianChange(new Date(Long.MAX_VALUE));
        }
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
        GregorianCalendar gregorianCalendar = this.calendar;
        synchronized (gregorianCalendar) {
            this.calendar.setTime(date);
            int dayOfYear = this.calendar.get(6);
            this.calendar.setTime(this.computeInYear(this.calendar.getTime(), this.calendar));
            return this.calendar.get(6) == dayOfYear;
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
        GregorianCalendar gregorianCalendar = this.calendar;
        synchronized (gregorianCalendar) {
            Date result = this.computeInYear(start, this.calendar);
            if (result.before(start)) {
                this.calendar.setTime(start);
                this.calendar.get(1);
                this.calendar.add(1, 1);
                result = this.computeInYear(this.calendar.getTime(), this.calendar);
            }
            if (end != null && !result.before(end)) {
                return null;
            }
            return result;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Date computeInYear(Date date, GregorianCalendar cal) {
        if (cal == null) {
            cal = this.calendar;
        }
        GregorianCalendar gregorianCalendar = cal;
        synchronized (gregorianCalendar) {
            cal.setTime(date);
            int year = cal.get(1);
            int g2 = year % 19;
            int i2 = 0;
            int j2 = 0;
            if (cal.getTime().after(cal.getGregorianChange())) {
                int c2 = year / 100;
                int h2 = (c2 - c2 / 4 - (8 * c2 + 13) / 25 + 19 * g2 + 15) % 30;
                i2 = h2 - h2 / 28 * (1 - h2 / 28 * (29 / (h2 + 1)) * ((21 - g2) / 11));
                j2 = (year + year / 4 + i2 + 2 - c2 + c2 / 4) % 7;
            } else {
                i2 = (19 * g2 + 15) % 30;
                j2 = (year + year / 4 + i2) % 7;
            }
            int l2 = i2 - j2;
            int m2 = 3 + (l2 + 40) / 44;
            int d2 = l2 + 28 - 31 * (m2 / 4);
            cal.clear();
            cal.set(0, 1);
            cal.set(1, year);
            cal.set(2, m2 - 1);
            cal.set(5, d2);
            cal.getTime();
            cal.add(5, this.daysAfterEaster);
            return cal.getTime();
        }
    }
}

