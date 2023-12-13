/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;

class EscapeTransliterator
extends Transliterator {
    private String prefix;
    private String suffix;
    private int radix;
    private int minDigits;
    private boolean grokSupplementals;
    private EscapeTransliterator supplementalHandler;

    static void register() {
        Transliterator.registerFactory("Any-Hex/Unicode", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new EscapeTransliterator("Any-Hex/Unicode", "U+", "", 16, 4, true, null);
            }
        });
        Transliterator.registerFactory("Any-Hex/Java", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new EscapeTransliterator("Any-Hex/Java", "\\u", "", 16, 4, false, null);
            }
        });
        Transliterator.registerFactory("Any-Hex/C", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new EscapeTransliterator("Any-Hex/C", "\\u", "", 16, 4, true, new EscapeTransliterator("", "\\U", "", 16, 8, true, null));
            }
        });
        Transliterator.registerFactory("Any-Hex/XML", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new EscapeTransliterator("Any-Hex/XML", "&#x", ";", 16, 1, true, null);
            }
        });
        Transliterator.registerFactory("Any-Hex/XML10", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new EscapeTransliterator("Any-Hex/XML10", "&#", ";", 10, 1, true, null);
            }
        });
        Transliterator.registerFactory("Any-Hex/Perl", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new EscapeTransliterator("Any-Hex/Perl", "\\x{", "}", 16, 1, true, null);
            }
        });
        Transliterator.registerFactory("Any-Hex/Plain", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new EscapeTransliterator("Any-Hex/Plain", "", "", 16, 4, true, null);
            }
        });
        Transliterator.registerFactory("Any-Hex", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new EscapeTransliterator("Any-Hex", "\\u", "", 16, 4, false, null);
            }
        });
    }

    EscapeTransliterator(String ID2, String prefix, String suffix, int radix, int minDigits, boolean grokSupplementals, EscapeTransliterator supplementalHandler) {
        super(ID2, null);
        this.prefix = prefix;
        this.suffix = suffix;
        this.radix = radix;
        this.minDigits = minDigits;
        this.grokSupplementals = grokSupplementals;
        this.supplementalHandler = supplementalHandler;
    }

    @Override
    protected void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean incremental) {
        int limit;
        int charLen;
        int start = pos.start;
        StringBuilder buf = new StringBuilder(this.prefix);
        int prefixLen = this.prefix.length();
        boolean redoPrefix = false;
        for (limit = pos.limit; start < limit; start += buf.length(), limit += buf.length() - charLen) {
            int c2 = this.grokSupplementals ? text.char32At(start) : (int)text.charAt(start);
            int n2 = charLen = this.grokSupplementals ? UTF16.getCharCount(c2) : 1;
            if ((c2 & 0xFFFF0000) != 0 && this.supplementalHandler != null) {
                buf.setLength(0);
                buf.append(this.supplementalHandler.prefix);
                Utility.appendNumber(buf, c2, this.supplementalHandler.radix, this.supplementalHandler.minDigits);
                buf.append(this.supplementalHandler.suffix);
                redoPrefix = true;
            } else {
                if (redoPrefix) {
                    buf.setLength(0);
                    buf.append(this.prefix);
                    redoPrefix = false;
                } else {
                    buf.setLength(prefixLen);
                }
                Utility.appendNumber(buf, c2, this.radix, this.minDigits);
                buf.append(this.suffix);
            }
            text.replace(start, start + charLen, buf.toString());
        }
        pos.contextLimit += limit - pos.limit;
        pos.limit = limit;
        pos.start = start;
    }

    @Override
    public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        sourceSet.addAll(this.getFilterAsUnicodeSet(inputFilter));
        EscapeTransliterator it = this;
        while (it != null) {
            if (inputFilter.size() != 0) {
                targetSet.addAll(it.prefix);
                targetSet.addAll(it.suffix);
                StringBuilder buffer = new StringBuilder();
                for (int i2 = 0; i2 < it.radix; ++i2) {
                    Utility.appendNumber(buffer, i2, it.radix, it.minDigits);
                }
                targetSet.addAll(buffer.toString());
            }
            it = it.supplementalHandler;
        }
    }
}

