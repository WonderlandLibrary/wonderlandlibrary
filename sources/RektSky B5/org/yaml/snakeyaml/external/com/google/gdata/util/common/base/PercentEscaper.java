/*
 * Decompiled with CFR 0.152.
 */
package org.yaml.snakeyaml.external.com.google.gdata.util.common.base;

import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.UnicodeEscaper;

public class PercentEscaper
extends UnicodeEscaper {
    public static final String SAFECHARS_URLENCODER = "-_.*";
    public static final String SAFEPATHCHARS_URLENCODER = "-_.!~*'()@:$&,;=";
    public static final String SAFEQUERYSTRINGCHARS_URLENCODER = "-_.!~*'()@:$,;/?:";
    private static final char[] URI_ESCAPED_SPACE = new char[]{'+'};
    private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    private final boolean plusForSpace;
    private final boolean[] safeOctets;

    public PercentEscaper(String safeChars, boolean plusForSpace) {
        if (safeChars.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        }
        if (plusForSpace && safeChars.contains(" ")) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        }
        if (safeChars.contains("%")) {
            throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
        }
        this.plusForSpace = plusForSpace;
        this.safeOctets = PercentEscaper.createSafeOctets(safeChars);
    }

    private static boolean[] createSafeOctets(String safeChars) {
        int c2;
        char[] safeCharArray;
        int maxChar = 122;
        for (char c3 : safeCharArray = safeChars.toCharArray()) {
            maxChar = Math.max(c3, maxChar);
        }
        boolean[] octets = new boolean[maxChar + 1];
        for (c2 = 48; c2 <= 57; ++c2) {
            octets[c2] = true;
        }
        for (c2 = 65; c2 <= 90; ++c2) {
            octets[c2] = true;
        }
        for (c2 = 97; c2 <= 122; ++c2) {
            octets[c2] = true;
        }
        for (char c4 : safeCharArray) {
            octets[c4] = true;
        }
        return octets;
    }

    @Override
    protected int nextEscapeIndex(CharSequence csq, int index, int end) {
        char c2;
        while (index < end && (c2 = csq.charAt(index)) < this.safeOctets.length && this.safeOctets[c2]) {
            ++index;
        }
        return index;
    }

    @Override
    public String escape(String s2) {
        int slen = s2.length();
        for (int index = 0; index < slen; ++index) {
            char c2 = s2.charAt(index);
            if (c2 < this.safeOctets.length && this.safeOctets[c2]) continue;
            return this.escapeSlow(s2, index);
        }
        return s2;
    }

    @Override
    protected char[] escape(int cp) {
        if (cp < this.safeOctets.length && this.safeOctets[cp]) {
            return null;
        }
        if (cp == 32 && this.plusForSpace) {
            return URI_ESCAPED_SPACE;
        }
        if (cp <= 127) {
            char[] dest = new char[3];
            dest[0] = 37;
            dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
            dest[1] = UPPER_HEX_DIGITS[cp >>> 4];
            return dest;
        }
        if (cp <= 2047) {
            char[] dest = new char[6];
            dest[0] = 37;
            dest[3] = 37;
            dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
            dest[4] = UPPER_HEX_DIGITS[8 | (cp >>>= 4) & 3];
            dest[2] = UPPER_HEX_DIGITS[(cp >>>= 2) & 0xF];
            dest[1] = UPPER_HEX_DIGITS[0xC | (cp >>>= 4)];
            return dest;
        }
        if (cp <= 65535) {
            char[] dest = new char[9];
            dest[0] = 37;
            dest[1] = 69;
            dest[3] = 37;
            dest[6] = 37;
            dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
            dest[7] = UPPER_HEX_DIGITS[8 | (cp >>>= 4) & 3];
            dest[5] = UPPER_HEX_DIGITS[(cp >>>= 2) & 0xF];
            dest[4] = UPPER_HEX_DIGITS[8 | (cp >>>= 4) & 3];
            dest[2] = UPPER_HEX_DIGITS[cp >>>= 2];
            return dest;
        }
        if (cp <= 0x10FFFF) {
            char[] dest = new char[12];
            dest[0] = 37;
            dest[1] = 70;
            dest[3] = 37;
            dest[6] = 37;
            dest[9] = 37;
            dest[11] = UPPER_HEX_DIGITS[cp & 0xF];
            dest[10] = UPPER_HEX_DIGITS[8 | (cp >>>= 4) & 3];
            dest[8] = UPPER_HEX_DIGITS[(cp >>>= 2) & 0xF];
            dest[7] = UPPER_HEX_DIGITS[8 | (cp >>>= 4) & 3];
            dest[5] = UPPER_HEX_DIGITS[(cp >>>= 2) & 0xF];
            dest[4] = UPPER_HEX_DIGITS[8 | (cp >>>= 4) & 3];
            dest[2] = UPPER_HEX_DIGITS[(cp >>>= 2) & 7];
            return dest;
        }
        throw new IllegalArgumentException("Invalid unicode character value " + cp);
    }
}

