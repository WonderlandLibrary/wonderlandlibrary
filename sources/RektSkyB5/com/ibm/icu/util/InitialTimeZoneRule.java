/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.TimeZoneRule;
import java.util.Date;

public class InitialTimeZoneRule
extends TimeZoneRule {
    private static final long serialVersionUID = 1876594993064051206L;

    public InitialTimeZoneRule(String name, int rawOffset, int dstSavings) {
        super(name, rawOffset, dstSavings);
    }

    @Override
    public boolean isEquivalentTo(TimeZoneRule other) {
        if (other instanceof InitialTimeZoneRule) {
            return super.isEquivalentTo(other);
        }
        return false;
    }

    @Override
    public Date getFinalStart(int prevRawOffset, int prevDSTSavings) {
        return null;
    }

    @Override
    public Date getFirstStart(int prevRawOffset, int prevDSTSavings) {
        return null;
    }

    @Override
    public Date getNextStart(long base, int prevRawOffset, int prevDSTSavings, boolean inclusive) {
        return null;
    }

    @Override
    public Date getPreviousStart(long base, int prevRawOffset, int prevDSTSavings, boolean inclusive) {
        return null;
    }

    @Override
    public boolean isTransitionRule() {
        return false;
    }
}

