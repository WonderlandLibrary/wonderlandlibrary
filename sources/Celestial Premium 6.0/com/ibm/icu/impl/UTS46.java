/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.Punycode;
import com.ibm.icu.impl.UBiDiProps;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.IDNA;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.StringPrepParseException;
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

    public StringBuilder labelToASCII(CharSequence label, StringBuilder dest, IDNA.Info info) {
        return this.process(label, true, true, dest, info);
    }

    public StringBuilder labelToUnicode(CharSequence label, StringBuilder dest, IDNA.Info info) {
        return this.process(label, true, false, dest, info);
    }

    public StringBuilder nameToASCII(CharSequence name, StringBuilder dest, IDNA.Info info) {
        this.process(name, false, true, dest, info);
        if (dest.length() >= 254 && !info.getErrors().contains((Object)IDNA.Error.DOMAIN_NAME_TOO_LONG) && UTS46.isASCIIString(dest) && (dest.length() > 254 || dest.charAt(253) != '.')) {
            UTS46.addError(info, IDNA.Error.DOMAIN_NAME_TOO_LONG);
        }
        return dest;
    }

    public StringBuilder nameToUnicode(CharSequence name, StringBuilder dest, IDNA.Info info) {
        return this.process(name, false, false, dest, info);
    }

    private static boolean isASCIIString(CharSequence dest) {
        int length = dest.length();
        for (int i = 0; i < length; ++i) {
            if (dest.charAt(i) <= '\u007f') continue;
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
            if (toASCII) {
                UTS46.addError(info, IDNA.Error.EMPTY_LABEL);
            }
            return dest;
        }
        boolean disallowNonLDHDot = (this.options & 2) != 0;
        int labelStart = 0;
        int i = 0;
        while (true) {
            if (i == srcLength) {
                if (toASCII) {
                    if (i - labelStart > 63) {
                        UTS46.addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
                    }
                    if (!(isLabel || i < 254 || i <= 254 && labelStart >= i)) {
                        UTS46.addError(info, IDNA.Error.DOMAIN_NAME_TOO_LONG);
                    }
                }
                UTS46.promoteAndResetLabelErrors(info);
                return dest;
            }
            char c = src.charAt(i);
            if (c > '\u007f') break;
            byte cData = asciiData[c];
            if (cData > 0) {
                dest.append((char)(c + 32));
            } else {
                if (cData < 0 && disallowNonLDHDot) break;
                dest.append(c);
                if (c == '-') {
                    if (i == labelStart + 3 && src.charAt(i - 1) == '-') {
                        ++i;
                        break;
                    }
                    if (i == labelStart) {
                        UTS46.addLabelError(info, IDNA.Error.LEADING_HYPHEN);
                    }
                    if (i + 1 == srcLength || src.charAt(i + 1) == '.') {
                        UTS46.addLabelError(info, IDNA.Error.TRAILING_HYPHEN);
                    }
                } else if (c == '.') {
                    if (isLabel) {
                        ++i;
                        break;
                    }
                    if (toASCII) {
                        if (i == labelStart && i < srcLength - 1) {
                            UTS46.addLabelError(info, IDNA.Error.EMPTY_LABEL);
                        } else if (i - labelStart > 63) {
                            UTS46.addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
                        }
                    }
                    UTS46.promoteAndResetLabelErrors(info);
                    labelStart = i + 1;
                }
            }
            ++i;
        }
        UTS46.promoteAndResetLabelErrors(info);
        this.processUnicode(src, labelStart, i, isLabel, toASCII, dest, info);
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
            char c = dest.charAt(labelLimit);
            if (c == '.' && !isLabel) {
                int labelLength = labelLimit - labelStart;
                int newLength = this.processLabel(dest, labelStart, labelLength, toASCII, info);
                UTS46.promoteAndResetLabelErrors(info);
                destLength += newLength - labelLength;
                labelLimit = labelStart += newLength + 1;
                continue;
            }
            if ('\u00df' <= c && c <= '\u200d' && (c == '\u00df' || c == '\u03c2' || c >= '\u200c')) {
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
        int i = mappingStart;
        block5: while (i < length) {
            char c = dest.charAt(i);
            switch (c) {
                case '\u00df': {
                    didMapDevChars = true;
                    dest.setCharAt(i++, 's');
                    dest.insert(i++, 's');
                    ++length;
                    continue block5;
                }
                case '\u03c2': {
                    didMapDevChars = true;
                    dest.setCharAt(i++, '\u03c3');
                    continue block5;
                }
                case '\u200c': 
                case '\u200d': {
                    didMapDevChars = true;
                    dest.delete(i, i + 1);
                    --length;
                    continue block5;
                }
            }
            ++i;
        }
        if (didMapDevChars) {
            String normalized = uts46Norm2.normalize(dest.subSequence(labelStart, dest.length()));
            dest.replace(labelStart, Integer.MAX_VALUE, normalized);
            return dest.length();
        }
        return length;
    }

    private static boolean isNonASCIIDisallowedSTD3Valid(int c) {
        return c == 8800 || c == 8814 || c == 8815;
    }

    private static int replaceLabel(StringBuilder dest, int destLabelStart, int destLabelLength, CharSequence label, int labelLength) {
        if (label != dest) {
            dest.delete(destLabelStart, destLabelStart + destLabelLength).insert(destLabelStart, label);
        }
        return labelLength;
    }

    private int processLabel(StringBuilder dest, int labelStart, int labelLength, boolean toASCII, IDNA.Info info) {
        int c;
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
            catch (StringPrepParseException e) {
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
            if (toASCII) {
                UTS46.addLabelError(info, IDNA.Error.EMPTY_LABEL);
            }
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
        int i = labelStart;
        int limit = labelStart + labelLength;
        char oredChars = '\u0000';
        boolean bl = disallowNonLDHDot = (this.options & 2) != 0;
        do {
            if ((c = labelString.charAt(i)) <= 127) {
                if (c == 46) {
                    UTS46.addLabelError(info, IDNA.Error.LABEL_HAS_DOT);
                    labelString.setCharAt(i, '\ufffd');
                    continue;
                }
                if (!disallowNonLDHDot || asciiData[c] >= 0) continue;
                UTS46.addLabelError(info, IDNA.Error.DISALLOWED);
                labelString.setCharAt(i, '\ufffd');
                continue;
            }
            oredChars = (char)(oredChars | c);
            if (disallowNonLDHDot && UTS46.isNonASCIIDisallowedSTD3Valid(c)) {
                UTS46.addLabelError(info, IDNA.Error.DISALLOWED);
                labelString.setCharAt(i, '\ufffd');
                continue;
            }
            if (c != 65533) continue;
            UTS46.addLabelError(info, IDNA.Error.DISALLOWED);
        } while (++i < limit);
        c = labelString.codePointAt(labelStart);
        if ((UTS46.U_GET_GC_MASK(c) & U_GC_M_MASK) != 0) {
            UTS46.addLabelError(info, IDNA.Error.LEADING_COMBINING_MARK);
            labelString.setCharAt(labelStart, '\ufffd');
            if (c > 65535) {
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
                    catch (StringPrepParseException e) {
                        throw new RuntimeException(e);
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
        int i = labelStart + 4;
        int limit = labelStart + labelLength;
        do {
            char c;
            if ((c = dest.charAt(i)) <= '\u007f') {
                if (c == '.') {
                    UTS46.addLabelError(info, IDNA.Error.LABEL_HAS_DOT);
                    dest.setCharAt(i, '\ufffd');
                    onlyLDH = false;
                    isASCII = false;
                    continue;
                }
                if (asciiData[c] >= 0) continue;
                onlyLDH = false;
                if (!disallowNonLDHDot) continue;
                dest.setCharAt(i, '\ufffd');
                isASCII = false;
                continue;
            }
            onlyLDH = false;
            isASCII = false;
        } while (++i < limit);
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
        int c;
        int i;
        block11: {
            int dir;
            i = labelStart;
            c = Character.codePointAt(label, i);
            i += Character.charCount(c);
            firstMask = UTS46.U_MASK(UBiDiProps.INSTANCE.getClass(c));
            if ((firstMask & ~L_R_AL_MASK) != 0) {
                UTS46.setNotOkBiDi(info);
            }
            labelLimit = labelStart + labelLength;
            do {
                if (i >= labelLimit) {
                    lastMask = firstMask;
                    break block11;
                }
                c = Character.codePointBefore(label, labelLimit);
                labelLimit -= Character.charCount(c);
            } while ((dir = UBiDiProps.INSTANCE.getClass(c)) == 17);
            lastMask = UTS46.U_MASK(dir);
        }
        if ((firstMask & L_MASK) != 0 ? (lastMask & ~L_EN_MASK) != 0 : (lastMask & ~R_AL_EN_AN_MASK) != 0) {
            UTS46.setNotOkBiDi(info);
        }
        int mask = 0;
        while (i < labelLimit) {
            c = Character.codePointAt(label, i);
            i += Character.charCount(c);
            mask |= UTS46.U_MASK(UBiDiProps.INSTANCE.getClass(c));
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
        if (((firstMask | mask | lastMask) & R_AL_AN_MASK) != 0) {
            UTS46.setBiDi(info);
        }
    }

    private static boolean isASCIIOkBiDi(CharSequence s, int length) {
        int labelStart = 0;
        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            if (c == '.') {
                if (!(i <= labelStart || 'a' <= (c = s.charAt(i - 1)) && c <= 'z' || '0' <= c && c <= '9')) {
                    return false;
                }
                labelStart = i + 1;
                continue;
            }
            if (!(i == labelStart ? 'a' > c || c > 'z' : c <= ' ' && (c >= '\u001c' || '\t' <= c && c <= '\r'))) continue;
            return false;
        }
        return true;
    }

    private boolean isLabelOkContextJ(CharSequence label, int labelStart, int labelLength) {
        int labelLimit = labelStart + labelLength;
        for (int i = labelStart; i < labelLimit; ++i) {
            int c;
            if (label.charAt(i) == '\u200c') {
                int type;
                if (i == labelStart) {
                    return false;
                }
                int j = i;
                c = Character.codePointBefore(label, j);
                j -= Character.charCount(c);
                if (uts46Norm2.getCombiningClass(c) == 9) continue;
                while ((type = UBiDiProps.INSTANCE.getJoiningType(c)) == 5) {
                    if (j == 0) {
                        return false;
                    }
                    c = Character.codePointBefore(label, j);
                    j -= Character.charCount(c);
                }
                if (type != 3 && type != 2) {
                    return false;
                }
                j = i + 1;
                do {
                    if (j == labelLimit) {
                        return false;
                    }
                    c = Character.codePointAt(label, j);
                    j += Character.charCount(c);
                } while ((type = UBiDiProps.INSTANCE.getJoiningType(c)) == 5);
                if (type == 4 || type == 2) continue;
                return false;
            }
            if (label.charAt(i) != '\u200d') continue;
            if (i == labelStart) {
                return false;
            }
            c = Character.codePointBefore(label, i);
            if (uts46Norm2.getCombiningClass(c) == 9) continue;
            return false;
        }
        return true;
    }

    private void checkLabelContextO(CharSequence label, int labelStart, int labelLength, IDNA.Info info) {
        int labelEnd = labelStart + labelLength - 1;
        int arabicDigits = 0;
        block0: for (int i = labelStart; i <= labelEnd; ++i) {
            int c = label.charAt(i);
            if (c < 183) continue;
            if (c <= 1785) {
                if (c == 183) {
                    if (labelStart < i && label.charAt(i - 1) == 'l' && i < labelEnd && label.charAt(i + 1) == 'l') continue;
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_PUNCTUATION);
                    continue;
                }
                if (c == 885) {
                    if (i < labelEnd && 14 == UScript.getScript(Character.codePointAt(label, i + 1))) continue;
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_PUNCTUATION);
                    continue;
                }
                if (c == 1523 || c == 1524) {
                    if (labelStart < i && 19 == UScript.getScript(Character.codePointBefore(label, i))) continue;
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_PUNCTUATION);
                    continue;
                }
                if (1632 > c) continue;
                if (c <= 1641) {
                    if (arabicDigits > 0) {
                        UTS46.addLabelError(info, IDNA.Error.CONTEXTO_DIGITS);
                    }
                    arabicDigits = -1;
                    continue;
                }
                if (1776 > c) continue;
                if (arabicDigits < 0) {
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_DIGITS);
                }
                arabicDigits = 1;
                continue;
            }
            if (c != 12539) continue;
            int j = labelStart;
            while (true) {
                if (j > labelEnd) {
                    UTS46.addLabelError(info, IDNA.Error.CONTEXTO_PUNCTUATION);
                    continue block0;
                }
                c = Character.codePointAt(label, j);
                int script = UScript.getScript(c);
                if (script == 20 || script == 22 || script == 17) continue block0;
                j += Character.charCount(c);
            }
        }
    }

    private static int U_MASK(int x) {
        return 1 << x;
    }

    private static int U_GET_GC_MASK(int c) {
        return 1 << UCharacter.getType(c);
    }
}

