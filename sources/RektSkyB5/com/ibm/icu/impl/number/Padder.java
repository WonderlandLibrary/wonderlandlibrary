/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.DecimalFormatProperties;
import com.ibm.icu.impl.number.Modifier;
import com.ibm.icu.impl.number.NumberStringBuilder;

public class Padder {
    public static final String FALLBACK_PADDING_STRING = " ";
    public static final Padder NONE = new Padder(null, -1, null);
    String paddingString;
    int targetWidth;
    PadPosition position;

    public Padder(String paddingString, int targetWidth, PadPosition position) {
        this.paddingString = paddingString == null ? FALLBACK_PADDING_STRING : paddingString;
        this.targetWidth = targetWidth;
        this.position = position == null ? PadPosition.BEFORE_PREFIX : position;
    }

    public static Padder none() {
        return NONE;
    }

    public static Padder codePoints(int cp, int targetWidth, PadPosition position) {
        if (targetWidth >= 0) {
            String paddingString = String.valueOf(Character.toChars(cp));
            return new Padder(paddingString, targetWidth, position);
        }
        throw new IllegalArgumentException("Padding width must not be negative");
    }

    public static Padder forProperties(DecimalFormatProperties properties) {
        return new Padder(properties.getPadString(), properties.getFormatWidth(), properties.getPadPosition());
    }

    public boolean isValid() {
        return this.targetWidth > 0;
    }

    public int padAndApply(Modifier mod1, Modifier mod2, NumberStringBuilder string, int leftIndex, int rightIndex) {
        int modLength = mod1.getCodePointCount() + mod2.getCodePointCount();
        int requiredPadding = this.targetWidth - modLength - string.codePointCount();
        assert (leftIndex == 0 && rightIndex == string.length());
        int length = 0;
        if (requiredPadding <= 0) {
            length += mod1.apply(string, leftIndex, rightIndex);
            length += mod2.apply(string, leftIndex, rightIndex + length);
            return length;
        }
        if (this.position == PadPosition.AFTER_PREFIX) {
            length += Padder.addPaddingHelper(this.paddingString, requiredPadding, string, leftIndex);
        } else if (this.position == PadPosition.BEFORE_SUFFIX) {
            length += Padder.addPaddingHelper(this.paddingString, requiredPadding, string, rightIndex + length);
        }
        length += mod1.apply(string, leftIndex, rightIndex + length);
        length += mod2.apply(string, leftIndex, rightIndex + length);
        if (this.position == PadPosition.BEFORE_PREFIX) {
            length += Padder.addPaddingHelper(this.paddingString, requiredPadding, string, leftIndex);
        } else if (this.position == PadPosition.AFTER_SUFFIX) {
            length += Padder.addPaddingHelper(this.paddingString, requiredPadding, string, rightIndex + length);
        }
        return length;
    }

    private static int addPaddingHelper(String paddingString, int requiredPadding, NumberStringBuilder string, int index) {
        for (int i2 = 0; i2 < requiredPadding; ++i2) {
            string.insert(index, paddingString, null);
        }
        return paddingString.length() * requiredPadding;
    }

    public static enum PadPosition {
        BEFORE_PREFIX,
        AFTER_PREFIX,
        BEFORE_SUFFIX,
        AFTER_SUFFIX;


        public static PadPosition fromOld(int old) {
            switch (old) {
                case 0: {
                    return BEFORE_PREFIX;
                }
                case 1: {
                    return AFTER_PREFIX;
                }
                case 2: {
                    return BEFORE_SUFFIX;
                }
                case 3: {
                    return AFTER_SUFFIX;
                }
            }
            throw new IllegalArgumentException("Don't know how to map " + old);
        }

        public int toOld() {
            switch (this) {
                case BEFORE_PREFIX: {
                    return 0;
                }
                case AFTER_PREFIX: {
                    return 1;
                }
                case BEFORE_SUFFIX: {
                    return 2;
                }
                case AFTER_SUFFIX: {
                    return 3;
                }
            }
            return -1;
        }
    }
}

