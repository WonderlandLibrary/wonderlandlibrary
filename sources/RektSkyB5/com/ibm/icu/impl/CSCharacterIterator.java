/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import java.text.CharacterIterator;

public class CSCharacterIterator
implements CharacterIterator {
    private int index;
    private CharSequence seq;

    public CSCharacterIterator(CharSequence text) {
        if (text == null) {
            throw new NullPointerException();
        }
        this.seq = text;
        this.index = 0;
    }

    @Override
    public char first() {
        this.index = 0;
        return this.current();
    }

    @Override
    public char last() {
        this.index = this.seq.length();
        return this.previous();
    }

    @Override
    public char current() {
        if (this.index == this.seq.length()) {
            return '\uffff';
        }
        return this.seq.charAt(this.index);
    }

    @Override
    public char next() {
        if (this.index < this.seq.length()) {
            ++this.index;
        }
        return this.current();
    }

    @Override
    public char previous() {
        if (this.index == 0) {
            return '\uffff';
        }
        --this.index;
        return this.current();
    }

    @Override
    public char setIndex(int position) {
        if (position < 0 || position > this.seq.length()) {
            throw new IllegalArgumentException();
        }
        this.index = position;
        return this.current();
    }

    @Override
    public int getBeginIndex() {
        return 0;
    }

    @Override
    public int getEndIndex() {
        return this.seq.length();
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public Object clone() {
        CSCharacterIterator copy = new CSCharacterIterator(this.seq);
        copy.setIndex(this.index);
        return copy;
    }
}

