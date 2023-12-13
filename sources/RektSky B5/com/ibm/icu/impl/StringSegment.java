/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.UnicodeSet;

public class StringSegment
implements CharSequence {
    private final String str;
    private int start;
    private int end;
    private boolean foldCase;

    public StringSegment(String str, boolean foldCase) {
        this.str = str;
        this.start = 0;
        this.end = str.length();
        this.foldCase = foldCase;
    }

    public int getOffset() {
        return this.start;
    }

    public void setOffset(int start) {
        assert (start <= this.end);
        this.start = start;
    }

    public void adjustOffset(int delta) {
        assert (this.start + delta >= 0);
        assert (this.start + delta <= this.end);
        this.start += delta;
    }

    public void adjustOffsetByCodePoint() {
        this.start += Character.charCount(this.getCodePoint());
    }

    public void setLength(int length) {
        assert (length >= 0);
        assert (this.start + length <= this.str.length());
        this.end = this.start + length;
    }

    public void resetLength() {
        this.end = this.str.length();
    }

    @Override
    public int length() {
        return this.end - this.start;
    }

    @Override
    public char charAt(int index) {
        return this.str.charAt(index + this.start);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.str.subSequence(start + this.start, end + this.start);
    }

    public int getCodePoint() {
        char trail;
        assert (this.start < this.end);
        char lead = this.str.charAt(this.start);
        if (Character.isHighSurrogate(lead) && this.start + 1 < this.end && Character.isLowSurrogate(trail = this.str.charAt(this.start + 1))) {
            return Character.toCodePoint(lead, trail);
        }
        return lead;
    }

    public int codePointAt(int index) {
        return this.str.codePointAt(this.start + index);
    }

    public boolean startsWith(int otherCp) {
        return StringSegment.codePointsEqual(this.getCodePoint(), otherCp, this.foldCase);
    }

    public boolean startsWith(UnicodeSet uniset) {
        int cp = this.getCodePoint();
        if (cp == -1) {
            return false;
        }
        return uniset.contains(cp);
    }

    public boolean startsWith(CharSequence other) {
        if (other == null || other.length() == 0 || this.length() == 0) {
            return false;
        }
        int cp1 = Character.codePointAt(this, 0);
        int cp2 = Character.codePointAt(other, 0);
        return StringSegment.codePointsEqual(cp1, cp2, this.foldCase);
    }

    public int getCommonPrefixLength(CharSequence other) {
        return this.getPrefixLengthInternal(other, this.foldCase);
    }

    public int getCaseSensitivePrefixLength(CharSequence other) {
        return this.getPrefixLengthInternal(other, false);
    }

    private int getPrefixLengthInternal(CharSequence other, boolean foldCase) {
        int cp2;
        int offset;
        int cp1;
        assert (other.length() != 0);
        for (offset = 0; offset < Math.min(this.length(), other.length()) && StringSegment.codePointsEqual(cp1 = Character.codePointAt(this, offset), cp2 = Character.codePointAt(other, offset), foldCase); offset += Character.charCount(cp1)) {
        }
        return offset;
    }

    private static final boolean codePointsEqual(int cp1, int cp2, boolean foldCase) {
        if (cp1 == cp2) {
            return true;
        }
        if (!foldCase) {
            return false;
        }
        return (cp1 = UCharacter.foldCase(cp1, true)) == (cp2 = UCharacter.foldCase(cp2, true));
    }

    public boolean equals(Object other) {
        if (!(other instanceof CharSequence)) {
            return false;
        }
        return Utility.charSequenceEquals(this, (CharSequence)other);
    }

    public int hashCode() {
        return Utility.charSequenceHashCode(this);
    }

    @Override
    public String toString() {
        return this.str.substring(0, this.start) + "[" + this.str.substring(this.start, this.end) + "]" + this.str.substring(this.end);
    }
}

