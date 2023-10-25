/*
 * Decompiled with CFR 0.150.
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

    public Date getFirstStart(int prevRawOffset, int prevDSTSavings) {
        return new Date(this.getUTC(this.startTimes[0], prevRawOffset, prevDSTSavings));
    }

    public Date getFinalStart(int prevRawOffset, int prevDSTSavings) {
        return new Date(this.getUTC(this.startTimes[this.startTimes.length - 1], prevRawOffset, prevDSTSavings));
    }

    public Date getNextStart(long base, int prevOffset, int prevDSTSavings, boolean inclusive) {
        long time;
        int i;
        for (i = this.startTimes.length - 1; i >= 0 && (time = this.getUTC(this.startTimes[i], prevOffset, prevDSTSavings)) >= base && (inclusive || time != base); --i) {
        }
        if (i == this.startTimes.length - 1) {
            return null;
        }
        return new Date(this.getUTC(this.startTimes[i + 1], prevOffset, prevDSTSavings));
    }

    public Date getPreviousStart(long base, int prevOffset, int prevDSTSavings, boolean inclusive) {
        for (int i = this.startTimes.length - 1; i >= 0; --i) {
            long time = this.getUTC(this.startTimes[i], prevOffset, prevDSTSavings);
            if (time >= base && (!inclusive || time != base)) continue;
            return new Date(time);
        }
        return null;
    }

    public boolean isEquivalentTo(TimeZoneRule other) {
        if (!(other instanceof TimeArrayTimeZoneRule)) {
            return false;
        }
        if (this.timeType == ((TimeArrayTimeZoneRule)other).timeType && Arrays.equals(this.startTimes, ((TimeArrayTimeZoneRule)other).startTimes)) {
            return super.isEquivalentTo(other);
        }
        return false;
    }

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

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append(", timeType=");
        buf.append(this.timeType);
        buf.append(", startTimes=[");
        for (int i = 0; i < this.startTimes.length; ++i) {
            if (i != 0) {
                buf.append(", ");
            }
            buf.append(Long.toString(this.startTimes[i]));
        }
        buf.append("]");
        return buf.toString();
    }
}

