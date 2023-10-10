/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.TimeZoneRule;
import java.util.Arrays;
import java.util.Date;

public class TimeArrayTimeZoneRule
extends TimeZoneRule {
    private static final long serialVersionUID = -1117109130077415245L;
    private final long[] startTimes;
    private final int timeType;

    public TimeArrayTimeZoneRule(String name, int rawOffset, int dstSavings, long[] startTimes, int timeType) {
        super(name, rawOffset, dstSavings);
        if (startTimes == null || startTimes.length == 0) {
            throw new IllegalArgumentException("No start times are specified.");
        }
        this.startTimes = (long[])startTimes.clone();
        Arrays.sort(this.startTimes);
        this.timeType = timeType;
    }

    public long[] getStartTimes() {
        return (long[])this.startTimes.clone();
    }

    public int getTimeType() {
        return this.timeType;
    }

    @Override
    public Date getFirstStart(int prevRawOffset, int prevDSTSavings) {
        return new Date(this.getUTC(this.startTimes[0], prevRawOffset, prevDSTSavings));
    }

    @Override
    public Date getFinalStart(int prevRawOffset, int prevDSTSavings) {
        return new Date(this.getUTC(this.startTimes[this.startTimes.length - 1], prevRawOffset, prevDSTSavings));
    }

    @Override
    public Date getNextStart(long base, int prevOffset, int prevDSTSavings, boolean inclusive) {
        long time;
        int i2;
        for (i2 = this.startTimes.length - 1; i2 >= 0 && (time = this.getUTC(this.startTimes[i2], prevOffset, prevDSTSavings)) >= base && (inclusive || time != base); --i2) {
        }
        if (i2 == this.startTimes.length - 1) {
            return null;
        }
        return new Date(this.getUTC(this.startTimes[i2 + 1], prevOffset, prevDSTSavings));
    }

    @Override
    public Date getPreviousStart(long base, int prevOffset, int prevDSTSavings, boolean inclusive) {
        for (int i2 = this.startTimes.length - 1; i2 >= 0; --i2) {
            long time = this.getUTC(this.startTimes[i2], prevOffset, prevDSTSavings);
            if (time >= base && (!inclusive || time != base)) continue;
            return new Date(time);
        }
        return null;
    }

    @Override
    public boolean isEquivalentTo(TimeZoneRule other) {
        if (!(other instanceof TimeArrayTimeZoneRule)) {
            return false;
        }
        if (this.timeType == ((TimeArrayTimeZoneRule)other).timeType && Arrays.equals(this.startTimes, ((TimeArrayTimeZoneRule)other).startTimes)) {
            return super.isEquivalentTo(other);
        }
        return false;
    }

    @Override
    public boolean isTransitionRule() {
        return true;
    }

    private long getUTC(long time, int raw, int dst) {
        if (this.timeType != 2) {
            time -= (long)raw;
        }
        if (this.timeType == 0) {
            time -= (long)dst;
        }
        return time;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append(", timeType=");
        buf.append(this.timeType);
        buf.append(", startTimes=[");
        for (int i2 = 0; i2 < this.startTimes.length; ++i2) {
            if (i2 != 0) {
                buf.append(", ");
            }
            buf.append(Long.toString(this.startTimes[i2]));
        }
        buf.append("]");
        return buf.toString();
    }
}

