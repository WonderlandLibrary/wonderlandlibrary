/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.Grego;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.ZoneMeta;
import com.ibm.icu.util.AnnualTimeZoneRule;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.DateTimeRule;
import com.ibm.icu.util.InitialTimeZoneRule;
import com.ibm.icu.util.SimpleTimeZone;
import com.ibm.icu.util.TimeArrayTimeZoneRule;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.TimeZoneRule;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.MissingResourceException;

public class OlsonTimeZone
extends BasicTimeZone {
    static final long serialVersionUID = -6281977362477515376L;
    private int transitionCount;
    private int typeCount;
    private long[] transitionTimes64;
    private int[] typeOffsets;
    private byte[] typeMapData;
    private int finalStartYear = Integer.MAX_VALUE;
    private double finalStartMillis = Double.MAX_VALUE;
    private SimpleTimeZone finalZone = null;
    private volatile String canonicalID = null;
    private static final String ZONEINFORES = "zoneinfo64";
    private static final boolean DEBUG = ICUDebug.enabled("olson");
    private static final int SECONDS_PER_DAY = 86400;
    private transient InitialTimeZoneRule initialRule;
    private transient TimeZoneTransition firstTZTransition;
    private transient int firstTZTransitionIdx;
    private transient TimeZoneTransition firstFinalTZTransition;
    private transient TimeArrayTimeZoneRule[] historicRules;
    private transient SimpleTimeZone finalZoneWithStartYear;
    private transient boolean transitionRulesInitialized;
    private static final int currentSerialVersion = 1;
    private int serialVersionOnStream = 1;
    private transient boolean isFrozen = false;

    public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
        if (month < 0 || month > 11) {
            throw new IllegalArgumentException("Month is not in the legal range: " + month);
        }
        return this.getOffset(era, year, month, day, dayOfWeek, milliseconds, Grego.monthLength(year, month));
    }

    public int getOffset(int era, int year, int month, int dom, int dow, int millis, int monthLength) {
        if (era != 1 && era != 0 || month < 0 || month > 11 || dom < 1 || dom > monthLength || dow < 1 || dow > 7 || millis < 0 || millis >= 86400000 || monthLength < 28 || monthLength > 31) {
            throw new IllegalArgumentException();
        }
        if (era == 0) {
            year = -year;
        }
        if (this.finalZone != null && year >= this.finalStartYear) {
            return this.finalZone.getOffset(era, year, month, dom, dow, millis);
        }
        long time = Grego.fieldsToDay(year, month, dom) * 86400000L + (long)millis;
        int[] offsets = new int[2];
        this.getHistoricalOffset(time, true, 3, 1, offsets);
        return offsets[0] + offsets[1];
    }

    public void setRawOffset(int offsetMillis) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen OlsonTimeZone instance.");
        }
        if (this.getRawOffset() == offsetMillis) {
            return;
        }
        long current = System.currentTimeMillis();
        if ((double)current < this.finalStartMillis) {
            SimpleTimeZone stz = new SimpleTimeZone(offsetMillis, this.getID());
            boolean bDst = this.useDaylightTime();
            if (bDst) {
                TimeZoneTransition tzt;
                TimeZoneRule[] currentRules = this.getSimpleTimeZoneRulesNear(current);
                if (currentRules.length != 3 && (tzt = this.getPreviousTransition(current, false)) != null) {
                    currentRules = this.getSimpleTimeZoneRulesNear(tzt.getTime() - 1L);
                }
                if (currentRules.length == 3 && currentRules[1] instanceof AnnualTimeZoneRule && currentRules[2] instanceof AnnualTimeZoneRule) {
                    int sav;
                    DateTimeRule end;
                    DateTimeRule start;
                    int offset2;
                    AnnualTimeZoneRule r1 = (AnnualTimeZoneRule)currentRules[1];
                    AnnualTimeZoneRule r2 = (AnnualTimeZoneRule)currentRules[2];
                    int offset1 = r1.getRawOffset() + r1.getDSTSavings();
                    if (offset1 > (offset2 = r2.getRawOffset() + r2.getDSTSavings())) {
                        start = r1.getRule();
                        end = r2.getRule();
                        sav = offset1 - offset2;
                    } else {
                        start = r2.getRule();
                        end = r1.getRule();
                        sav = offset2 - offset1;
                    }
                    stz.setStartRule(start.getRuleMonth(), start.getRuleWeekInMonth(), start.getRuleDayOfWeek(), start.getRuleMillisInDay());
                    stz.setEndRule(end.getRuleMonth(), end.getRuleWeekInMonth(), end.getRuleDayOfWeek(), end.getRuleMillisInDay());
                    stz.setDSTSavings(sav);
                } else {
                    stz.setStartRule(0, 1, 0);
                    stz.setEndRule(11, 31, 86399999);
                }
            }
            int[] fields = Grego.timeToFields(current, null);
            this.finalStartYear = fields[0];
            this.finalStartMillis = Grego.fieldsToDay(fields[0], 0, 1);
            if (bDst) {
                stz.setStartYear(this.finalStartYear);
            }
            this.finalZone = stz;
        } else {
            this.finalZone.setRawOffset(offsetMillis);
        }
        this.transitionRulesInitialized = false;
    }

    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }

    public void getOffset(long date, boolean local, int[] offsets) {
        if (this.finalZone != null && (double)date >= this.finalStartMillis) {
            this.finalZone.getOffset(date, local, offsets);
        } else {
            this.getHistoricalOffset(date, local, 4, 12, offsets);
        }
    }

    public void getOffsetFromLocal(long date, int nonExistingTimeOpt, int duplicatedTimeOpt, int[] offsets) {
        if (this.finalZone != null && (double)date >= this.finalStartMillis) {
            this.finalZone.getOffsetFromLocal(date, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
        } else {
            this.getHistoricalOffset(date, true, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
        }
    }

    public int getRawOffset() {
        int[] ret = new int[2];
        this.getOffset(System.currentTimeMillis(), false, ret);
        return ret[0];
    }

    public boolean useDaylightTime() {
        long current = System.currentTimeMillis();
        if (this.finalZone != null && (double)current >= this.finalStartMillis) {
            return this.finalZone != null && this.finalZone.useDaylightTime();
        }
        int[] fields = Grego.timeToFields(current, null);
        long start = Grego.fieldsToDay(fields[0], 0, 1) * 86400L;
        long limit = Grego.fieldsToDay(fields[0] + 1, 0, 1) * 86400L;
        for (int i = 0; i < this.transitionCount && this.transitionTimes64[i] < limit; ++i) {
            if ((this.transitionTimes64[i] < start || this.dstOffsetAt(i) == 0) && (this.transitionTimes64[i] <= start || i <= 0 || this.dstOffsetAt(i - 1) == 0)) continue;
            return true;
        }
        return false;
    }

    public boolean observesDaylightTime() {
        long current = System.currentTimeMillis();
        if (this.finalZone != null) {
            if (this.finalZone.useDaylightTime()) {
                return true;
            }
            if ((double)current >= this.finalStartMillis) {
                return false;
            }
        }
        long currentSec = Grego.floorDivide(current, 1000L);
        int trsIdx = this.transitionCount - 1;
        if (this.dstOffsetAt(trsIdx) != 0) {
            return true;
        }
        while (trsIdx >= 0 && this.transitionTimes64[trsIdx] > currentSec) {
            if (this.dstOffsetAt(trsIdx - 1) == 0) continue;
            return true;
        }
        return false;
    }

    public int getDSTSavings() {
        if (this.finalZone != null) {
            return this.finalZone.getDSTSavings();
        }
        return super.getDSTSavings();
    }

    public boolean inDaylightTime(Date date) {
        int[] temp = new int[2];
        this.getOffset(date.getTime(), false, temp);
        return temp[1] != 0;
    }

    public boolean hasSameRules(TimeZone other) {
        if (this == other) {
            return true;
        }
        if (!super.hasSameRules(other)) {
            return false;
        }
        if (!(other instanceof OlsonTimeZone)) {
            return false;
        }
        OlsonTimeZone o = (OlsonTimeZone)other;
        if (this.finalZone == null ? o.finalZone != null : o.finalZone == null || this.finalStartYear != o.finalStartYear || !this.finalZone.hasSameRules(o.finalZone)) {
            return false;
        }
        return this.transitionCount == o.transitionCount && Arrays.equals(this.transitionTimes64, o.transitionTimes64) && this.typeCount == o.typeCount && Arrays.equals(this.typeMapData, o.typeMapData) && Arrays.equals(this.typeOffsets, o.typeOffsets);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getCanonicalID() {
        if (this.canonicalID == null) {
            OlsonTimeZone olsonTimeZone = this;
            synchronized (olsonTimeZone) {
                if (this.canonicalID == null) {
                    this.canonicalID = OlsonTimeZone.getCanonicalID(this.getID());
                    assert (this.canonicalID != null);
                    if (this.canonicalID == null) {
                        this.canonicalID = this.getID();
                    }
                }
            }
        }
        return this.canonicalID;
    }

    private void constructEmpty() {
        this.transitionCount = 0;
        this.transitionTimes64 = null;
        this.typeMapData = null;
        this.typeCount = 1;
        this.typeOffsets = new int[]{0, 0};
        this.finalZone = null;
        this.finalStartYear = Integer.MAX_VALUE;
        this.finalStartMillis = Double.MAX_VALUE;
        this.transitionRulesInitialized = false;
    }

    public OlsonTimeZone(UResourceBundle top, UResourceBundle res, String id) {
        super(id);
        this.construct(top, res);
    }

    private void construct(UResourceBundle top, UResourceBundle res) {
        block25: {
            UResourceBundle r;
            if (top == null || res == null) {
                throw new IllegalArgumentException();
            }
            if (DEBUG) {
                System.out.println("OlsonTimeZone(" + res.getKey() + ")");
            }
            int[] transPost32 = null;
            int[] trans32 = null;
            int[] transPre32 = null;
            this.transitionCount = 0;
            try {
                r = res.get("transPre32");
                transPre32 = r.getIntVector();
                if (transPre32.length % 2 != 0) {
                    throw new IllegalArgumentException("Invalid Format");
                }
                this.transitionCount += transPre32.length / 2;
            }
            catch (MissingResourceException e) {
                // empty catch block
            }
            try {
                r = res.get("trans");
                trans32 = r.getIntVector();
                this.transitionCount += trans32.length;
            }
            catch (MissingResourceException e) {
                // empty catch block
            }
            try {
                r = res.get("transPost32");
                transPost32 = r.getIntVector();
                if (transPost32.length % 2 != 0) {
                    throw new IllegalArgumentException("Invalid Format");
                }
                this.transitionCount += transPost32.length / 2;
            }
            catch (MissingResourceException e) {
                // empty catch block
            }
            if (this.transitionCount > 0) {
                int i;
                this.transitionTimes64 = new long[this.transitionCount];
                int idx = 0;
                if (transPre32 != null) {
                    i = 0;
                    while (i < transPre32.length / 2) {
                        this.transitionTimes64[idx] = ((long)transPre32[i * 2] & 0xFFFFFFFFL) << 32 | (long)transPre32[i * 2 + 1] & 0xFFFFFFFFL;
                        ++i;
                        ++idx;
                    }
                }
                if (trans32 != null) {
                    i = 0;
                    while (i < trans32.length) {
                        this.transitionTimes64[idx] = trans32[i];
                        ++i;
                        ++idx;
                    }
                }
                if (transPost32 != null) {
                    i = 0;
                    while (i < transPost32.length / 2) {
                        this.transitionTimes64[idx] = ((long)transPost32[i * 2] & 0xFFFFFFFFL) << 32 | (long)transPost32[i * 2 + 1] & 0xFFFFFFFFL;
                        ++i;
                        ++idx;
                    }
                }
            } else {
                this.transitionTimes64 = null;
            }
            r = res.get("typeOffsets");
            this.typeOffsets = r.getIntVector();
            if (this.typeOffsets.length < 2 || this.typeOffsets.length > 32766 || this.typeOffsets.length % 2 != 0) {
                throw new IllegalArgumentException("Invalid Format");
            }
            this.typeCount = this.typeOffsets.length / 2;
            if (this.transitionCount > 0) {
                r = res.get("typeMap");
                this.typeMapData = r.getBinary(null);
                if (this.typeMapData.length != this.transitionCount) {
                    throw new IllegalArgumentException("Invalid Format");
                }
            } else {
                this.typeMapData = null;
            }
            this.finalZone = null;
            this.finalStartYear = Integer.MAX_VALUE;
            this.finalStartMillis = Double.MAX_VALUE;
            String ruleID = null;
            try {
                ruleID = res.getString("finalRule");
                r = res.get("finalRaw");
                int ruleRaw = r.getInt() * 1000;
                r = OlsonTimeZone.loadRule(top, ruleID);
                int[] ruleData = r.getIntVector();
                if (ruleData == null || ruleData.length != 11) {
                    throw new IllegalArgumentException("Invalid Format");
                }
                this.finalZone = new SimpleTimeZone(ruleRaw, "", ruleData[0], ruleData[1], ruleData[2], ruleData[3] * 1000, ruleData[4], ruleData[5], ruleData[6], ruleData[7], ruleData[8] * 1000, ruleData[9], ruleData[10] * 1000);
                r = res.get("finalYear");
                this.finalStartYear = r.getInt();
                this.finalStartMillis = Grego.fieldsToDay(this.finalStartYear, 0, 1) * 86400000L;
            }
            catch (MissingResourceException e) {
                if (ruleID == null) break block25;
                throw new IllegalArgumentException("Invalid Format");
            }
        }
    }

    public OlsonTimeZone(String id) {
        super(id);
        UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", ZONEINFORES, ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        UResourceBundle res = ZoneMeta.openOlsonResource(top, id);
        this.construct(top, res);
        if (this.finalZone != null) {
            this.finalZone.setID(id);
        }
    }

    public void setID(String id) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen OlsonTimeZone instance.");
        }
        if (this.canonicalID == null) {
            this.canonicalID = OlsonTimeZone.getCanonicalID(this.getID());
            assert (this.canonicalID != null);
            if (this.canonicalID == null) {
                this.canonicalID = this.getID();
            }
        }
        if (this.finalZone != null) {
            this.finalZone.setID(id);
        }
        super.setID(id);
        this.transitionRulesInitialized = false;
    }

    private void getHistoricalOffset(long date, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt, int[] offsets) {
        if (this.transitionCount != 0) {
            long sec = Grego.floorDivide(date, 1000L);
            if (!local && sec < this.transitionTimes64[0]) {
                offsets[0] = this.initialRawOffset() * 1000;
                offsets[1] = this.initialDstOffset() * 1000;
            } else {
                int transIdx;
                for (transIdx = this.transitionCount - 1; transIdx >= 0; --transIdx) {
                    long transition = this.transitionTimes64[transIdx];
                    if (local) {
                        boolean stdToDst;
                        int offsetBefore = this.zoneOffsetAt(transIdx - 1);
                        boolean dstBefore = this.dstOffsetAt(transIdx - 1) != 0;
                        int offsetAfter = this.zoneOffsetAt(transIdx);
                        boolean dstAfter = this.dstOffsetAt(transIdx) != 0;
                        boolean dstToStd = dstBefore && !dstAfter;
                        boolean bl = stdToDst = !dstBefore && dstAfter;
                        transition = offsetAfter - offsetBefore >= 0 ? ((NonExistingTimeOpt & 3) == 1 && dstToStd || (NonExistingTimeOpt & 3) == 3 && stdToDst ? (transition += (long)offsetBefore) : ((NonExistingTimeOpt & 3) == 1 && stdToDst || (NonExistingTimeOpt & 3) == 3 && dstToStd ? (transition += (long)offsetAfter) : ((NonExistingTimeOpt & 0xC) == 12 ? (transition += (long)offsetBefore) : (transition += (long)offsetAfter)))) : ((DuplicatedTimeOpt & 3) == 1 && dstToStd || (DuplicatedTimeOpt & 3) == 3 && stdToDst ? (transition += (long)offsetAfter) : ((DuplicatedTimeOpt & 3) == 1 && stdToDst || (DuplicatedTimeOpt & 3) == 3 && dstToStd ? (transition += (long)offsetBefore) : ((DuplicatedTimeOpt & 0xC) == 4 ? (transition += (long)offsetBefore) : (transition += (long)offsetAfter))));
                    }
                    if (sec >= transition) break;
                }
                offsets[0] = this.rawOffsetAt(transIdx) * 1000;
                offsets[1] = this.dstOffsetAt(transIdx) * 1000;
            }
        } else {
            offsets[0] = this.initialRawOffset() * 1000;
            offsets[1] = this.initialDstOffset() * 1000;
        }
    }

    private int getInt(byte val) {
        return val & 0xFF;
    }

    private int zoneOffsetAt(int transIdx) {
        int typeIdx = transIdx >= 0 ? this.getInt(this.typeMapData[transIdx]) * 2 : 0;
        return this.typeOffsets[typeIdx] + this.typeOffsets[typeIdx + 1];
    }

    private int rawOffsetAt(int transIdx) {
        int typeIdx = transIdx >= 0 ? this.getInt(this.typeMapData[transIdx]) * 2 : 0;
        return this.typeOffsets[typeIdx];
    }

    private int dstOffsetAt(int transIdx) {
        int typeIdx = transIdx >= 0 ? this.getInt(this.typeMapData[transIdx]) * 2 : 0;
        return this.typeOffsets[typeIdx + 1];
    }

    private int initialRawOffset() {
        return this.typeOffsets[0];
    }

    private int initialDstOffset() {
        return this.typeOffsets[1];
    }

    public String toString() {
        int i;
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append('[');
        buf.append("transitionCount=" + this.transitionCount);
        buf.append(",typeCount=" + this.typeCount);
        buf.append(",transitionTimes=");
        if (this.transitionTimes64 != null) {
            buf.append('[');
            for (i = 0; i < this.transitionTimes64.length; ++i) {
                if (i > 0) {
                    buf.append(',');
                }
                buf.append(Long.toString(this.transitionTimes64[i]));
            }
            buf.append(']');
        } else {
            buf.append("null");
        }
        buf.append(",typeOffsets=");
        if (this.typeOffsets != null) {
            buf.append('[');
            for (i = 0; i < this.typeOffsets.length; ++i) {
                if (i > 0) {
                    buf.append(',');
                }
                buf.append(Integer.toString(this.typeOffsets[i]));
            }
            buf.append(']');
        } else {
            buf.append("null");
        }
        buf.append(",typeMapData=");
        if (this.typeMapData != null) {
            buf.append('[');
            for (i = 0; i < this.typeMapData.length; ++i) {
                if (i > 0) {
                    buf.append(',');
                }
                buf.append(Byte.toString(this.typeMapData[i]));
            }
        } else {
            buf.append("null");
        }
        buf.append(",finalStartYear=" + this.finalStartYear);
        buf.append(",finalStartMillis=" + this.finalStartMillis);
        buf.append(",finalZone=" + this.finalZone);
        buf.append(']');
        return buf.toString();
    }

    private static UResourceBundle loadRule(UResourceBundle top, String ruleid) {
        UResourceBundle r = top.get("Rules");
        r = r.get(ruleid);
        return r;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        OlsonTimeZone z = (OlsonTimeZone)obj;
        return Utility.arrayEquals(this.typeMapData, (Object)z.typeMapData) || this.finalStartYear == z.finalStartYear && (this.finalZone == null && z.finalZone == null || this.finalZone != null && z.finalZone != null && this.finalZone.equals(z.finalZone) && this.transitionCount == z.transitionCount && this.typeCount == z.typeCount && Utility.arrayEquals((Object)this.transitionTimes64, (Object)z.transitionTimes64) && Utility.arrayEquals(this.typeOffsets, (Object)z.typeOffsets) && Utility.arrayEquals(this.typeMapData, (Object)z.typeMapData));
    }

    public int hashCode() {
        int i;
        int ret = (int)((long)(this.finalStartYear ^ (this.finalStartYear >>> 4) + this.transitionCount ^ (this.transitionCount >>> 6) + this.typeCount) ^ (long)(this.typeCount >>> 8) + Double.doubleToLongBits(this.finalStartMillis) + (long)(this.finalZone == null ? 0 : this.finalZone.hashCode()) + (long)super.hashCode());
        if (this.transitionTimes64 != null) {
            for (i = 0; i < this.transitionTimes64.length; ++i) {
                ret = (int)((long)ret + (this.transitionTimes64[i] ^ this.transitionTimes64[i] >>> 8));
            }
        }
        for (i = 0; i < this.typeOffsets.length; ++i) {
            ret += this.typeOffsets[i] ^ this.typeOffsets[i] >>> 8;
        }
        if (this.typeMapData != null) {
            for (i = 0; i < this.typeMapData.length; ++i) {
                ret += this.typeMapData[i] & 0xFF;
            }
        }
        return ret;
    }

    public TimeZoneTransition getNextTransition(long base, boolean inclusive) {
        this.initTransitionRules();
        if (this.finalZone != null) {
            if (inclusive && base == this.firstFinalTZTransition.getTime()) {
                return this.firstFinalTZTransition;
            }
            if (base >= this.firstFinalTZTransition.getTime()) {
                if (this.finalZone.useDaylightTime()) {
                    return this.finalZoneWithStartYear.getNextTransition(base, inclusive);
                }
                return null;
            }
        }
        if (this.historicRules != null) {
            long t;
            int ttidx;
            for (ttidx = this.transitionCount - 1; ttidx >= this.firstTZTransitionIdx && base <= (t = this.transitionTimes64[ttidx] * 1000L) && (inclusive || base != t); --ttidx) {
            }
            if (ttidx == this.transitionCount - 1) {
                return this.firstFinalTZTransition;
            }
            if (ttidx < this.firstTZTransitionIdx) {
                return this.firstTZTransition;
            }
            TimeArrayTimeZoneRule to = this.historicRules[this.getInt(this.typeMapData[ttidx + 1])];
            TimeArrayTimeZoneRule from = this.historicRules[this.getInt(this.typeMapData[ttidx])];
            long startTime = this.transitionTimes64[ttidx + 1] * 1000L;
            if (from.getName().equals(to.getName()) && from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings()) {
                return this.getNextTransition(startTime, false);
            }
            return new TimeZoneTransition(startTime, from, to);
        }
        return null;
    }

    public TimeZoneTransition getPreviousTransition(long base, boolean inclusive) {
        this.initTransitionRules();
        if (this.finalZone != null) {
            if (inclusive && base == this.firstFinalTZTransition.getTime()) {
                return this.firstFinalTZTransition;
            }
            if (base > this.firstFinalTZTransition.getTime()) {
                if (this.finalZone.useDaylightTime()) {
                    return this.finalZoneWithStartYear.getPreviousTransition(base, inclusive);
                }
                return this.firstFinalTZTransition;
            }
        }
        if (this.historicRules != null) {
            long t;
            int ttidx;
            for (ttidx = this.transitionCount - 1; !(ttidx < this.firstTZTransitionIdx || base > (t = this.transitionTimes64[ttidx] * 1000L) || inclusive && base == t); --ttidx) {
            }
            if (ttidx < this.firstTZTransitionIdx) {
                return null;
            }
            if (ttidx == this.firstTZTransitionIdx) {
                return this.firstTZTransition;
            }
            TimeArrayTimeZoneRule to = this.historicRules[this.getInt(this.typeMapData[ttidx])];
            TimeArrayTimeZoneRule from = this.historicRules[this.getInt(this.typeMapData[ttidx - 1])];
            long startTime = this.transitionTimes64[ttidx] * 1000L;
            if (from.getName().equals(to.getName()) && from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings()) {
                return this.getPreviousTransition(startTime, false);
            }
            return new TimeZoneTransition(startTime, from, to);
        }
        return null;
    }

    public TimeZoneRule[] getTimeZoneRules() {
        this.initTransitionRules();
        int size = 1;
        if (this.historicRules != null) {
            for (int i = 0; i < this.historicRules.length; ++i) {
                if (this.historicRules[i] == null) continue;
                ++size;
            }
        }
        if (this.finalZone != null) {
            size = this.finalZone.useDaylightTime() ? (size += 2) : ++size;
        }
        TimeZoneRule[] rules = new TimeZoneRule[size];
        int idx = 0;
        rules[idx++] = this.initialRule;
        if (this.historicRules != null) {
            for (int i = 0; i < this.historicRules.length; ++i) {
                if (this.historicRules[i] == null) continue;
                rules[idx++] = this.historicRules[i];
            }
        }
        if (this.finalZone != null) {
            if (this.finalZone.useDaylightTime()) {
                TimeZoneRule[] stzr = this.finalZoneWithStartYear.getTimeZoneRules();
                rules[idx++] = stzr[1];
                rules[idx++] = stzr[2];
            } else {
                rules[idx++] = new TimeArrayTimeZoneRule(this.getID() + "(STD)", this.finalZone.getRawOffset(), 0, new long[]{(long)this.finalStartMillis}, 2);
            }
        }
        return rules;
    }

    private synchronized void initTransitionRules() {
        if (this.transitionRulesInitialized) {
            return;
        }
        this.initialRule = null;
        this.firstTZTransition = null;
        this.firstFinalTZTransition = null;
        this.historicRules = null;
        this.firstTZTransitionIdx = 0;
        this.finalZoneWithStartYear = null;
        String stdName = this.getID() + "(STD)";
        String dstName = this.getID() + "(DST)";
        int raw = this.initialRawOffset() * 1000;
        int dst = this.initialDstOffset() * 1000;
        this.initialRule = new InitialTimeZoneRule(dst == 0 ? stdName : dstName, raw, dst);
        if (this.transitionCount > 0) {
            int transitionIdx;
            for (transitionIdx = 0; transitionIdx < this.transitionCount && this.getInt(this.typeMapData[transitionIdx]) == 0; ++transitionIdx) {
                ++this.firstTZTransitionIdx;
            }
            if (transitionIdx != this.transitionCount) {
                int typeIdx;
                long[] times = new long[this.transitionCount];
                for (typeIdx = 0; typeIdx < this.typeCount; ++typeIdx) {
                    int nTimes = 0;
                    for (transitionIdx = this.firstTZTransitionIdx; transitionIdx < this.transitionCount; ++transitionIdx) {
                        long tt;
                        if (typeIdx != this.getInt(this.typeMapData[transitionIdx]) || !((double)(tt = this.transitionTimes64[transitionIdx] * 1000L) < this.finalStartMillis)) continue;
                        times[nTimes++] = tt;
                    }
                    if (nTimes <= 0) continue;
                    long[] startTimes = new long[nTimes];
                    System.arraycopy(times, 0, startTimes, 0, nTimes);
                    raw = this.typeOffsets[typeIdx * 2] * 1000;
                    dst = this.typeOffsets[typeIdx * 2 + 1] * 1000;
                    if (this.historicRules == null) {
                        this.historicRules = new TimeArrayTimeZoneRule[this.typeCount];
                    }
                    this.historicRules[typeIdx] = new TimeArrayTimeZoneRule(dst == 0 ? stdName : dstName, raw, dst, startTimes, 2);
                }
                typeIdx = this.getInt(this.typeMapData[this.firstTZTransitionIdx]);
                this.firstTZTransition = new TimeZoneTransition(this.transitionTimes64[this.firstTZTransitionIdx] * 1000L, this.initialRule, this.historicRules[typeIdx]);
            }
        }
        if (this.finalZone != null) {
            TimeZoneRule firstFinalRule;
            long startTime = (long)this.finalStartMillis;
            if (this.finalZone.useDaylightTime()) {
                this.finalZoneWithStartYear = (SimpleTimeZone)this.finalZone.clone();
                this.finalZoneWithStartYear.setStartYear(this.finalStartYear);
                TimeZoneTransition tzt = this.finalZoneWithStartYear.getNextTransition(startTime, false);
                firstFinalRule = tzt.getTo();
                startTime = tzt.getTime();
            } else {
                this.finalZoneWithStartYear = this.finalZone;
                firstFinalRule = new TimeArrayTimeZoneRule(this.finalZone.getID(), this.finalZone.getRawOffset(), 0, new long[]{startTime}, 2);
            }
            TimeZoneRule prevRule = null;
            if (this.transitionCount > 0) {
                prevRule = this.historicRules[this.getInt(this.typeMapData[this.transitionCount - 1])];
            }
            if (prevRule == null) {
                prevRule = this.initialRule;
            }
            this.firstFinalTZTransition = new TimeZoneTransition(startTime, prevRule, firstFinalRule);
        }
        this.transitionRulesInitialized = true;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            boolean initialized = false;
            String tzid = this.getID();
            if (tzid != null) {
                try {
                    UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", ZONEINFORES, ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                    UResourceBundle res = ZoneMeta.openOlsonResource(top, tzid);
                    this.construct(top, res);
                    if (this.finalZone != null) {
                        this.finalZone.setID(tzid);
                    }
                    initialized = true;
                }
                catch (Exception e) {
                    // empty catch block
                }
            }
            if (!initialized) {
                this.constructEmpty();
            }
        }
        this.transitionRulesInitialized = false;
    }

    public boolean isFrozen() {
        return this.isFrozen;
    }

    public TimeZone freeze() {
        this.isFrozen = true;
        return this;
    }

    public TimeZone cloneAsThawed() {
        OlsonTimeZone tz = (OlsonTimeZone)super.cloneAsThawed();
        if (this.finalZone != null) {
            this.finalZone.setID(this.getID());
            tz.finalZone = (SimpleTimeZone)this.finalZone.clone();
        }
        tz.isFrozen = false;
        return tz;
    }
}

