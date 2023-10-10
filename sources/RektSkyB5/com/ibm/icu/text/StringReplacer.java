/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.RuleBasedTransliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeReplacer;
import com.ibm.icu.text.UnicodeSet;

class StringReplacer
implements UnicodeReplacer {
    private String output;
    private int cursorPos;
    private boolean hasCursor;
    private boolean isComplex;
    private final RuleBasedTransliterator.Data data;

    public StringReplacer(String theOutput, int theCursorPos, RuleBasedTransliterator.Data theData) {
        this.output = theOutput;
        this.cursorPos = theCursorPos;
        this.hasCursor = true;
        this.data = theData;
        this.isComplex = true;
    }

    public StringReplacer(String theOutput, RuleBasedTransliterator.Data theData) {
        this.output = theOutput;
        this.cursorPos = 0;
        this.hasCursor = false;
        this.data = theData;
        this.isComplex = true;
    }

    @Override
    public int replace(Replaceable text, int start, int limit, int[] cursor) {
        int outLen;
        int newStart = 0;
        if (!this.isComplex) {
            text.replace(start, limit, this.output);
            outLen = this.output.length();
            newStart = this.cursorPos;
        } else {
            int tempStart;
            StringBuffer buf = new StringBuffer();
            this.isComplex = false;
            int destStart = tempStart = text.length();
            if (start > 0) {
                int len = UTF16.getCharCount(text.char32At(start - 1));
                text.copy(start - len, start, tempStart);
                destStart += len;
            } else {
                text.replace(tempStart, tempStart, "\uffff");
                ++destStart;
            }
            int destLimit = destStart;
            int tempExtra = 0;
            int oOutput = 0;
            while (oOutput < this.output.length()) {
                UnicodeReplacer r2;
                int c2;
                int nextIndex;
                if (oOutput == this.cursorPos) {
                    newStart = buf.length() + destLimit - destStart;
                }
                if ((nextIndex = oOutput + UTF16.getCharCount(c2 = UTF16.charAt(this.output, oOutput))) == this.output.length()) {
                    tempExtra = UTF16.getCharCount(text.char32At(limit));
                    text.copy(limit, limit + tempExtra, destLimit);
                }
                if ((r2 = this.data.lookupReplacer(c2)) == null) {
                    UTF16.append(buf, c2);
                } else {
                    this.isComplex = true;
                    if (buf.length() > 0) {
                        text.replace(destLimit, destLimit, buf.toString());
                        destLimit += buf.length();
                        buf.setLength(0);
                    }
                    int len = r2.replace(text, destLimit, destLimit, cursor);
                    destLimit += len;
                }
                oOutput = nextIndex;
            }
            if (buf.length() > 0) {
                text.replace(destLimit, destLimit, buf.toString());
                destLimit += buf.length();
            }
            if (oOutput == this.cursorPos) {
                newStart = destLimit - destStart;
            }
            outLen = destLimit - destStart;
            text.copy(destStart, destLimit, start);
            text.replace(tempStart + outLen, destLimit + tempExtra + outLen, "");
            text.replace(start + outLen, limit + outLen, "");
        }
        if (this.hasCursor) {
            if (this.cursorPos < 0) {
                int n2;
                newStart = start;
                for (n2 = this.cursorPos; n2 < 0 && newStart > 0; newStart -= UTF16.getCharCount(text.char32At(newStart - 1)), ++n2) {
                }
                newStart += n2;
            } else if (this.cursorPos > this.output.length()) {
                int n3;
                newStart = start + outLen;
                for (n3 = this.cursorPos - this.output.length(); n3 > 0 && newStart < text.length(); newStart += UTF16.getCharCount(text.char32At(newStart)), --n3) {
                }
                newStart += n3;
            } else {
                newStart += start;
            }
            cursor[0] = newStart;
        }
        return outLen;
    }

    @Override
    public String toReplacerPattern(boolean escapeUnprintable) {
        StringBuffer rule = new StringBuffer();
        StringBuffer quoteBuf = new StringBuffer();
        int cursor = this.cursorPos;
        if (this.hasCursor && cursor < 0) {
            while (cursor++ < 0) {
                Utility.appendToRule(rule, 64, true, escapeUnprintable, quoteBuf);
            }
        }
        for (int i2 = 0; i2 < this.output.length(); ++i2) {
            char c2;
            UnicodeReplacer r2;
            if (this.hasCursor && i2 == cursor) {
                Utility.appendToRule(rule, 124, true, escapeUnprintable, quoteBuf);
            }
            if ((r2 = this.data.lookupReplacer(c2 = this.output.charAt(i2))) == null) {
                Utility.appendToRule(rule, c2, false, escapeUnprintable, quoteBuf);
                continue;
            }
            StringBuffer buf = new StringBuffer(" ");
            buf.append(r2.toReplacerPattern(escapeUnprintable));
            buf.append(' ');
            Utility.appendToRule(rule, buf.toString(), true, escapeUnprintable, quoteBuf);
        }
        if (this.hasCursor && cursor > this.output.length()) {
            cursor -= this.output.length();
            while (cursor-- > 0) {
                Utility.appendToRule(rule, 64, true, escapeUnprintable, quoteBuf);
            }
            Utility.appendToRule(rule, 124, true, escapeUnprintable, quoteBuf);
        }
        Utility.appendToRule(rule, -1, true, escapeUnprintable, quoteBuf);
        return rule.toString();
    }

    @Override
    public void addReplacementSetTo(UnicodeSet toUnionTo) {
        int ch;
        for (int i2 = 0; i2 < this.output.length(); i2 += UTF16.getCharCount(ch)) {
            ch = UTF16.charAt(this.output, i2);
            UnicodeReplacer r2 = this.data.lookupReplacer(ch);
            if (r2 == null) {
                toUnionTo.add(ch);
                continue;
            }
            r2.addReplacementSetTo(toUnionTo);
        }
    }
}

