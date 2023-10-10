/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.text.CharacterIterator;

public final class CharacterIteration {
    public static final int DONE32 = Integer.MAX_VALUE;

    private CharacterIteration() {
    }

    public static int next32(CharacterIterator ci) {
        int c2 = ci.current();
        if (c2 >= 55296 && c2 <= 56319 && ((c2 = ci.next()) < 56320 || c2 > 57343)) {
            ci.previous();
        }
        if ((c2 = ci.next()) >= 55296) {
            c2 = CharacterIteration.nextTrail32(ci, c2);
        }
        if (c2 >= 65536 && c2 != Integer.MAX_VALUE) {
            ci.previous();
        }
        return c2;
    }

    public static int nextTrail32(CharacterIterator ci, int lead) {
        if (lead == 65535 && ci.getIndex() >= ci.getEndIndex()) {
            return Integer.MAX_VALUE;
        }
        int retVal = lead;
        if (lead <= 56319) {
            char cTrail = ci.next();
            if (UTF16.isTrailSurrogate(cTrail)) {
                retVal = (lead - 55296 << 10) + (cTrail - 56320) + 65536;
            } else {
                ci.previous();
            }
        }
        return retVal;
    }

    public static int previous32(CharacterIterator ci) {
        int trail;
        if (ci.getIndex() <= ci.getBeginIndex()) {
            return Integer.MAX_VALUE;
        }
        int retVal = trail = ci.previous();
        if (UTF16.isTrailSurrogate((char)trail) && ci.getIndex() > ci.getBeginIndex()) {
            char lead = ci.previous();
            if (UTF16.isLeadSurrogate(lead)) {
                retVal = (lead - 55296 << 10) + (trail - 56320) + 65536;
            } else {
                ci.next();
            }
        }
        return retVal;
    }

    public static int current32(CharacterIterator ci) {
        int lead = ci.current();
        int retVal = lead;
        if (retVal < 55296) {
            return retVal;
        }
        if (UTF16.isLeadSurrogate((char)lead)) {
            char trail = ci.next();
            ci.previous();
            if (UTF16.isTrailSurrogate(trail)) {
                retVal = (lead - 55296 << 10) + (trail - 56320) + 65536;
            }
        } else if (lead == 65535 && ci.getIndex() >= ci.getEndIndex()) {
            retVal = Integer.MAX_VALUE;
        }
        return retVal;
    }
}

