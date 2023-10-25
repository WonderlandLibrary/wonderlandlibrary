package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

public class RuleBasedTimeZone extends BasicTimeZone {
  private static final long serialVersionUID = 7580833058949327935L;
  
  private final InitialTimeZoneRule initialRule;
  
  private List<TimeZoneRule> historicRules;
  
  private AnnualTimeZoneRule[] finalRules;
  
  private transient List<TimeZoneTransition> historicTransitions;
  
  private transient boolean upToDate;
  
  private transient boolean isFrozen;
  
  public RuleBasedTimeZone(String id, InitialTimeZoneRule initialRule) {
    super(id);
    this.isFrozen = false;
    this.initialRule = initialRule;
  }
  
  public void addTransitionRule(TimeZoneRule rule) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify a frozen RuleBasedTimeZone instance."); 
    if (!rule.isTransitionRule())
      throw new IllegalArgumentException("Rule must be a transition rule"); 
    if (rule instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)rule).getEndYear() == Integer.MAX_VALUE) {
      if (this.finalRules == null) {
        this.finalRules = new AnnualTimeZoneRule[2];
        this.finalRules[0] = (AnnualTimeZoneRule)rule;
      } else if (this.finalRules[1] == null) {
        this.finalRules[1] = (AnnualTimeZoneRule)rule;
      } else {
        throw new IllegalStateException("Too many final rules");
      } 
    } else {
      if (this.historicRules == null)
        this.historicRules = new ArrayList<TimeZoneRule>(); 
      this.historicRules.add(rule);
    } 
    this.upToDate = false;
  }
  
  public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
    if (era == 0)
      year = 1 - year; 
    long time = Grego.fieldsToDay(year, month, day) * 86400000L + milliseconds;
    int[] offsets = new int[2];
    getOffset(time, true, 3, 1, offsets);
    return offsets[0] + offsets[1];
  }
  
  public void getOffset(long time, boolean local, int[] offsets) {
    getOffset(time, local, 4, 12, offsets);
  }
  
  public void getOffsetFromLocal(long date, int nonExistingTimeOpt, int duplicatedTimeOpt, int[] offsets) {
    getOffset(date, true, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
  }
  
  public int getRawOffset() {
    long now = System.currentTimeMillis();
    int[] offsets = new int[2];
    getOffset(now, false, offsets);
    return offsets[0];
  }
  
  public boolean inDaylightTime(Date date) {
    int[] offsets = new int[2];
    getOffset(date.getTime(), false, offsets);
    return (offsets[1] != 0);
  }
  
  public void setRawOffset(int offsetMillis) {
    throw new UnsupportedOperationException("setRawOffset in RuleBasedTimeZone is not supported.");
  }
  
  public boolean useDaylightTime() {
    long now = System.currentTimeMillis();
    int[] offsets = new int[2];
    getOffset(now, false, offsets);
    if (offsets[1] != 0)
      return true; 
    TimeZoneTransition tt = getNextTransition(now, false);
    if (tt != null && tt.getTo().getDSTSavings() != 0)
      return true; 
    return false;
  }
  
  public boolean observesDaylightTime() {
    long time = System.currentTimeMillis();
    int[] offsets = new int[2];
    getOffset(time, false, offsets);
    if (offsets[1] != 0)
      return true; 
    BitSet checkFinals = (this.finalRules == null) ? null : new BitSet(this.finalRules.length);
    while (true) {
      TimeZoneTransition tt = getNextTransition(time, false);
      if (tt == null)
        break; 
      TimeZoneRule toRule = tt.getTo();
      if (toRule.getDSTSavings() != 0)
        return true; 
      if (checkFinals != null) {
        for (int i = 0; i < this.finalRules.length; i++) {
          if (this.finalRules[i].equals(toRule))
            checkFinals.set(i); 
        } 
        if (checkFinals.cardinality() == this.finalRules.length)
          break; 
      } 
      time = tt.getTime();
    } 
    return false;
  }
  
  public boolean hasSameRules(TimeZone other) {
    if (this == other)
      return true; 
    if (!(other instanceof RuleBasedTimeZone))
      return false; 
    RuleBasedTimeZone otherRBTZ = (RuleBasedTimeZone)other;
    if (!this.initialRule.isEquivalentTo(otherRBTZ.initialRule))
      return false; 
    if (this.finalRules != null && otherRBTZ.finalRules != null) {
      for (int i = 0; i < this.finalRules.length; i++) {
        if (this.finalRules[i] != null || otherRBTZ.finalRules[i] != null)
          if (this.finalRules[i] == null || otherRBTZ.finalRules[i] == null || !this.finalRules[i].isEquivalentTo(otherRBTZ.finalRules[i]))
            return false;  
      } 
    } else if (this.finalRules != null || otherRBTZ.finalRules != null) {
      return false;
    } 
    if (this.historicRules != null && otherRBTZ.historicRules != null) {
      if (this.historicRules.size() != otherRBTZ.historicRules.size())
        return false; 
      for (TimeZoneRule rule : this.historicRules) {
        boolean foundSameRule = false;
        for (TimeZoneRule orule : otherRBTZ.historicRules) {
          if (rule.isEquivalentTo(orule)) {
            foundSameRule = true;
            break;
          } 
        } 
        if (!foundSameRule)
          return false; 
      } 
    } else if (this.historicRules != null || otherRBTZ.historicRules != null) {
      return false;
    } 
    return true;
  }
  
  public TimeZoneRule[] getTimeZoneRules() {
    int size = 1;
    if (this.historicRules != null)
      size += this.historicRules.size(); 
    if (this.finalRules != null)
      if (this.finalRules[1] != null) {
        size += 2;
      } else {
        size++;
      }  
    TimeZoneRule[] rules = new TimeZoneRule[size];
    rules[0] = this.initialRule;
    int idx = 1;
    if (this.historicRules != null)
      for (; idx < this.historicRules.size() + 1; idx++)
        rules[idx] = this.historicRules.get(idx - 1);  
    if (this.finalRules != null) {
      rules[idx++] = this.finalRules[0];
      if (this.finalRules[1] != null)
        rules[idx] = this.finalRules[1]; 
    } 
    return rules;
  }
  
  public TimeZoneTransition getNextTransition(long base, boolean inclusive) {
    complete();
    if (this.historicTransitions == null)
      return null; 
    boolean isFinal = false;
    TimeZoneTransition result = null;
    TimeZoneTransition tzt = this.historicTransitions.get(0);
    long tt = tzt.getTime();
    if (tt > base || (inclusive && tt == base)) {
      result = tzt;
    } else {
      int idx = this.historicTransitions.size() - 1;
      tzt = this.historicTransitions.get(idx);
      tt = tzt.getTime();
      if (inclusive && tt == base) {
        result = tzt;
      } else if (tt <= base) {
        if (this.finalRules != null) {
          Date start0 = this.finalRules[0].getNextStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), inclusive);
          Date start1 = this.finalRules[1].getNextStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), inclusive);
          if (start1.after(start0)) {
            tzt = new TimeZoneTransition(start0.getTime(), this.finalRules[1], this.finalRules[0]);
          } else {
            tzt = new TimeZoneTransition(start1.getTime(), this.finalRules[0], this.finalRules[1]);
          } 
          result = tzt;
          isFinal = true;
        } else {
          return null;
        } 
      } else {
        idx--;
        TimeZoneTransition prev = tzt;
        while (idx > 0) {
          tzt = this.historicTransitions.get(idx);
          tt = tzt.getTime();
          if (tt < base || (!inclusive && tt == base))
            break; 
          idx--;
          prev = tzt;
        } 
        result = prev;
      } 
    } 
    if (result != null) {
      TimeZoneRule from = result.getFrom();
      TimeZoneRule to = result.getTo();
      if (from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings()) {
        if (isFinal)
          return null; 
        result = getNextTransition(result.getTime(), false);
      } 
    } 
    return result;
  }
  
  public TimeZoneTransition getPreviousTransition(long base, boolean inclusive) {
    complete();
    if (this.historicTransitions == null)
      return null; 
    TimeZoneTransition result = null;
    TimeZoneTransition tzt = this.historicTransitions.get(0);
    long tt = tzt.getTime();
    if (inclusive && tt == base) {
      result = tzt;
    } else {
      if (tt >= base)
        return null; 
      int idx = this.historicTransitions.size() - 1;
      tzt = this.historicTransitions.get(idx);
      tt = tzt.getTime();
      if (inclusive && tt == base) {
        result = tzt;
      } else if (tt < base) {
        if (this.finalRules != null) {
          Date start0 = this.finalRules[0].getPreviousStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), inclusive);
          Date start1 = this.finalRules[1].getPreviousStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), inclusive);
          if (start1.before(start0)) {
            tzt = new TimeZoneTransition(start0.getTime(), this.finalRules[1], this.finalRules[0]);
          } else {
            tzt = new TimeZoneTransition(start1.getTime(), this.finalRules[0], this.finalRules[1]);
          } 
        } 
        result = tzt;
      } else {
        idx--;
        while (idx >= 0) {
          tzt = this.historicTransitions.get(idx);
          tt = tzt.getTime();
          if (tt < base || (inclusive && tt == base))
            break; 
          idx--;
        } 
        result = tzt;
      } 
    } 
    if (result != null) {
      TimeZoneRule from = result.getFrom();
      TimeZoneRule to = result.getTo();
      if (from.getRawOffset() == to.getRawOffset() && from.getDSTSavings() == to.getDSTSavings())
        result = getPreviousTransition(result.getTime(), false); 
    } 
    return result;
  }
  
  public Object clone() {
    if (isFrozen())
      return this; 
    return cloneAsThawed();
  }
  
  private void complete() {
    if (this.upToDate)
      return; 
    if (this.finalRules != null && this.finalRules[1] == null)
      throw new IllegalStateException("Incomplete final rules"); 
    if (this.historicRules != null || this.finalRules != null) {
      TimeZoneRule curRule = this.initialRule;
      long lastTransitionTime = -184303902528000000L;
      if (this.historicRules != null) {
        BitSet done = new BitSet(this.historicRules.size());
        while (true) {
          int curStdOffset = curRule.getRawOffset();
          int curDstSavings = curRule.getDSTSavings();
          long nextTransitionTime = 183882168921600000L;
          TimeZoneRule nextRule = null;
          int i;
          for (i = 0; i < this.historicRules.size(); i++) {
            if (!done.get(i)) {
              TimeZoneRule r = this.historicRules.get(i);
              Date d = r.getNextStart(lastTransitionTime, curStdOffset, curDstSavings, false);
              if (d == null) {
                done.set(i);
              } else if (r != curRule && (!r.getName().equals(curRule.getName()) || r.getRawOffset() != curRule.getRawOffset() || r.getDSTSavings() != curRule.getDSTSavings())) {
                long tt = d.getTime();
                if (tt < nextTransitionTime) {
                  nextTransitionTime = tt;
                  nextRule = r;
                } 
              } 
            } 
          } 
          if (nextRule == null) {
            boolean bDoneAll = true;
            for (int j = 0; j < this.historicRules.size(); j++) {
              if (!done.get(j)) {
                bDoneAll = false;
                break;
              } 
            } 
            if (bDoneAll)
              break; 
          } 
          if (this.finalRules != null)
            for (i = 0; i < 2; i++) {
              if (this.finalRules[i] != curRule) {
                Date d = this.finalRules[i].getNextStart(lastTransitionTime, curStdOffset, curDstSavings, false);
                if (d != null) {
                  long tt = d.getTime();
                  if (tt < nextTransitionTime) {
                    nextTransitionTime = tt;
                    nextRule = this.finalRules[i];
                  } 
                } 
              } 
            }  
          if (nextRule == null)
            break; 
          if (this.historicTransitions == null)
            this.historicTransitions = new ArrayList<TimeZoneTransition>(); 
          this.historicTransitions.add(new TimeZoneTransition(nextTransitionTime, curRule, nextRule));
          lastTransitionTime = nextTransitionTime;
          curRule = nextRule;
        } 
      } 
      if (this.finalRules != null) {
        if (this.historicTransitions == null)
          this.historicTransitions = new ArrayList<TimeZoneTransition>(); 
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
    complete();
    TimeZoneRule rule = null;
    if (this.historicTransitions == null) {
      rule = this.initialRule;
    } else {
      long tstart = getTransitionTime(this.historicTransitions.get(0), local, NonExistingTimeOpt, DuplicatedTimeOpt);
      if (time < tstart) {
        rule = this.initialRule;
      } else {
        int idx = this.historicTransitions.size() - 1;
        long tend = getTransitionTime(this.historicTransitions.get(idx), local, NonExistingTimeOpt, DuplicatedTimeOpt);
        if (time > tend) {
          if (this.finalRules != null)
            rule = findRuleInFinal(time, local, NonExistingTimeOpt, DuplicatedTimeOpt); 
          if (rule == null)
            rule = ((TimeZoneTransition)this.historicTransitions.get(idx)).getTo(); 
        } else {
          while (idx >= 0 && time < getTransitionTime(this.historicTransitions.get(idx), local, NonExistingTimeOpt, DuplicatedTimeOpt))
            idx--; 
          rule = ((TimeZoneTransition)this.historicTransitions.get(idx)).getTo();
        } 
      } 
    } 
    offsets[0] = rule.getRawOffset();
    offsets[1] = rule.getDSTSavings();
  }
  
  private TimeZoneRule findRuleInFinal(long time, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt) {
    if (this.finalRules == null || this.finalRules.length != 2 || this.finalRules[0] == null || this.finalRules[1] == null)
      return null; 
    long base = time;
    if (local) {
      int localDelta = getLocalDelta(this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
      base -= localDelta;
    } 
    Date start0 = this.finalRules[0].getPreviousStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), true);
    base = time;
    if (local) {
      int localDelta = getLocalDelta(this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
      base -= localDelta;
    } 
    Date start1 = this.finalRules[1].getPreviousStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), true);
    if (start0 == null || start1 == null) {
      if (start0 != null)
        return this.finalRules[0]; 
      if (start1 != null)
        return this.finalRules[1]; 
      return null;
    } 
    return start0.after(start1) ? this.finalRules[0] : this.finalRules[1];
  }
  
  private static long getTransitionTime(TimeZoneTransition tzt, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt) {
    long time = tzt.getTime();
    if (local)
      time += getLocalDelta(tzt.getFrom().getRawOffset(), tzt.getFrom().getDSTSavings(), tzt.getTo().getRawOffset(), tzt.getTo().getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt); 
    return time;
  }
  
  private static int getLocalDelta(int rawBefore, int dstBefore, int rawAfter, int dstAfter, int NonExistingTimeOpt, int DuplicatedTimeOpt) {
    int delta = 0;
    int offsetBefore = rawBefore + dstBefore;
    int offsetAfter = rawAfter + dstAfter;
    boolean dstToStd = (dstBefore != 0 && dstAfter == 0);
    boolean stdToDst = (dstBefore == 0 && dstAfter != 0);
    if (offsetAfter - offsetBefore >= 0) {
      if (((NonExistingTimeOpt & 0x3) == 1 && dstToStd) || ((NonExistingTimeOpt & 0x3) == 3 && stdToDst)) {
        delta = offsetBefore;
      } else if (((NonExistingTimeOpt & 0x3) == 1 && stdToDst) || ((NonExistingTimeOpt & 0x3) == 3 && dstToStd)) {
        delta = offsetAfter;
      } else if ((NonExistingTimeOpt & 0xC) == 12) {
        delta = offsetBefore;
      } else {
        delta = offsetAfter;
      } 
    } else if (((DuplicatedTimeOpt & 0x3) == 1 && dstToStd) || ((DuplicatedTimeOpt & 0x3) == 3 && stdToDst)) {
      delta = offsetAfter;
    } else if (((DuplicatedTimeOpt & 0x3) == 1 && stdToDst) || ((DuplicatedTimeOpt & 0x3) == 3 && dstToStd)) {
      delta = offsetBefore;
    } else if ((DuplicatedTimeOpt & 0xC) == 4) {
      delta = offsetBefore;
    } else {
      delta = offsetAfter;
    } 
    return delta;
  }
  
  public boolean isFrozen() {
    return this.isFrozen;
  }
  
  public TimeZone freeze() {
    complete();
    this.isFrozen = true;
    return this;
  }
  
  public TimeZone cloneAsThawed() {
    RuleBasedTimeZone tz = (RuleBasedTimeZone)super.cloneAsThawed();
    if (this.historicRules != null)
      tz.historicRules = new ArrayList<TimeZoneRule>(this.historicRules); 
    if (this.finalRules != null)
      tz.finalRules = (AnnualTimeZoneRule[])this.finalRules.clone(); 
    tz.isFrozen = false;
    return tz;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\RuleBasedTimeZone.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */