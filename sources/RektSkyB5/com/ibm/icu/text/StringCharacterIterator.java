/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.util.ICUCloneNotSupportedException;
import java.text.CharacterIterator;

@Deprecated
public final class StringCharacterIterator
implements CharacterIterator {
    private String text;
    private int begin;
    private int end;
    private int pos;

    @Deprecated
    public StringCharacterIterator(String text) {
        this(text, 0);
    }

    @Deprecated
    public StringCharacterIterator(String text, int pos) {
        this(text, 0, text.length(), pos);
    }

    @Deprecated
    public StringCharacterIterator(String text, int begin, int end, int pos) {
        if (text == null) {
            throw new NullPointerException();
        }
        this.text = text;
        if (begin < 0 || begin > end || end > text.length()) {
            throw new IllegalArgumentException("Invalid substring range");
        }
        if (pos < begin || pos > end) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.begin = begin;
        this.end = end;
        this.pos = pos;
    }

    @Deprecated
    public void setText(String text) {
        if (text == null) {
            throw new NullPointerException();
        }
        this.text = text;
        this.begin = 0;
        this.end = text.length();
        this.pos = 0;
    }

    @Override
    @Deprecated
    public char first() {
        this.pos = this.begin;
        return this.current();
    }

    @Override
    @Deprecated
    public char last() {
        this.pos = this.end != this.begin ? this.end - 1 : this.end;
        return this.current();
    }

    @Override
    @Deprecated
    public char setIndex(int p2) {
        if (p2 < this.begin || p2 > this.end) {
            throw new IllegalArgumentException("Invalid index");
        }
        this.pos = p2;
        return this.current();
    }

    @Override
    @Deprecated
    public char current() {
        if (this.pos >= this.begin && this.pos < this.end) {
            return this.text.charAt(this.pos);
        }
        return '\uffff';
    }

    @Override
    @Deprecated
    public char next() {
        if (this.pos < this.end - 1) {
            ++this.pos;
            return this.text.charAt(this.pos);
        }
        this.pos = this.end;
        return '\uffff';
    }

    @Override
    @Deprecated
    public char previous() {
        if (this.pos > this.begin) {
            --this.pos;
            return this.text.charAt(this.pos);
        }
        return '\uffff';
    }

    @Override
    @Deprecated
    public int getBeginIndex() {
        return this.begin;
    }

    @Override
    @Deprecated
    public int getEndIndex() {
        return this.end;
    }

    @Override
    @Deprecated
    public int getIndex() {
        return this.pos;
    }

    @Deprecated
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StringCharacterIterator)) {
            return false;
        }
        StringCharacterIterator that = (StringCharacterIterator)obj;
        if (this.hashCode() != that.hashCode()) {
            return false;
        }
        if (!this.text.equals(that.text)) {
            return false;
        }
        return this.pos == that.pos && this.begin == that.begin && this.end == that.end;
    }

    @Deprecated
    public int hashCode() {
        return this.text.hashCode() ^ this.pos ^ this.begin ^ this.end;
    }

    @Override
    @Deprecated
    public Object clone() {
        try {
            StringCharacterIterator other = (StringCharacterIterator)super.clone();
            return other;
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
    }
}

