/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.lang;

import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.UTF16;

@Deprecated
public final class UScriptRun {
    private char[] emptyCharArray = new char[0];
    private char[] text;
    private int textIndex;
    private int textStart;
    private int textLimit;
    private int scriptStart;
    private int scriptLimit;
    private int scriptCode;
    private static int PAREN_STACK_DEPTH = 32;
    private static ParenStackEntry[] parenStack = new ParenStackEntry[PAREN_STACK_DEPTH];
    private int parenSP = -1;
    private int pushCount = 0;
    private int fixupCount = 0;
    private static int[] pairedChars = new int[]{40, 41, 60, 62, 91, 93, 123, 125, 171, 187, 8216, 8217, 8220, 8221, 8249, 8250, 12296, 12297, 12298, 12299, 12300, 12301, 12302, 12303, 12304, 12305, 12308, 12309, 12310, 12311, 12312, 12313, 12314, 12315};
    private static int pairedCharPower = 1 << UScriptRun.highBit(pairedChars.length);
    private static int pairedCharExtra = pairedChars.length - pairedCharPower;

    @Deprecated
    public UScriptRun() {
        char[] nullChars = null;
        this.reset(nullChars, 0, 0);
    }

    @Deprecated
    public UScriptRun(String text) {
        this.reset(text);
    }

    @Deprecated
    public UScriptRun(String text, int start, int count) {
        this.reset(text, start, count);
    }

    @Deprecated
    public UScriptRun(char[] chars) {
        this.reset(chars);
    }

    @Deprecated
    public UScriptRun(char[] chars, int start, int count) {
        this.reset(chars, start, count);
    }

    @Deprecated
    public final void reset() {
        while (this.stackIsNotEmpty()) {
            this.pop();
        }
        this.scriptStart = this.textStart;
        this.scriptLimit = this.textStart;
        this.scriptCode = -1;
        this.parenSP = -1;
        this.pushCount = 0;
        this.fixupCount = 0;
        this.textIndex = this.textStart;
    }

    @Deprecated
    public final void reset(int start, int count) throws IllegalArgumentException {
        int len = 0;
        if (this.text != null) {
            len = this.text.length;
        }
        if (start < 0 || count < 0 || start > len - count) {
            throw new IllegalArgumentException();
        }
        this.textStart = start;
        this.textLimit = start + count;
        this.reset();
    }

    @Deprecated
    public final void reset(char[] chars, int start, int count) {
        if (chars == null) {
            chars = this.emptyCharArray;
        }
        this.text = chars;
        this.reset(start, count);
    }

    @Deprecated
    public final void reset(char[] chars) {
        int length = 0;
        if (chars != null) {
            length = chars.length;
        }
        this.reset(chars, 0, length);
    }

    @Deprecated
    public final void reset(String str, int start, int count) {
        char[] chars = null;
        if (str != null) {
            chars = str.toCharArray();
        }
        this.reset(chars, start, count);
    }

    @Deprecated
    public final void reset(String str) {
        int length = 0;
        if (str != null) {
            length = str.length();
        }
        this.reset(str, 0, length);
    }

    @Deprecated
    public final int getScriptStart() {
        return this.scriptStart;
    }

    @Deprecated
    public final int getScriptLimit() {
        return this.scriptLimit;
    }

    @Deprecated
    public final int getScriptCode() {
        return this.scriptCode;
    }

    @Deprecated
    public final boolean next() {
        if (this.scriptLimit >= this.textLimit) {
            return false;
        }
        this.scriptCode = 0;
        this.scriptStart = this.scriptLimit;
        this.syncFixup();
        while (this.textIndex < this.textLimit) {
            int ch = UTF16.charAt(this.text, this.textStart, this.textLimit, this.textIndex - this.textStart);
            int codePointCount = UTF16.getCharCount(ch);
            int sc = UScript.getScript(ch);
            int pairIndex = UScriptRun.getPairIndex(ch);
            this.textIndex += codePointCount;
            if (pairIndex >= 0) {
                if ((pairIndex & 1) == 0) {
                    this.push(pairIndex, this.scriptCode);
                } else {
                    int pi = pairIndex & 0xFFFFFFFE;
                    while (this.stackIsNotEmpty() && this.top().pairIndex != pi) {
                        this.pop();
                    }
                    if (this.stackIsNotEmpty()) {
                        sc = this.top().scriptCode;
                    }
                }
            }
            if (UScriptRun.sameScript(this.scriptCode, sc)) {
                if (this.scriptCode <= 1 && sc > 1) {
                    this.scriptCode = sc;
                    this.fixup(this.scriptCode);
                }
                if (pairIndex < 0 || (pairIndex & 1) == 0) continue;
                this.pop();
                continue;
            }
            this.textIndex -= codePointCount;
            break;
        }
        this.scriptLimit = this.textIndex;
        return true;
    }

