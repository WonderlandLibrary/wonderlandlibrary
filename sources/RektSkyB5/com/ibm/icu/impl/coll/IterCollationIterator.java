/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationIterator;
import com.ibm.icu.text.UCharacterIterator;

public class IterCollationIterator
extends CollationIterator {
    protected UCharacterIterator iter;

    public IterCollationIterator(CollationData d2, boolean numeric, UCharacterIterator ui) {
        super(d2, numeric);
        this.iter = ui;
    }

    @Override
    public void resetToOffset(int newOffset) {
        this.reset();
        this.iter.setIndex(newOffset);
    }

    @Override
    public int getOffset() {
        return this.iter.getIndex();
    }

    @Override
    public int nextCodePoint() {
        return this.iter.nextCodePoint();
    }

    @Override
    public int previousCodePoint() {
        return this.iter.previousCodePoint();
    }

    @Override
    protected long handleNextCE32() {
        int c2 = this.iter.next();
        if (c2 < 0) {
            return -4294967104L;
        }
        return this.makeCodePointAndCE32Pair(c2, this.trie.getFromU16SingleLead((char)c2));
    }

    @Override
    protected char handleGetTrailSurrogate() {
        int trail = this.iter.next();
        if (!IterCollationIterator.isTrailSurrogate(trail) && trail >= 0) {
            this.iter.previous();
        }
        return (char)trail;
    }

    @Override
    protected void forwardNumCodePoints(int num) {
        this.iter.moveCodePointIndex(num);
    }

    @Override
    protected void backwardNumCodePoints(int num) {
        this.iter.moveCodePointIndex(-num);
    }
}

