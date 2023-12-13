/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;
import java.text.CharacterIterator;

public class CharacterIteratorWrapper
extends UCharacterIterator {
    private CharacterIterator iterator;

    public CharacterIteratorWrapper(CharacterIterator iter) {
        if (iter == null) {
            throw new IllegalArgumentException();
        }
        this.iterator = iter;
    }

    @Override
    public int current() {
        char c2 = this.iterator.current();
        if (c2 == '\uffff') {
            return -1;
        }
        return c2;
    }

    @Override
    public int getLength() {
        return this.iterator.getEndIndex() - this.iterator.getBeginIndex();
    }

    @Override
    public int getIndex() {
        return this.iterator.getIndex();
    }

    @Override
    public int next() {
        char i2 = this.iterator.current();
        this.iterator.next();
        if (i2 == '\uffff') {
            return -1;
        }
        return i2;
    }

    @Override
    public int previous() {
        char i2 = this.iterator.previous();
        if (i2 == '\uffff') {
            return -1;
        }
        return i2;
    }

    @Override
    public void setIndex(int index) {
        try {
            this.iterator.setIndex(index);
        }
        catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void setToLimit() {
        this.iterator.setIndex(this.iterator.getEndIndex());
    }

    @Override
    public int getText(char[] fillIn, int offset) {
        int length = this.iterator.getEndIndex() - this.iterator.getBeginIndex();
        int currentIndex = this.iterator.getIndex();
        if (offset < 0 || offset + length > fillIn.length) {
            throw new IndexOutOfBoundsException(Integer.toString(length));
        }
        char ch = this.iterator.first();
        while (ch != '\uffff') {
            fillIn[offset++] = ch;
            ch = this.iterator.next();
        }
        this.iterator.setIndex(currentIndex);
        return length;
    }

    @Override
    public Object clone() {
        try {
            CharacterIteratorWrapper result = (CharacterIteratorWrapper)super.clone();
            result.iterator = (CharacterIterator)this.iterator.clone();
            return result;
        }
        catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    @Override
    public int moveIndex(int delta) {
        int length = this.iterator.getEndIndex() - this.iterator.getBeginIndex();
        int idx = this.iterator.getIndex() + delta;
        if (idx < 0) {
            idx = 0;
        } else if (idx > length) {
            idx = length;
        }
        return this.iterator.setIndex(idx);
    }

    @Override
    public CharacterIterator getCharacterIterator() {
        return (CharacterIterator)this.iterator.clone();
    }
}

