/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import java.text.CharacterIterator;

class CharArrayIterator
implements CharacterIterator {
    private char[] chars;
    private int pos;
    private int begin;

    public CharArrayIterator(char[] arrc) {
        this.reset(arrc, 0);
    }

    public CharArrayIterator(char[] arrc, int n) {
        this.reset(arrc, n);
    }

    @Override
    public char first() {
        this.pos = 0;
        return this.current();
    }

    @Override
    public char last() {
        this.pos = this.chars.length > 0 ? this.chars.length - 1 : 0;
        return this.current();
    }

    @Override
    public char current() {
        if (this.pos >= 0 && this.pos < this.chars.length) {
            return this.chars[this.pos];
        }
        return '\uffff';
    }

    @Override
    public char next() {
        if (this.pos < this.chars.length - 1) {
            ++this.pos;
            return this.chars[this.pos];
        }
        this.pos = this.chars.length;
        return '\uffff';
    }

    @Override
    public char previous() {
        if (this.pos > 0) {
            --this.pos;
            return this.chars[this.pos];
        }
        this.pos = 0;
        return '\uffff';
    }

    @Override
    public char setIndex(int n) {
        if ((n -= this.begin) < 0 || n > this.chars.length) {
            throw new IllegalArgumentException("Invalid index");
        }
        this.pos = n;
        return this.current();
    }

    @Override
    public int getBeginIndex() {
        return this.begin;
    }

    @Override
    public int getEndIndex() {
        return this.begin + this.chars.length;
    }

    @Override
    public int getIndex() {
        return this.begin + this.pos;
    }

    @Override
    public Object clone() {
        CharArrayIterator charArrayIterator = new CharArrayIterator(this.chars, this.begin);
        charArrayIterator.pos = this.pos;
        return charArrayIterator;
    }

    void reset(char[] arrc) {
        this.reset(arrc, 0);
    }

    void reset(char[] arrc, int n) {
        this.chars = arrc;
        this.begin = n;
        this.pos = 0;
    }
}

