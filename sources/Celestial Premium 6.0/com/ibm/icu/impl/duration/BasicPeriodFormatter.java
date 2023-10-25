/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.BasicPeriodFormatterFactory;
import com.ibm.icu.impl.duration.Period;
import com.ibm.icu.impl.duration.PeriodFormatter;
import com.ibm.icu.impl.duration.TimeUnit;
import com.ibm.icu.impl.duration.impl.PeriodFormatterData;

class BasicPeriodFormatter
implements PeriodFormatter {
    private BasicPeriodFormatterFactory factory;
    private String localeName;
    private PeriodFormatterData data;
    private BasicPeriodFormatterFactory.Customizations customs;

    BasicPeriodFormatter(BasicPeriodFormatterFactory factory, String localeName, PeriodFormatterData data, BasicPeriodFormatterFactory.Customizations customs) {
        this.factory = factory;
        this.localeName = localeName;
        this.data = data;
        this.customs = customs;
    }

    public String format(Period period) {
        if (!period.isSet()) {
            throw new IllegalArgumentException("period is not set");
        }
        return this.format(period.timeLimit, period.inFuture, period.counts);
    }

    public PeriodFormatter withLocale(String locName) {
        if (!this.localeName.equals(locName)) {
            PeriodFormatterData newData = this.factory.getData(locName);
            return new BasicPeriodFormatter(this.factory, locName, newData, this.customs);
        }
        return this;
    }

    private String format(int tl, boolean inFuture, int[] counts) {
        int i;
        int first;
        int i2;
        int mask = 0;
        for (i2 = 0; i2 < counts.length; ++i2) {
            if (counts[i2] <= 0) continue;
            mask |= 1 << i2;
        }
        if (!this.data.allowZero()) {
            i2 = 0;
            int m = 1;
            while (i2 < counts.length) {
                if ((mask & m) != 0 && counts[i2] == 1) {
                    mask &= ~m;
                }
                ++i2;
                m <<= 1;
            }
            if (mask == 0) {
                return null;
            }
        }
        boolean forceD3Seconds = false;
        if (this.data.useMilliseconds() != 0 && (mask & 1 << TimeUnit.MILLISECOND.ordinal) != 0) {
            byte sx = TimeUnit.SECOND.ordinal;
            byte mx = TimeUnit.MILLISECOND.ordinal;
            int sf = 1 << sx;
            int mf = 1 << mx;
            switch (this.data.useMilliseconds()) {
                case 2: {
                    if ((mask & sf) == 0) break;
                    byte by = sx;
                    counts[by] = counts[by] + (counts[mx] - 1) / 1000;
                    mask &= ~mf;
                    forceD3Seconds = true;
                    break;
                }
                case 1: {
                    if ((mask & sf) == 0) {
                        mask |= sf;
                        counts[sx] = 1;
                    }
                    byte by = sx;
                    counts[by] = counts[by] + (counts[mx] - 1) / 1000;
                    mask &= ~mf;
                    forceD3Seconds = true;
                }
            }
        }
        int last = counts.length - 1;
        for (first = 0; first < counts.length && (mask & 1 << first) == 0; ++first) {
        }
        while (last > first && (mask & 1 << last) == 0) {
            --last;
        }
        boolean isZero = true;
        for (int i3 = first; i3 <= last; ++i3) {
            if ((mask & 1 << i3) == 0 || counts[i3] <= 1) continue;
            isZero = false;
            break;
        }
        StringBuffer sb = new StringBuffer();
        if (!this.customs.displayLimit || isZero) {
            tl = 0;
        }
        int td = !this.customs.displayDirection || isZero ? 0 : (inFuture ? 2 : 1);
        boolean useDigitPrefix = this.data.appendPrefix(tl, td, sb);
        boolean multiple = first != last;
        boolean wasSkipped = true;
        boolean skipped = false;
        boolean countSep = this.customs.separatorVariant != 0;
        int j = i = first;
        while (i <= last) {
            if (skipped) {
                this.data.appendSkippedUnit(sb);
                skipped = false;
                wasSkipped = true;
            }
            while (++j < last && (mask & 1 << j) == 0) {
                skipped = true;
            }
            TimeUnit unit = TimeUnit.units[i];
            int count = counts[i] - 1;
            int cv = this.customs.countVariant;
            if (i == last) {
                if (forceD3Seconds) {
                    cv = 5;
                }
            } else {
                cv = 0;
            }
            boolean isLast = i == last;
            boolean mustSkip = this.data.appendUnit(unit, count, cv, this.customs.unitVariant, countSep, useDigitPrefix, multiple, isLast, wasSkipped, sb);
            skipped |= mustSkip;
            wasSkipped = false;
            if (this.customs.separatorVariant != 0 && j <= last) {
                boolean afterFirst = i == first;
                boolean beforeLast = j == last;
                boolean fullSep = this.customs.separatorVariant == 2;
                useDigitPrefix = this.data.appendUnitSeparator(unit, fullSep, afterFirst, beforeLast, sb);
            } else {
                useDigitPrefix = false;
            }
            i = j;
        }
        this.data.appendSuffix(tl, td, sb);
        return sb.toString();
    }
}

