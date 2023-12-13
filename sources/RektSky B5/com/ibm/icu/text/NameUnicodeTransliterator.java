/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.UCharacterName;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeFilter;
import com.ibm.icu.text.UnicodeSet;

class NameUnicodeTransliterator
extends Transliterator {
    static final String _ID = "Name-Any";
    static final String OPEN_PAT = "\\N~{~";
    static final char OPEN_DELIM = '\\';
    static final char CLOSE_DELIM = '}';
    static final char SPACE = ' ';

    static void register() {
        Transliterator.registerFactory(_ID, new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new NameUnicodeTransliterator(null);
            }
        });
    }

    public NameUnicodeTransliterator(UnicodeFilter filter) {
        super(_ID, filter);
    }

    @Override
    protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental) {
        int maxLen = UCharacterName.INSTANCE.getMaxCharNameLength() + 1;
        StringBuffer name = new StringBuffer(maxLen);
        UnicodeSet legal = new UnicodeSet();
        UCharacterName.INSTANCE.getCharNameCharacters(legal);
        int cursor = offsets.start;
        int limit = offsets.limit;
        int mode = 0;
        int openPos = -1;
        block4: while (cursor < limit) {
            int c2 = text.char32At(cursor);
            switch (mode) {
                case 0: {
                    if (c2 != 92) break;
                    openPos = cursor;
                    int i2 = Utility.parsePattern(OPEN_PAT, text, cursor, limit);
                    if (i2 < 0 || i2 >= limit) break;
                    mode = 1;
                    name.setLength(0);
                    cursor = i2;
                    continue block4;
                }
                case 1: {
                    if (PatternProps.isWhiteSpace(c2)) {
                        if (name.length() <= 0 || name.charAt(name.length() - 1) == ' ') break;
                        name.append(' ');
                        if (name.length() <= maxLen) break;
                        mode = 0;
                        break;
                    }
                    if (c2 == 125) {
                        int len = name.length();
                        if (len > 0 && name.charAt(len - 1) == ' ') {
                            name.setLength(--len);
                        }
                        if ((c2 = UCharacter.getCharFromExtendedName(name.toString())) != -1) {
                            String str = UTF16.valueOf(c2);
                            text.replace(openPos, ++cursor, str);
                            int delta = cursor - openPos - str.length();
                            cursor -= delta;
                            limit -= delta;
                        }
                        mode = 0;
                        openPos = -1;
                        continue block4;
                    }
                    if (legal.contains(c2)) {
                        UTF16.append(name, c2);
                        if (name.length() < maxLen) break;
                        mode = 0;
                        break;
                    }
                    --cursor;
                    mode = 0;
                }
            }
            cursor += UTF16.getCharCount(c2);
        }
        offsets.contextLimit += limit - offsets.limit;
        offsets.limit = limit;
        offsets.start = isIncremental && openPos >= 0 ? openPos : cursor;
    }

    @Override
    public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        UnicodeSet myFilter = this.getFilterAsUnicodeSet(inputFilter);
        if (!myFilter.containsAll("\\N{") || !myFilter.contains(125)) {
            return;
        }
        UnicodeSet items = new UnicodeSet().addAll(48, 57).addAll(65, 70).addAll(97, 122).add(60).add(62).add(40).add(41).add(45).add(32).addAll("\\N{").add(125);
        items.retainAll(myFilter);
        if (items.size() > 0) {
            sourceSet.addAll(items);
            targetSet.addAll(0, 0x10FFFF);
        }
    }
}

