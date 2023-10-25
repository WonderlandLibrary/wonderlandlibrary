/*
 * Decompiled with CFR 0.150.
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

    public Date firstAfter(Date start) {
        int index = this.startIndex(start);
        if (index == this.ranges.size()) {
            index = 0;
        }
        Date result = null;
        Range r = this.rangeAt(index);
        Range e = this.rangeAt(index + 1);
        if (r != null && r.rule != null) {
            result = e != null ? r.rule.firstBetween(start, e.start) : r.rule.firstAfter(start);
        }
        return result;
    }

    public Date firstBetween(Date start, Date end) {
        if (end == null) {
            return this.firstAfter(start);
        }
        int index = this.startIndex(start);
        Date result = null;
        Range next = this.rangeAt(index);
        while (result == null && next != null && !next.start.after(end)) {
            Range r = next;
            next = this.rangeAt(index + 1);
            if (r.rule == null) continue;
            Date e = next != null && !next.start.after(end) ? next.start : end;
            result = r.rule.firstBetween(start, e);
        }
        return result;
    }

    public boolean isOn(Date date) {
        Range r = this.rangeAt(this.startIndex(date));
        return r != null && r.rule != null && r.rule.isOn(date);
    }

    public boolean isBetween(Date start, Date end) {
        return this.firstBetween(start, end) == null;
    }

    private int startIndex(Date start) {
        int lastIndex = this.ranges.size();
        int i = 0;
        while (i < this.ranges.size()) {
            Range r = this.ranges.get(i);
            if (start.before(r.start)) break;
            lastIndex = i++;
        }
        return lastIndex;
    }

    private Range rangeAt(int index) {
        return index < this.ranges.size() ? this.ranges.get(index) : null;
    }
}

