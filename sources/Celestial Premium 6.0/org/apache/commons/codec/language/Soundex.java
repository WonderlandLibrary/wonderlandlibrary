/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

public class Soundex
implements StringEncoder {
    public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
    private static final char[] US_ENGLISH_MAPPING = "01230120022455012623010202".toCharArray();
    public static final Soundex US_ENGLISH = new Soundex();
    @Deprecated
    private int maxLength = 4;
    private final char[] soundexMapping;

    public Soundex() {
        this.soundexMapping = US_ENGLISH_MAPPING;
    }

    public Soundex(char[] mapping) {
        this.soundexMapping = new char[mapping.length];
        System.arraycopy(mapping, 0, this.soundexMapping, 0, mapping.length);
    }

    public Soundex(String mapping) {
        this.soundexMapping = mapping.toCharArray();
    }

    public int difference(String s1, String s2) throws EncoderException {
        return SoundexUtils.difference(this, s1, s2);
    }

    @Override
    public Object encode(Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("Parameter supplied to Soundex encode is not of type java.lang.String");
        }
        return this.soundex((String)obj);
    }

    @Override
    public String encode(String str) {
        return this.soundex(str);
    }

    private char getMappingCode(String str, int index) {
        char preHWChar;
        char firstCode;
        char hwChar;
        char mappedChar = this.map(str.charAt(index));
        if (!(index <= 1 || mappedChar == '0' || 'H' != (hwChar = str.charAt(index - 1)) && 'W' != hwChar || (firstCode = this.map(preHWChar = str.charAt(index - 2))) != mappedChar && 'H' != preHWChar && 'W' != preHWChar)) {
            return '\u0000';
        }
        return mappedChar;
    }

    @Deprecated
    public int getMaxLength() {
        return this.maxLength;
    }

    private char[] getSoundexMapping() {
        return this.soundexMapping;
    }

    private char map(char ch) {
        int index = ch - 65;
        if (index < 0 || index >= this.getSoundexMapping().length) {
            throw new IllegalArgumentException("The character is not mapped: " + ch);
        }
        return this.getSoundexMapping()[index];
    }

    @Deprecated
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String soundex(String str) {
        if (str == null) {
            return null;
        }
        if ((str = SoundexUtils.clean(str)).length() == 0) {
            return str;
        }
        char[] out = new char[]{'0', '0', '0', '0'};
        int incount = 1;
        int count = 1;
        out[0] = str.charAt(0);
        char last = this.getMappingCode(str, 0);
        while (incount < str.length() && count < out.length) {
            char mapped;
            if ((mapped = this.getMappingCode(str, incount++)) == '\u0000') continue;
            if (mapped != '0' && mapped != last) {
                out[count++] = mapped;
            }
            last = mapped;
        }
        return new String(out);
    }
}

