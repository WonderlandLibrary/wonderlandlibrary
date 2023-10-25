/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.text.DictionaryMatcher;
import com.ibm.icu.text.UCharacterIterator;
import com.ibm.icu.util.BytesTrie;
import java.text.CharacterIterator;

class BytesDictionaryMatcher
extends DictionaryMatcher {
    private final byte[] characters;
    private final int transform;

    public BytesDictionaryMatcher(byte[] chars, int transform) {
        this.characters = chars;
        Assert.assrt((transform & 0x7F000000) == 0x1000000);
        this.transform = transform;
    }

    private int transform(int c) {
        if (c == 8205) {
            return 255;
        }
        if (c == 8204) {
            return 254;
        }
        int delta = c - (this.transform & 0x1FFFFF);
        if (delta < 0 || 253 < delta) {
            return -1;
        }
        return delta;
    }

    public int matches(CharacterIterator text_, int maxLength, int[] lengths, int[] count_, int limit, int[] values) {
        UCharacterIterator text = UCharacterIterator.getInstance(text_);
        BytesTrie bt = new BytesTrie(this.characters, 0);
        int c = text.nextCodePoint();
        BytesTrie.Result result = bt.first(this.transform(c));
        int numChars = 1;
        int count = 0;
        while (true) {
            if (result.hasValue()) {
                if (count < limit) {
                    if (values != null) {
                        values[count] = bt.getValue();
                    }
                    lengths[count] = numChars;
                    ++count;
                }
                if (result == BytesTrie.Result.FINAL_VALUE) {
                    break;
                }
            } else if (result == BytesTrie.Result.NO_MATCH) break;
            if (numChars >= maxLength) break;
            c = text.nextCodePoint();
            ++numChars;
            result = bt.next(this.transform(c));
        }
        count_[0] = count;
        return numChars;
    }

    public int getType() {
        return 0;
    }
}

