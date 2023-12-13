/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.BidiRun;
import com.ibm.icu.text.UTF16;

final class BidiWriter {
    static final char LRM_CHAR = '\u200e';
    static final char RLM_CHAR = '\u200f';
    static final int MASK_R_AL = 8194;

    BidiWriter() {
    }

    private static boolean IsCombining(int type) {
        return (1 << type & 0x1C0) != 0;
    }

    private static String doWriteForward(String src, int options) {
        switch (options & 0xA) {
            case 0: {
                return src;
            }
            case 2: {
                int c2;
                StringBuffer dest = new StringBuffer(src.length());
                int i2 = 0;
                do {
                    c2 = UTF16.charAt(src, i2);
                    UTF16.append(dest, UCharacter.getMirror(c2));
                } while ((i2 += UTF16.getCharCount(c2)) < src.length());
                return dest.toString();
            }
            case 8: {
                StringBuilder dest = new StringBuilder(src.length());
                int i3 = 0;
                do {
                    char c3;
                    if (Bidi.IsBidiControlChar(c3 = src.charAt(i3++))) continue;
                    dest.append(c3);
                } while (i3 < src.length());
                return dest.toString();
            }
        }
        StringBuffer dest = new StringBuffer(src.length());
        int i4 = 0;
        do {
            int c4 = UTF16.charAt(src, i4);
            i4 += UTF16.getCharCount(c4);
            if (Bidi.IsBidiControlChar(c4)) continue;
            UTF16.append(dest, UCharacter.getMirror(c4));
        } while (i4 < src.length());
        return dest.toString();
    }

    private static String doWriteForward(char[] text, int start, int limit, int options) {
        return BidiWriter.doWriteForward(new String(text, start, limit - start), options);
    }

    static String writeReverse(String src, int options) {
        StringBuffer dest = new StringBuffer(src.length());
        switch (options & 0xB) {
            case 0: {
                int srcLength = src.length();
                do {
                    int i2 = srcLength;
                    srcLength -= UTF16.getCharCount(UTF16.charAt(src, srcLength - 1));
                    dest.append(src.substring(srcLength, i2));
                } while (srcLength > 0);
                break;
            }
            case 1: {
                int srcLength = src.length();
                do {
                    int c2;
                    int i3 = srcLength;
                    while ((srcLength -= UTF16.getCharCount(c2 = UTF16.charAt(src, srcLength - 1))) > 0 && BidiWriter.IsCombining(UCharacter.getType(c2))) {
                    }
                    dest.append(src.substring(srcLength, i3));
                } while (srcLength > 0);
                break;
            }
            default: {
                int srcLength = src.length();
                do {
                    int i4 = srcLength;
                    int c3 = UTF16.charAt(src, srcLength - 1);
                    srcLength -= UTF16.getCharCount(c3);
                    if ((options & 1) != 0) {
                        while (srcLength > 0 && BidiWriter.IsCombining(UCharacter.getType(c3))) {
                            c3 = UTF16.charAt(src, srcLength - 1);
                            srcLength -= UTF16.getCharCount(c3);
                        }
                    }
                    if ((options & 8) != 0 && Bidi.IsBidiControlChar(c3)) continue;
                    int j2 = srcLength;
                    if ((options & 2) != 0) {
                        c3 = UCharacter.getMirror(c3);
                        UTF16.append(dest, c3);
                        j2 += UTF16.getCharCount(c3);
                    }
                    dest.append(src.substring(j2, i4));
                } while (srcLength > 0);
            }
        }
        return dest.toString();
    }

    static String doWriteReverse(char[] text, int start, int limit, int options) {
        return BidiWriter.writeReverse(new String(text, start, limit - start), options);
    }

    static String writeReordered(Bidi bidi, int options) {
        char[] text = bidi.text;
        int runCount = bidi.countRuns();
        if ((bidi.reorderingOptions & 1) != 0) {
            options |= 4;
            options &= 0xFFFFFFF7;
        }
        if ((bidi.reorderingOptions & 2) != 0) {
            options |= 8;
            options &= 0xFFFFFFFB;
        }
        if (bidi.reorderingMode != 4 && bidi.reorderingMode != 5 && bidi.reorderingMode != 6 && bidi.reorderingMode != 3) {
            options &= 0xFFFFFFFB;
        }
        StringBuilder dest = new StringBuilder((options & 4) != 0 ? bidi.length * 2 : bidi.length);
        if ((options & 0x10) == 0) {
            if ((options & 4) == 0) {
                for (int run = 0; run < runCount; ++run) {
                    BidiRun bidiRun = bidi.getVisualRun(run);
                    if (bidiRun.isEvenRun()) {
                        dest.append(BidiWriter.doWriteForward(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
                        continue;
                    }
                    dest.append(BidiWriter.doWriteReverse(text, bidiRun.start, bidiRun.limit, options));
                }
            } else {
                byte[] dirProps = bidi.dirProps;
                for (int run = 0; run < runCount; ++run) {
                    char uc;
                    BidiRun bidiRun = bidi.getVisualRun(run);
                    int markFlag = 0;
                    markFlag = bidi.runs[run].insertRemove;
                    if (markFlag < 0) {
                        markFlag = 0;
                    }
                    if (bidiRun.isEvenRun()) {
                        if (bidi.isInverse() && dirProps[bidiRun.start] != 0) {
                            markFlag |= 1;
                        }
                        if ((uc = (markFlag & 1) != 0 ? (char)'\u200e' : ((markFlag & 4) != 0 ? (char)'\u200f' : '\u0000')) != '\u0000') {
                            dest.append(uc);
                        }
                        dest.append(BidiWriter.doWriteForward(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
                        if (bidi.isInverse() && dirProps[bidiRun.limit - 1] != 0) {
                            markFlag |= 2;
                        }
                        if ((uc = (markFlag & 2) != 0 ? (char)'\u200e' : ((markFlag & 8) != 0 ? (char)'\u200f' : '\u0000')) == '\u0000') continue;
                        dest.append(uc);
                        continue;
                    }
                    if (bidi.isInverse() && !bidi.testDirPropFlagAt(8194, bidiRun.limit - 1)) {
                        markFlag |= 4;
                    }
                    if ((uc = (markFlag & 1) != 0 ? (char)'\u200e' : ((markFlag & 4) != 0 ? (char)'\u200f' : '\u0000')) != '\u0000') {
                        dest.append(uc);
                    }
                    dest.append(BidiWriter.doWriteReverse(text, bidiRun.start, bidiRun.limit, options));
                    if (bidi.isInverse() && (0x2002 & Bidi.DirPropFlag(dirProps[bidiRun.start])) == 0) {
                        markFlag |= 8;
                    }
                    if ((uc = (markFlag & 2) != 0 ? (char)'\u200e' : ((markFlag & 8) != 0 ? (char)'\u200f' : '\u0000')) == '\u0000') continue;
                    dest.append(uc);
                }
            }
        } else if ((options & 4) == 0) {
            int run = runCount;
            while (--run >= 0) {
                BidiRun bidiRun = bidi.getVisualRun(run);
                if (bidiRun.isEvenRun()) {
                    dest.append(BidiWriter.doWriteReverse(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
                    continue;
                }
                dest.append(BidiWriter.doWriteForward(text, bidiRun.start, bidiRun.limit, options));
            }
        } else {
            byte[] dirProps = bidi.dirProps;
            int run = runCount;
            while (--run >= 0) {
                BidiRun bidiRun = bidi.getVisualRun(run);
                if (bidiRun.isEvenRun()) {
                    if (dirProps[bidiRun.limit - 1] != 0) {
                        dest.append('\u200e');
                    }
                    dest.append(BidiWriter.doWriteReverse(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
                    if (dirProps[bidiRun.start] == 0) continue;
                    dest.append('\u200e');
                    continue;
                }
                if ((0x2002 & Bidi.DirPropFlag(dirProps[bidiRun.start])) == 0) {
                    dest.append('\u200f');
                }
                dest.append(BidiWriter.doWriteForward(text, bidiRun.start, bidiRun.limit, options));
                if ((0x2002 & Bidi.DirPropFlag(dirProps[bidiRun.limit - 1])) != 0) continue;
                dest.append('\u200f');
            }
        }
        return dest.toString();
    }
}

