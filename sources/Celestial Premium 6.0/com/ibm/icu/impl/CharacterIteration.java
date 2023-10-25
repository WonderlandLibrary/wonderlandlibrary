/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.text.CharacterIterator;

public final class CharacterIteration {
    public static final int DONE32 = Integer.MAX_VALUE;

    private CharacterIteration() {
    }

    public static int next32(CharacterIterator ci) {
        int c = ci.current();
        if (c >= 55296 && c <= 56319 && ((c = ci.next()) < 56320 || c > 57343)) {
            c = ci.previous();
        }
        if ((c = ci.next()) >= 55296) {
            c = CharacterIteration.nextTrail32(ci, c);
        }
        if (c >= 65536 && c != Integer.MAX_VALUE) {
            ci.previous();
        }
        return c;
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

