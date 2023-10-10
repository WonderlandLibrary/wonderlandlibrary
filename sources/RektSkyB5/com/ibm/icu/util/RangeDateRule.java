/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.DateRule;
import com.ibm.icu.util.Range;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RangeDateRule
implements DateRule {
    List<Range> ranges = new ArrayList<Range>(2);

    public void add(DateRule rule) {
        this.add(new Date(Long.MIN_VALUE), rule);
    }

    public void add(Date start, DateRule rule) {
        this.ranges.add(new Range(start, rule));
    }

    @Override
    public Date firstAfter(Date start) {
        int index = this.startIndex(start);
        if (index == this.ranges.size()) {
            index = 0;
        }
        Date result = null;
        Range r2 = this.rangeAt(index);
        Range e2 = this.rangeAt(index + 1);
        if (r2 != null && r2.rule != null) {
            result = e2 != null ? r2.rule.firstBetween(start, e2.start) : r2.rule.firstAfter(start);
        }
        return result;
    }

    @Override
    public Date firstBetween(Date start, Date end) {
        if (end == null) {
            return this.firstAfter(start);
        }
        int index = this.startIndex(start);
        Date result = null;
        Range next = this.rangeAt(index);
        while (result == null && next != null && !next.start.after(end)) {
            Range r2 = next;
            next = this.rangeAt(index + 1);
            if (r2.rule == null) continue;
            Date e2 = next != null && !next.start.after(end) ? next.start : end;
            result = r2.rule.firstBetween(start, e2);
        }
        return result;
    }

    @Override
    public boolean isOn(Date date) {
        Range r2 = this.rangeAt(this.startIndex(date));
        return r2 != null && r2.rule != null && r2.rule.isOn(date);
    }

    @Override
    public boolean isBetween(Date start, Date end) {
        return this.firstBetween(start, end) == null;
    }

    private int startIndex(Date start) {
        int lastIndex = this.ranges.size();
        int i2 = 0;
        while (i2 < this.ranges.size()) {
            Range r2 = this.ranges.get(i2);
            if (start.before(r2.start)) break;
            lastIndex = i2++;
        }
        return lastIndex;
    }

    private Range rangeAt(int index) {
        return index < this.ranges.size() ? this.ranges.get(index) : null;
    }
}

