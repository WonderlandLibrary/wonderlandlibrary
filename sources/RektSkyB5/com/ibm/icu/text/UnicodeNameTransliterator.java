/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeFilter;
import com.ibm.icu.text.UnicodeSet;

class UnicodeNameTransliterator
extends Transliterator {
    static final String _ID = "Any-Name";
    static final String OPEN_DELIM = "\\N{";
    static final char CLOSE_DELIM = '}';
    static final int OPEN_DELIM_LEN = 3;

    static void register() {
        Transliterator.registerFactory(_ID, new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new UnicodeNameTransliterator(null);
            }
        });
    }

    public UnicodeNameTransliterator(UnicodeFilter filter) {
        super(_ID, filter);
    }

    @Override
    protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental) {
        int cursor = offsets.start;
        int limit = offsets.limit;
        StringBuilder str = new StringBuilder();
        str.append(OPEN_DELIM);
        while (cursor < limit) {
            int c2 = text.char32At(cursor);
            String name = UCharacter.getExtendedName(c2);
            if (name != null) {
                str.setLength(3);
                str.append(name).append('}');
                int clen = UTF16.getCharCount(c2);
                text.replace(cursor, cursor + clen, str.toString());
                int len = str.length();
                cursor += len;
                limit += len - clen;
                continue;
            }
            ++cursor;
        }
        offsets.contextLimit += limit - offsets.limit;
        offsets.limit = limit;
        offsets.start = cursor;
    }

    @Override
    public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        UnicodeSet myFilter = this.getFilterAsUnicodeSet(inputFilter);
        if (myFilter.size() > 0) {
            sourceSet.addAll(myFilter);
            targetSet.addAll(48, 57).addAll(65, 90).add(45).add(32).addAll(OPEN_DELIM).add(125).addAll(97, 122).add(60).add(62).add(40).add(41);
        }
    }
}

