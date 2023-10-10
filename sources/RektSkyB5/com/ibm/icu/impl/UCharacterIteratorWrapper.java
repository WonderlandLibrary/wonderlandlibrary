/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;
import java.text.CharacterIterator;

public class UCharacterIteratorWrapper
implements CharacterIterator {
    private UCharacterIterator iterator;

    public UCharacterIteratorWrapper(UCharacterIterator iter) {
        this.iterator = iter;
    }

    @Override
    public char first() {
        this.iterator.setToStart();
        return (char)this.iterator.current();
    }

    @Override
    public char last() {
        this.iterator.setToLimit();
        return (char)this.iterator.previous();
    }

    @Override
    public char current() {
        return (char)this.iterator.current();
    }

    @Override
    public char next() {
        this.iterator.next();
        return (char)this.iterator.current();
    }

    @Override
    public char previous() {
        return (char)this.iterator.previous();
    }

    @Override
    public char setIndex(int position) {
        this.iterator.setIndex(position);
        return (char)this.iterator.current();
    }

    @Override
    public int getBeginIndex() {
        return 0;
    }

    @Override
    public int getEndIndex() {
        return this.iterator.getLength();
    }

    @Override
    public int getIndex() {
        return this.iterator.getIndex();
    }

    @Override
    public Object clone() {
        try {
            UCharacterIteratorWrapper result = (UCharacterIteratorWrapper)super.clone();
            result.iterator = (UCharacterIterator)this.iterator.clone();
            return result;
        }
        catch (CloneNotSupportedException e2) {
            return null;
        }
    }
}

