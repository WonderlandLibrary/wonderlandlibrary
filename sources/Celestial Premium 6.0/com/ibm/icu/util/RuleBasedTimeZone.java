/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import com.ibm.icu.util.AnnualTimeZoneRule;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.InitialTimeZoneRule;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.TimeZoneRule;
import com.ibm.icu.util.TimeZoneTransition;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

public class RuleBasedTimeZone
extends BasicTimeZone {
    private static final long serialVersionUID = 7580833058949327935L;
    private final InitialTimeZoneRule initialRule;
    private List<TimeZoneRule> historicRules;
    private AnnualTimeZoneRule[] finalRules;
    private transient List<TimeZoneTransition> historicTransitions;
    private transient boolean upToDate;
    private transient boolean isFrozen = false;

    public RuleBasedTimeZone(String id, InitialTimeZoneRule initialRule) {
        super(id);
        this.initialRule = initialRule;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void addTransitionRule(TimeZoneRule rule) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen RuleBasedTimeZone instance.");
        }
        if (!rule.isTransitionRule()) {
            throw new IllegalArgumentException("Rule must be a transition rule");
        }
        if (rule instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)rule).getEndYear() == Integer.MAX_VALUE) {
            if (this.finalRules == null) {
                this.finalRules = new AnnualTimeZoneRule[2];
                this.finalRules[0] = (AnnualTimeZoneRule)rule;
            } else {
                if (this.finalRules[1] != null) throw new IllegalStateException("Too many final rules");
                this.finalRules[1] = (AnnualTimeZoneRule)rule;
            }
        } else {
            if (this.historicRules == null) {
                this.historicRules = new ArrayList<TimeZoneRule>();
            }
            this.historicRules.add(rule);
        }
        this.upToDate = false;
    }

    public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
        if (era == 0) {
            year = 1 - year;
        }
        long time = Grego.fieldsToDay(year, month, day) * 86400000L + (long)milliseconds;
        int[] offsets = new int[2];
        this.getOffset(time, true, 3, 1, offsets);
        return offsets[0] + offsets[1];
    }

    public void getOffset(long time, boolean local, int[] offsets) {
        this.getOffset(time, local, 4, 12, offsets);
    }

    public void getOffsetFromLocal(long date, int nonExistingTimeOpt, int duplicatedTimeOpt, int[] offsets) {
        this.getOffset(date, true, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
    }

    public int getRawOffset() {
        long now = System.currentTimeMillis();
        int[] offsets = new int[2];
        this.getOffset(now, false, offsets);
        return offsets[0];
    }

    public boolean inDaylightTime(Date date) {
        int[] offsets = new int[2];
        this.getOffset(date.getTime(), false, offsets);
        return offsets[1] != 0;
    }

    public void setRawOffset(int offsetMillis) {
        throw new UnsupportedOperationException("setRawOffset in RuleBasedTimeZone is not supported.");
    }

    public boolean useDaylightTime() {
        long now = System.currentTimeMillis();
        int[] offsets = new int[2];
        this.getOffset(now, false, offsets);
        if (offsets[1] != 0) {
            return true;
        }
        TimeZoneTransition tt = this.getNextTransition(now, false);
        return tt != null && tt.getTo().getDSTSavings() != 0;
    }

    public boolean observesDaylightTime() {
        TimeZoneTransition tt;
        BitSet checkFinals;
        long time = System.currentTimeMillis();
        int[] offsets = new int[2];
        this.getOffset(time, false, offsets);
        if (offsets[1] != 0) {
            return true;
        }
        BitSet bitSet = checkFinals = this.finalRules == null ? null : new BitSet(this.finalRules.length);
        while ((tt = this.getNextTransition(time, false)) != null) {
            TimeZoneRule toRule = tt.getTo();
            if (toRule.getDSTSavings() != 0) {
                return true;
            }
            if (checkFinals != null) {
                for (int i = 0; i < this.finalRules.length; ++i) {
                    if (!this.finalRules[i].equals(toRule)) continue;
                    checkFinals.set(i);
                }
                if (checkFinals.cardinality() == this.finalRules.length) break;
            }
            time = tt.getTime();
        }
        return false;
    }

    public boolean hasSameRules(TimeZone other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RuleBasedTimeZone)) {
            return false;
        }
        RuleBasedTimeZone otherRBTZ = (RuleBasedTimeZone)other;
        if (!this.initialRule.isEquivalentTo(otherRBTZ.initialRule)) {
            return false;
        }
        if (this.finalRules != null && otherRBTZ.finalRules != null) {
            for (int i = 0; i < this.finalRules.length; ++i) {
                if (this.finalRules[i] == null && otherRBTZ.finalRules[i] == null || this.finalRules[i] != null && otherRBTZ.finalRules[i] != null && this.finalRules[i].isEquivalentTo(otherRBTZ.finalRules[i])) continue;
                return false;
            }
        } else if (this.finalRules != null || otherRBTZ.finalRules != null) {
            return false;
        }
        if (this.historicRules != null && otherRBTZ.historicRules != null) {
            if (this.historicRules.size() != otherRBTZ.historicRules.size()) {
                return false;
            }
            for (TimeZoneRule rule : this.historicRules) {
                boolean foundSameRule = false;
                for (TimeZoneRule orule : otherRBTZ.historicRules) {
                    if (!rule.isEquivalentTo(orule)) continue;
                    foundSameRule = true;
                    break;
                }
                if (foundSameRule) continue;
                return false;
            }
        } else if (this.historicRules != null || otherRBTZ.historicRules != null) {
            return false;
        }
        return true;
    }

    public TimeZoneRule[] getTimeZoneRules() {
        int size = 1;
        if (this.historicRules != null) {
            size += this.historicRules.size();
        }
        if (this.finalRules != null) {
            size = this.finalRules[1] != null ? (size += 2) : ++size;
        }
        TimeZoneRule[] rules = new TimeZoneRule[size];
        rules[0] = this.initialRule;
        if (this.historicRules != null) {
            for (int idx = 1; idx < this.historicRules.size() + 1; ++idx) {
                rules[idx] = this.historicRules.get(idx - 1);
            }
        }
        if (this.finalRules != null) {
            rules[idx++] = this.finalRules[0];
            if (this.finalRules[1] != null) {
                rules[idx] = this.finalRules[1];
            }
        }
        return rules;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public TimeZoneTransition getNextTransition(long base, boolean inclusive) {
        this.complete();
        if (this.historicTransitions == null) {
            return null;
        }
        boolean isFinal = false;
        TimeZoneTransition result = null;
        TimeZoneTransition tzt = this.historicTransitions.get(0);
        long tt = tzt.getTime();
        if (tt > base || inclusive && tt == base) {
            result = tzt;
        } else {
            int idx = this.historicTransitions.size() - 1;
            tzt = this.historicTransitions.get(idx);
            tt = tzt.getTime();
            if (inclusive && tt == base) {
                result = tzt;
            } else if (tt <= base) {
                if (this.finalRules == null) return null;
                Date start0 = this.finalRules[0].getNextStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), inclusive);
                Date start1 = this.finalRules[1].getNextStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), inclusive);
                tzt = start1.after(start0) ? new TimeZoneTransition(start0.getTime(), this.finalRules[1], this.finalRules[0]) : new TimeZoneTransition(start1.getTime(), this.finalRules[0], this.finalRules[1]);
                result = tzt;
                isFinal = true;
            } else {
                --idx;
                TimeZoneTransition prev = tzt;
                while (idx > 0 && (tt = (tzt = this.historicTransitions.get(idx)).getTime()) >= base && (inclusive || tt != base)) {
                    --idx;
                    prev = tzt;
                }
                result = prev;
            }
        }
        if (result == null) return result;
        TimeZoneRule from = result.getFrom();
        TimeZoneRule to = result.getTo();
        if (from.getRawOffset() != to.getRawOffset()) return result;
        if (from.getDSTSavings() != to.getDSTSavings()) return result;
        if (!isFinal) return this.getNextTransition(result.getTime(), false);
        return null;
    }

    public TimeZoneTransition getPreviousTransition(long base, boolean inclusive) {
        this.complete();
        if (this.historicTransitions == null) {
            return null;
        }
        TimeZoneTransition result = null;
        TimeZoneTransition tzt = this.historicTransitions.get(0);
        long tt = tzt.getTime();
        if (inclusive && tt == base) {
            result = tzt;
        } else {
            if (tt >= base) {
                return null;
            }
            int idx = this.historicTransitions.size() - 1;
            tzt = this.historicTransitions.get(idx);
            tt = tzt.getTime();
            if (inclusive && tt == base) {
                result = tzt;
            } else if (tt < base) {
                if (this.finalRules != null) {
                    Date start0 = this.finalRules[0].getPreviousStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), inclusive);
                    Date start1 = this.finalRules[1].getPreviousStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), inclusive);
                    tzt = start1.before(start0) ? new TimeZoneTransition(start0.getTime(), this.finalRules[1], this.finalRules[0]) : new TimeZoneTransition(start1.getTime(), this.finalRules[0], this.finalRules[1]);
                }
                result = tzt;
            } else {
                --idx;
                while (!(idx < 0 || (tt = (tzt = this.historicTransitions.get(idx)).getTime()) < base || inclusive && tt == base)) {
                    --idx;
                }
                result = tzt;
            }
        }
        if (result != null) {
            TimeZoneRule from = result.getFrom();
            TimeZoneRule to = result.getTo();
            if (from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings()) {
                result = this.getPreviousTransition(result.getTime(), false);
            }
        }
        return result;
    }

    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }

    private void complete() {
        if (this.upToDate) {
            return;
        }
        if (this.finalRules != null && this.finalRules[1] == null) {
            throw new IllegalStateException("Incomplete final rules");
        }
        if (this.historicRules != null || this.finalRules != null) {
            InitialTimeZoneRule curRule = this.initialRule;
            long lastTransitionTime = -184303902528000000L;
            if (this.historicRules != null) {
                BitSet done = new BitSet(this.historicRules.size());
                while (true) {
                    long tt;
                    Date d;
                    int i;
                    int curStdOffset = curRule.getRawOffset();
                    int curDstSavings = curRule.getDSTSavings();
                    long nextTransitionTime = 183882168921600000L;
                    TimeZoneRule nextRule = null;
                    for (i = 0; i < this.historicRules.size(); ++i) {
                        if (done.get(i)) continue;
                        TimeZoneRule r = this.historicRules.get(i);
                        d = r.getNextStart(lastTransitionTime, curStdOffset, curDstSavings, false);
                        if (d == null) {
                            done.set(i);
                            continue;
                        }
                        if (r == curRule || r.getName().equals(curRule.getName()) && r.getRawOffset() == curRule.getRawOffset() && r.getDSTSavings() == curRule.getDSTSavings() || (tt = d.getTime()) >= nextTransitionTime) continue;
                        nextTransitionTime = tt;
                        nextRule = r;
                    }
                    if (nextRule == null) {
                        boolean bDoneAll = true;
                        for (int j = 0; j < this.historicRules.size(); ++j) {
                            if (done.get(j)) continue;
                            bDoneAll = false;
                            break;
                        }
                        if (bDoneAll) break;
                    }
                    if (this.finalRules != null) {
                        for (i = 0; i < 2; ++i) {
                            if (this.finalRules[i] == curRule || (d = this.finalRules[i].getNextStart(lastTransitionTime, curStdOffset, curDstSavings, false)) == null || (tt = d.getTime()) >= nextTransitionTime) continue;
                            nextTransitionTime = tt;
                            nextRule = this.finalRules[i];
                        }
                    }
                    if (nextRule == null) break;
                    if (this.historicTransitions == null) {
                        this.historicTransitions = new ArrayList<TimeZoneTransition>();
                    }
                    this.historicTransitions.add(new TimeZoneTransition(nextTransitionTime, curRule, nextRule));
                    lastTransitionTime = nextTransitionTime;
                    curRule = nextRule;
                }
            }
            if (this.finalRules != null) {
                if (this.historicTransitions == null) {
                    this.historicTransitions = new ArrayList<TimeZoneTransition>();
                }
                Date d0 = this.finalRules[0].getNextStart(lastTransitionTime, curRule.getRawOffset(), curRule.getDSTSavings(), false);
                Date d1 = this.finalRules[1].getNextStart(lastTransitionTime, curRule.getRawOffset(), curRule.getDSTSavings(), false);
                if (d1.after(d0)) {
                    this.historicTransitions.add(new TimeZoneTransition(d0.getTime(), curRule, this.finalRules[0]));
                    d1 = this.finalRules[1].getNextStart(d0.getTime(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), false);
                    this.historicTransitions.add(new TimeZoneTransition(d1.getTime(), this.finalRules[0], this.finalRules[1]));
                } else {
                    this.historicTransitions.add(new TimeZoneTransition(d1.getTime(), curRule, this.finalRules[1]));
                    d0 = this.finalRules[0].getNextStart(d1.getTime(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), false);
                    this.historicTransitions.add(new TimeZoneTransition(d0.getTime(), this.finalRules[1], this.finalRules[0]));
                }
            }
        }
        this.upToDate = true;
    }

    private void getOffset(long time, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt, int[] offsets) {
        this.complete();
        TimeZoneRule rule = null;
        if (this.historicTransitions == null) {
            rule = this.initialRule;
        } else {
            long tstart = RuleBasedTimeZone.getTransitionTime(this.historicTransitions.get(0), local, NonExistingTimeOpt, DuplicatedTimeOpt);
            if (time < tstart) {
                rule = this.initialRule;
            } else {
                int idx = this.historicTransitions.size() - 1;
                long tend = RuleBasedTimeZone.getTransitionTime(this.historicTransitions.get(idx), local, NonExistingTimeOpt, DuplicatedTimeOpt);
                if (time > tend) {
                    if (this.finalRules != null) {
                        rule = this.findRuleInFinal(time, local, NonExistingTimeOpt, DuplicatedTimeOpt);
                    }
                    if (rule == null) {
                        rule = this.historicTransitions.get(idx).getTo();
                    }
                } else {
                    while (idx >= 0 && time < RuleBasedTimeZone.getTransitionTime(this.historicTransitions.get(idx), local, NonExistingTimeOpt, DuplicatedTimeOpt)) {
                        --idx;
                    }
                    rule = this.historicTransitions.get(idx).getTo();
                }
            }
        }
        offsets[0] = rule.getRawOffset();
        offsets[1] = rule.getDSTSavings();
    }

    private TimeZoneRule findRuleInFinal(long time, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt) {
        int localDelta;
        if (this.finalRules == null || this.finalRules.length != 2 || this.finalRules[0] == null || this.finalRules[1] == null) {
            return null;
        }
        long base = time;
        if (local) {
            localDelta = RuleBasedTimeZone.getLocalDelta(this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
            base -= (long)localDelta;
        }
        Date start0 = this.finalRules[0].getPreviousStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), true);
        base = time;
        if (local) {
            localDelta = RuleBasedTimeZone.getLocalDelta(this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
            base -= (long)localDelta;
        }
        Date start1 = this.finalRules[1].getPreviousStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), true);
        if (start0 == null || start1 == null) {
            if (start0 != null) {
                return this.finalRules[0];
            }
            if (start1 != null) {
                return this.finalRules[1];
            }
            return null;
        }
        return start0.after(start1) ? this.finalRules[0] : this.finalRules[1];
    }

    private static long getTransitionTime(TimeZoneTransition tzt, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt) {
        long time = tzt.getTime();
        if (local) {
            time += (long)RuleBasedTimeZone.getLocalDelta(tzt.getFrom().getRawOffset(), tzt.getFrom().getDSTSavings(), tzt.getTo().getRawOffset(), tzt.getTo().getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
        }
        return time;
    }

    private static int getLocalDelta(int rawBefore, int dstBefore, int rawAfter, int dstAfter, int NonExistingTimeOpt, int DuplicatedTimeOpt) {
        boolean stdToDst;
        int delta = 0;
        int offsetBefore = rawBefore + dstBefore;
        int offsetAfter = rawAfter + dstAfter;
        boolean dstToStd = dstBefore != 0 && dstAfter == 0;
        boolean bl = stdToDst = dstBefore == 0 && dstAfter != 0;
        delta = offsetAfter - offsetBefore >= 0 ? ((NonExistingTimeOpt & 3) == 1 && dstToStd || (NonExistingTimeOpt & 3) == 3 && stdToDst ? offsetBefore : ((NonExistingTimeOpt & 3) == 1 && stdToDst || (NonExistingTimeOpt & 3) == 3 && dstToStd ? offsetAfter : ((NonExistingTimeOpt & 0xC) == 12 ? offsetBefore : offsetAfter))) : ((DuplicatedTimeOpt & 3) == 1 && dstToStd || (DuplicatedTimeOpt & 3) == 3 && stdToDst ? offsetAfter : ((DuplicatedTimeOpt & 3) == 1 && stdToDst || (DuplicatedTimeOpt & 3) == 3 && dstToStd ? offsetBefore : ((DuplicatedTimeOpt & 0xC) == 4 ? offsetBefore : offsetAfter)));
        return delta;
    }

    public boolean isFrozen() {
        return this.isFrozen;
    }

    public TimeZone freeze() {
        this.complete();
        this.isFrozen = true;
        return this;
    }

    public TimeZone cloneAsThawed() {
        RuleBasedTimeZone tz = (RuleBasedTimeZone)super.cloneAsThawed();
        if (this.historicRules != null) {
            tz.historicRules = new ArrayList<TimeZoneRule>(this.historicRules);
        }
        if (this.finalRules != null) {
            tz.finalRules = (AnnualTimeZoneRule[])this.finalRules.clone();
        }
        tz.isFrozen = false;
        return tz;
    }
}