    private static boolean sameScript(int scriptOne, int scriptTwo) {
        return scriptOne <= 1 || scriptTwo <= 1 || scriptOne == scriptTwo;
    }

    private static final int mod(int sp) {
        return sp % PAREN_STACK_DEPTH;
    }

    private static final int inc(int sp, int count) {
        return UScriptRun.mod(sp + count);
    }

    private static final int inc(int sp) {
        return UScriptRun.inc(sp, 1);
    }

    private static final int dec(int sp, int count) {
        return UScriptRun.mod(sp + PAREN_STACK_DEPTH - count);
    }

    private static final int dec(int sp) {
        return UScriptRun.dec(sp, 1);
    }

    private static final int limitInc(int count) {
        if (count < PAREN_STACK_DEPTH) {
            ++count;
        }
        return count;
    }

    private final boolean stackIsEmpty() {
        return this.pushCount <= 0;
    }

    private final boolean stackIsNotEmpty() {
        return !this.stackIsEmpty();
    }

    private final void push(int pairIndex, int scrptCode) {
        this.pushCount = UScriptRun.limitInc(this.pushCount);
        this.fixupCount = UScriptRun.limitInc(this.fixupCount);
        this.parenSP = UScriptRun.inc(this.parenSP);
        UScriptRun.parenStack[this.parenSP] = new ParenStackEntry(pairIndex, scrptCode);
    }

    private final void pop() {
        if (this.stackIsEmpty()) {
            return;
        }
        UScriptRun.parenStack[this.parenSP] = null;
        if (this.fixupCount > 0) {
            --this.fixupCount;
        }
        --this.pushCount;
        this.parenSP = UScriptRun.dec(this.parenSP);
        if (this.stackIsEmpty()) {
            this.parenSP = -1;
        }
    }

    private final ParenStackEntry top() {
        return parenStack[this.parenSP];
    }

    private final void syncFixup() {
        this.fixupCount = 0;
    }

    private final void fixup(int scrptCode) {
        int fixupSP = UScriptRun.dec(this.parenSP, this.fixupCount);
        while (this.fixupCount-- > 0) {
            fixupSP = UScriptRun.inc(fixupSP);
            UScriptRun.parenStack[fixupSP].scriptCode = scrptCode;
        }
    }

    private static final byte highBit(int n2) {
        if (n2 <= 0) {
            return -32;
        }
        byte bit = 0;
        if (n2 >= 65536) {
            n2 >>= 16;
            bit = (byte)(bit + 16);
        }
        if (n2 >= 256) {
            n2 >>= 8;
            bit = (byte)(bit + 8);
        }
        if (n2 >= 16) {
            n2 >>= 4;
            bit = (byte)(bit + 4);
        }
        if (n2 >= 4) {
            n2 >>= 2;
            bit = (byte)(bit + 2);
        }
        if (n2 >= 2) {
            n2 >>= 1;
            bit = (byte)(bit + 1);
        }
        return bit;
    }

    private static int getPairIndex(int ch) {
        int probe = pairedCharPower;
        int index = 0;
        if (ch >= pairedChars[pairedCharExtra]) {
            index = pairedCharExtra;
        }
        while (probe > 1) {
            if (ch < pairedChars[index + (probe >>= 1)]) continue;
            index += probe;
        }
        if (pairedChars[index] != ch) {
            index = -1;
        }
        return index;
    }

    private static final class ParenStackEntry {
        int pairIndex;
        int scriptCode;

        public ParenStackEntry(int thePairIndex, int theScriptCode) {
            this.pairIndex = thePairIndex;
            this.scriptCode = theScriptCode;
        }
    }
}

