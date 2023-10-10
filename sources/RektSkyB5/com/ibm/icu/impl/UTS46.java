/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.Punycode;
import com.ibm.icu.impl.UBiDiProps;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.IDNA;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.StringPrepParseException;
import com.ibm.icu.util.ICUException;
import java.util.EnumSet;

public final class UTS46
extends IDNA {
    private static final Normalizer2 uts46Norm2 = Normalizer2.getInstance(null, "uts46", Normalizer2.Mode.COMPOSE);
    final int options;
    private static final EnumSet<IDNA.Error> severeErrors = EnumSet.of(IDNA.Error.LEADING_COMBINING_MARK, IDNA.Error.DISALLOWED, IDNA.Error.PUNYCODE, IDNA.Error.LABEL_HAS_DOT, IDNA.Error.INVALID_ACE_LABEL);
    private static final byte[] asciiData = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1};
    private static final int L_MASK = UTS46.U_MASK(0);
    private static final int R_AL_MASK = UTS46.U_MASK(1) | UTS46.U_MASK(13);
    private static final int L_R_AL_MASK = L_MASK | R_AL_MASK;
    private static final int R_AL_AN_MASK = R_AL_MASK | UTS46.U_MASK(5);
    private static final int EN_AN_MASK = UTS46.U_MASK(2) | UTS46.U_MASK(5);
    private static final int R_AL_EN_AN_MASK = R_AL_MASK | EN_AN_MASK;
    private static final int L_EN_MASK = L_MASK | UTS46.U_MASK(2);
    private static final int ES_CS_ET_ON_BN_NSM_MASK = UTS46.U_MASK(3) | UTS46.U_MASK(6) | UTS46.U_MASK(4) | UTS46.U_MASK(10) | UTS46.U_MASK(18) | UTS46.U_MASK(17);
    private static final int L_EN_ES_CS_ET_ON_BN_NSM_MASK = L_EN_MASK | ES_CS_ET_ON_BN_NSM_MASK;
    private static final int R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK = R_AL_MASK | EN_AN_MASK | ES_CS_ET_ON_BN_NSM_MASK;
    private static int U_GC_M_MASK = UTS46.U_MASK(6) | UTS46.U_MASK(7) | UTS46.U_MASK(8);

    public UTS46(int options) {
        this.options = options;
    }

    @Override
    public StringBuilder labelToASCII(CharSequence label, StringBuilder dest, IDNA.Info info) {
        return this.process(label, true, true, dest, info);
    }

    @Override
    public StringBuilder labelToUnicode(CharSequence label, StringBuilder dest, IDNA.Info info) {
        return this.process(label, true, false, dest, info);
    }

    @Override
    public StringBuilder nameToASCII(CharSequence name, StringBuilder dest, IDNA.Info info) {
        this.process(name, false, true, dest, info);
        if (dest.length() >= 254 && !info.getErrors().contains((Object)IDNA.Error.DOMAIN_NAME_TOO_LONG) && UTS46.isASCIIString(dest) && (dest.length() > 254 || dest.charAt(253) != '.')) {
            UTS46.addError(info, IDNA.Error.DOMAIN_NAME_TOO_LONG);
        }
        return dest;
    }

    @Override
    public StringBuilder nameToUnicode(CharSequence name, StringBuilder dest, IDNA.Info info) {
        return this.process(name, false, false, dest, info);
    }

    private static boolean isASCIIString(CharSequence dest) {
        int length = dest.length();
        for (int i2 = 0; i2 < length; ++i2) {
            if (dest.charAt(i2) <= '\u007f') continue;
            return false;
        }
        return true;
    }

    private StringBuilder process(CharSequence src, boolean isLabel, boolean toASCII, StringBuilder dest, IDNA.Info info) {
        if (dest == src) {
            throw new IllegalArgumentException();
        }
        dest.delete(0, Integer.MAX_VALUE);
        UTS46.resetInfo(info);
        int srcLength = src.length();
        if (srcLength == 0) {
            UTS46.addError(info, IDNA.Error.EMPTY_LABEL);
            return dest;
        }
        boolean disallowNonLDHDot = (this.options & 2) != 0;
        int labelStart = 0;
        int i2 = 0;
        while (true) {
            if (i2 == srcLength) {
                if (toASCII) {
                    if (i2 - labelStart > 63) {
                        UTS46.addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
                    }
                    if (!(isLabel || i2 < 254 || i2 <= 254 && labelStart >= i2)) {
                        UTS46.addError(info, IDNA.Error.DOMAIN_NAME_TOO_LONG);
                    }
                }
                UTS46.promoteAndResetLabelErrors(info);
                return dest;
            }
            char c2 = src.charAt(i2);
            if (c2 > '\u007f') break;
            byte cData = asciiData[c2];
            if (cData > 0) {
                dest.append((char)(c2 + 32));
            } else {
                if (cData < 0 && disallowNonLDHDot) break;
                dest.append(c2);
                if (c2 == '-') {
                    if (i2 == labelStart + 3 && src.charAt(i2 - 1) == '-') {
                        ++i2;
                        break;
                    }
                    if (i2 == labelStart) {
                        UTS46.addLabelError(info, IDNA.Error.LEADING_HYPHEN);
                    }
                    if (i2 + 1 == srcLength || src.charAt(i2 + 1) == '.') {
                        UTS46.addLabelError(info, IDNA.Error.TRAILING_HYPHEN);
                    }
                } else if (c2 == '.') {
                    if (isLabel) {
                        ++i2;
                        break;
                    }
                    if (i2 == labelStart) {
                        UTS46.addLabelError(info, IDNA.Error.EMPTY_LABEL);
                    }
                    if (toASCII && i2 - labelStart > 63) {
                        UTS46.addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
                    }
                    UTS46.promoteAndResetLabelErrors(info);
                    labelStart = i2 + 1;
                }
            }
            ++i2;
        }
        UTS46.promoteAndResetLabelErrors(info);
        this.processUnicode(src, labelStart, i2, isLabel, toASCII, dest, info);
        if (UTS46.isBiDi(info) && !UTS46.hasCertainErrors(info, severeErrors) && (!UTS46.isOkBiDi(info) || labelStart > 0 && !UTS46.isASCIIOkBiDi(dest, labelStart))) {
            UTS46.addError(info, IDNA.Error.BIDI);
        }
        return dest;
    }

    private StringBuilder processUnicode(CharSequence src, int labelStart, int mappingStart, boolean isLabel, boolean toASCII, StringBuilder dest, IDNA.Info info) {
        if (mappingStart == 0) {
            uts46Norm2.normalize(src, dest);
        } else {
            uts46Norm2.normalizeSecondAndAppend(dest, src.subSequence(mappingStart, src.length()));
        }
        boolean doMapDevChars = toASCII ? (this.options & 0x10) == 0 : (this.options & 0x20) == 0;
        int destLength = dest.length();
        int labelLimit = labelStart;
        while (labelLimit < destLength) {
            char c2 = dest.charAt(labelLimit);
            if (c2 == '.' && !isLabel) {
                int labelLength = labelLimit - labelStart;
                int newLength = this.processLabel(dest, labelStart, labelLength, toASCII, info);
                UTS46.promoteAndResetLabelErrors(info);
                destLength += newLength - labelLength;
                labelLimit = labelStart += newLength + 1;
                continue;
            }
            if ('\u00df' <= c2 && c2 <= '\u200d' && (c2 == '\u00df' || c2 == '\u03c2' || c2 >= '\u200c')) {
                UTS46.setTransitionalDifferent(info);
                if (doMapDevChars) {
                    destLength = this.mapDevChars(dest, labelStart, labelLimit);
                    doMapDevChars = false;
                    continue;
                }
                ++labelLimit;
                continue;
            }
            ++labelLimit;
        }
        if (0 == labelStart || labelStart < labelLimit) {
            this.processLabel(dest, labelStart, labelLimit - labelStart, toASCII, info);
            UTS46.promoteAndResetLabelErrors(info);
        }
        return dest;
    }

    private int mapDevChars(StringBuilder dest, int labelStart, int mappingStart) {
        int length = dest.length();
        boolean didMapDevChars = false;
        int i2 = mappingStart;
        block5: while (i2 < length) {
            char c2 = dest.charAt(i2);
            switch (c2) {
                case '\u00df': {
                    didMapDevChars = true;
                    dest.setCharAt(i2++, 's');
                    dest.insert(i2++, 's');
                    ++length;
                    continue block5;
                }
                case '\u03c2': {
                    didMapDevChars = true;
                    dest.setCharAt(i2++, '\u03c3');
                    continue block5;
                }
                case '\u200c': 
                case '\u200d': {
                    didMapDevChars = true;
                    dest.delete(i2, i2 + 1);
                    --length;
                    continue block5;
                }
            }
            ++i2;
        }
        if (didMapDevChars) {
            String normalized = uts46Norm2.normalize(dest.subSequence(labelStart, dest.length()));
            dest.replace(labelStart, Integer.MAX_VALUE, normalized);
            return dest.length();
        }
        return length;
    }

    private static boolean isNonASCIIDisallowedSTD3Valid(int c2) {
        return c2 == 8800 || c2 == 8814 || c2 == 8815;
    }

    private static int replaceLabel(StringBuilder dest, int destLabelStart, int destLabelLength, CharSequence label, int labelLength) {
        if (label != dest) {
            dest.delete(destLabelStart, destLabelStart + destLabelLength).insert(destLabelStart, label);
        }
        return labelLength;
    }

    private int processLabel(StringBuilder dest, int labelStart, int labelLength, boolean toASCII, IDNA.Info info) {
        int c2;
        boolean disallowNonLDHDot;
        StringBuilder labelString;
        boolean wasPunycode;
        int destLabelStart = labelStart;
        int destLabelLength = labelLength;
        if (labelLength >= 4 && dest.charAt(labelStart) == 'x' && dest.charAt(labelStart + 1) == 'n' && dest.charAt(labelStart + 2) == '-' && dest.charAt(labelStart + 3) == '-') {
            StringBuilder fromPunycode;
            wasPunycode = true;
            try {
                fromPunycode = Punycode.decode(dest.subSequence(labelStart + 4, labelStart + labelLength), null);
            }
            catch (StringPrepParseException e2) {
                UTS46.addLabelError(info, IDNA.Error.PUNYCODE);
                return this.markBadACELabel(dest, labelStart, labelLength, toASCII, info);
            }
            boolean isValid = uts46Norm2.isNormalized(fromPunycode);
            if (!isValid) {
                UTS46.addLabelError(info, IDNA.Error.INVALID_ACE_LABEL);
                return this.markBadACELabel(dest, labelStart, labelLength, toASCII, info);
            }
            labelString = fromPunycode;
            labelStart = 0;
            labelLength = fromPunycode.length();
        } else {
            wasPunycode = false;
            labelString = dest;
        }
        if (labelLength == 0) {
            UTS46.addLabelError(info, IDNA.Error.EMPTY_LABEL);
            return UTS46.replaceLabel(dest, destLabelStart, destLabelLength, labelString, labelLength);
        }
        if (labelLength >= 4 && labelString.charAt(labelStart + 2) == '-' && labelString.charAt(labelStart + 3) == '-') {
            UTS46.addLabelError(info, IDNA.Error.HYPHEN_3_4);
        }
        if (labelString.charAt(labelStart) == '-') {
            UTS46.addLabelError(info, IDNA.Error.LEADING_HYPHEN);
        }
        if (labelString.charAt(labelStart + labelLength - 1) == '-') {
            UTS46.addLabelError(info, IDNA.Error.TRAILING_HYPHEN);
        }
        int i2 = labelStart;
        int limit = labelStart + labelLength;
        char oredChars = '\u0000';
        boolean bl = disallowNonLDHDot = (this.options & 2) != 0;
        do {
            if ((c2 = labelString.charAt(i2)) <= 127) {
                if (c2 == 46) {
                    UTS46.addLabelError(info, IDNA.Error.LABEL_HAS_DOT);
                    labelString.setCharAt(i2, '\ufffd');
                    continue;
                }
                if (!disallowNonLDHDot || asciiData[c2] >= 0) continue;
                UTS46.addLabelError(info, IDNA.Error.DISALLOWED);
                labelString.setCharAt(i2, '\ufffd');
                continue;
            }
            oredChars = (char)(oredChars | c2);
            if (disallowNonLDHDot && UTS46.isNonASCIIDisallowedSTD3Valid(c2)) {
                UTS46.addLabelError(info, IDNA.Error.DISALLOWED);
                labelString.setCharAt(i2, '\ufffd');
                continue;
            }
            if (c2 != 65533) continue;
            UTS46.addLabelError(info, IDNA.Error.DISALLOWED);
        } while (++i2 < limit);
        c2 = labelString.codePointAt(labelStart);
        if ((UTS46.U_GET_GC_MASK(c2) & U_GC_M_MASK) != 0) {
            UTS46.addLabelError(info, IDNA.Error.LEADING_COMBINING_MARK);
            labelString.setCharAt(labelStart, '\ufffd');
            if (c2 > 65535) {
                labelString.deleteCharAt(labelStart + 1);
                --labelLength;
                if (labelString == dest) {
                    --destLabelLength;
                }
            }
        }
        if (!UTS46.hasCertainLabelErrors(info, severeErrors)) {
            if ((this.options & 4) != 0 && (!UTS46.isBiDi(info) || UTS46.isOkBiDi(info))) {
                this.checkLabelBiDi(labelString, labelStart, labelLength, info);
            }
            if ((this.options & 8) != 0 && (oredChars & 0x200C) == 8204 && !this.isLabelOkContextJ(labelString, labelStart, labelLength)) {
                UTS46.addLabelError(info, IDNA.Error.CONTEXTJ);
            }
            if ((this.options & 0x40) != 0 && oredChars >= '\u00b7') {
                this.checkLabelContextO(labelString, labelStart, labelLength, info);
            }
            if (toASCII) {
                if (wasPunycode) {
                    if (destLabelLength > 63) {
                        UTS46.addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
                    }
                    return destLabelLength;
                }
                if (oredChars >= '\u0080') {
                    StringBuilder punycode;
                    try {
                        punycode = Punycode.encode(labelString.subSequence(labelStart, labelStart + labelLength), null);
                    }
                    catch (StringPrepParseException e3) {
                        throw new ICUException(e3);
                    }
                    punycode.insert(0, "xn--");
                    if (punycode.length() > 63) {
                        UTS46.addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
                    }
                    return UTS46.replaceLabel(dest, destLabelStart, destLabelLength, punycode, punycode.length());
                }
                if (labelLength > 63) {
                    UTS46.addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
                }
            }
        } else if (wasPunycode) {
            UTS46.addLabelError(info, IDNA.Error.INVALID_ACE_LABEL);
            return this.markBadACELabel(dest, destLabelStart, destLabelLength, toASCII, info);
        }
        return UTS46.replaceLabel(dest, destLabelStart, destLabelLength, labelString, labelLength);
    }

    private int markBadACELabel(StringBuilder dest, int labelStart, int labelLength, boolean toASCII, IDNA.Info info) {
        boolean disallowNonLDHDot = (this.options & 2) != 0;
        boolean isASCII = true;
        boolean onlyLDH = true;
        int i2 = labelStart + 4;
        int limit = labelStart + labelLength;
        do {
            char c2;
            if ((c2 = dest.charAt(i2)) <= '\u007f') {
                if (c2 == '.') {
                    UTS46.addLabelError(info, IDNA.Error.LABEL_HAS_DOT);
                    dest.setCharAt(i2, '\ufffd');
                    onlyLDH = false;
                    isASCII = false;
                    continue;
                }
                if (asciiData[c2] >= 0) continue;
                onlyLDH = false;
                if (!disallowNonLDHDot) continue;
                dest.setCharAt(i2, '\ufffd');
                isASCII = false;
                continue;
            }
            onlyLDH = false;
            isASCII = false;
        } while (++i2 < limit);
        if (onlyLDH) {
            dest.insert(labelStart + labelLength, '\ufffd');
            ++labelLength;
        } else if (toASCII && isASCII && labelLength > 63) {
            UTS46.addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
        }
        return labelLength;
    }

    private void checkLabelBiDi(CharSequence label, int labelStart, int labelLength, IDNA.Info info) {
        int lastMask;
        int labelLimit;
        int firstMask;
        int c2;
        int i2;
        block11: {
            int dir;
            i2 = labelStart;
            c2 = Character.codePointAt(label, i2);
            i2 += Character.charCount(c2);
            firstMask = UTS46.U_MASK(UBiDiProps.INSTANCE.getClass(c2));
            if ((firstMask & ~L_R_AL_MASK) != 0) {
                UTS46.setNotOkBiDi(info);
            }
            labelLimit = labelStart + labelLength;
            do {
                if (i2 >= labelLimit) {
                    lastMask = firstMask;
                    break block11;
                }
                c2 = Character.codePointBefore(label, labelLimit);
                labelLimit -= Character.charCount(c2);
            } while ((dir = UBiDiProps.INSTANCE.getClass(c2)) == 17);
            lastMask = UTS46.U_MASK(dir);
        }
        if ((firstMask & L_MASK) != 0 ? (lastMask & ~L_EN_MASK) != 0 : (lastMask & ~R_AL_EN_AN_MASK) != 0) {
            UTS46.setNotOkBiDi(info);
        }
        int mask = firstMask | lastMask;
        while (i2 < labelLimit) {
            c2 = Character.codePointAt(label, i2);
            i2 += Character.charCount(c2);
            mask |= UTS46.U_MASK(UBiDiProps.INSTANCE.getClass(c2));
        }
        if ((firstMask & L_MASK) != 0) {
            if ((mask & ~L_EN_ES_CS_ET_ON_BN_NSM_MASK) != 0) {
                UTS46.setNotOkBiDi(info);
            }
        } else {
            if ((mask & ~R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK) != 0) {
                UTS46.setNotOkBiDi(info);
            }
            if ((mask & EN_AN_MASK) == EN_AN_MASK) {
                UTS46.setNotOkBiDi(info);
            }
        }
        if ((mask & R_AL_AN_MASK) != 0) {
            UTS46.setBiDi(info);
        }
    }

    private static boolean isASCIIOkBiDi(CharSequence s2, int length) {
        int labelStart = 0;
        for (int i2 = 0; i2 < length; ++i2) {
            char c2 = s2.charAt(i2);
            if (c2 == '.') {
                if (!(i2 <= labelStart || 'a' <= (c2 = s2.charAt(i2 - 1)) && c2 <= 'z' || '0' <= c2 && c2 <= '9')) {
                    return false;
                }
                labelStart = i2 + 1;
                continue;
            }
            if (!(i2 == labelStart ? 'a' > c2 || c2 > 'z' : c2 <= ' ' && (c2 >= '\u001c' || '\t' <= c2 && c2 <= '\r'))) continue;
            return false;
        }
        return true;
    }

    private boolean isLabelOkContextJ(CharSequence label, int labelStart, int labelLength) {
        int labelLimit = labelStart + labelLength;
        for (int i2 = labelStart; i2 < labelLimit; ++i2) {
            int c2;
            if (label.charAt(i2) == '\u200c') {
                int type;
                if (i2 == labelStart) {
                    return false;
                }
                int j2 = i2;
                c2 = Character.codePointBefore(label, j2);
                j2 -= Character.charCount(c2);
                if (uts46Norm2.getCombiningClass(c2) == 9) continue;
                while ((type = UBiDiProps.INSTANCE.getJoiningType(c2)) == 5) {
                    if (j2 == 0) {
                        return false;
                    }
                    c2 = Character.codePointBefore(label, j2);
                    j2 -= Character.charCount(c2);
                }
                if (type != 3 && type != 2) {
                    return false;
                }
                j2 = i2 + 1;
                do {
                    if (j2 == labelLimit) {
                        return false;
                    }
                    c2 = Character.codePointAt(label, j2);
                    j2 += Character.charCount(c2);
                } while ((type = UBiDiProps.INSTANCE.getJoiningType(c2)) == 5);
                if (type == 4 || type == 2) continue;
                return false;
            }
            if (label.charAt(i2) != '\u200d') continue;
            if (i2 == labelStart) {
                return false;
            }
            c2 = Character.codePointBefore(label, i2);
            if (uts46Norm2.getCombiningClass(c2) == 9) continue;
            return false;
        }
        return true;
    }

    private void checkLabelContextO(CharSequence label, int labelStart, int labelLength, IDNA.Info info) {
        int labelEnd = labelStart + labelLength - 1;
        int arabicDigits = 0;
        block0: for (int i2 = labelStart; i2 <= labelEnd; ++i2) {
            int c2 = label.charAt(i2);
            if (c2 < 183) continue;
            if (c2 <= 1785) {
                if (c2 == 183) {
                    if (labelStart < i2 && label.charAt(i2 - 1) == 'l' && i2 < labelEnd && label.charAt(i2 + 1) == 'l') continue;
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_PUNCTUATION);
                    continue;
                }
                if (c2 == 885) {
                    if (i2 < labelEnd && 14 == UScript.getScript(Character.codePointAt(label, i2 + 1))) continue;
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_PUNCTUATION);
                    continue;
                }
                if (c2 == 1523 || c2 == 1524) {
                    if (labelStart < i2 && 19 == UScript.getScript(Character.codePointBefore(label, i2))) continue;
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_PUNCTUATION);
                    continue;
                }
                if (1632 > c2) continue;
                if (c2 <= 1641) {
                    if (arabicDigits > 0) {
                        UTS46.addLabelError(info, IDNA.Error.CONTEXTO_DIGITS);
                    }
                    arabicDigits = -1;
                    continue;
                }
                if (1776 > c2) continue;
                if (arabicDigits < 0) {
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_DIGITS);
                }
                arabicDigits = 1;
                continue;
            }
            if (c2 != 12539) continue;
            int j2 = labelStart;
            while (true) {
                if (j2 > labelEnd) {
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_PUNCTUATION);
                    continue block0;
                }
                c2 = Character.codePointAt(label, j2);
                int script = UScript.getScript(c2);
                if (script == 20 || script == 22 || script == 17) continue block0;
                j2 += Character.charCount(c2);
            }
        }
    }

    private static int U_MASK(int x2) {
        return 1 << x2;
    }

    private static int U_GET_GC_MASK(int c2) {
        return 1 << UCharacter.getType(c2);
    }
}

