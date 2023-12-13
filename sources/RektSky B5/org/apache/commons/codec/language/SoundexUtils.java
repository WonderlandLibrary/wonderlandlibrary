/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

final class SoundexUtils {
    SoundexUtils() {
    }

    static String clean(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        int len = str.length();
        char[] chars = new char[len];
        int count = 0;
        for (int i2 = 0; i2 < len; ++i2) {
            if (!Character.isLetter(str.charAt(i2))) continue;
            chars[count++] = str.charAt(i2);
        }
        if (count == len) {
            return str.toUpperCase(Locale.ENGLISH);
        }
        return new String(chars, 0, count).toUpperCase(Locale.ENGLISH);
    }

    static int difference(StringEncoder encoder, String s1, String s2) throws EncoderException {
        return SoundexUtils.differenceEncoded(encoder.encode(s1), encoder.encode(s2));
    }

    static int differenceEncoded(String es1, String es2) {
        if (es1 == null || es2 == null) {
            return 0;
        }
        int lengthToMatch = Math.min(es1.length(), es2.length());
        int diff = 0;
        for (int i2 = 0; i2 < lengthToMatch; ++i2) {
            if (es1.charAt(i2) != es2.charAt(i2)) continue;
            ++diff;
        }
        return diff;
    }
}

