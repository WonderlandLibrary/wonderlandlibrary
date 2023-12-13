/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.SimpleFormatterImpl;
import com.ibm.icu.impl.number.Modifier;
import com.ibm.icu.impl.number.NumberStringBuilder;
import com.ibm.icu.text.NumberFormat;

public class SimpleModifier
implements Modifier {
    private final String compiledPattern;
    private final NumberFormat.Field field;
    private final boolean strong;
    private final int prefixLength;
    private final int suffixOffset;
    private final int suffixLength;
    private static final int ARG_NUM_LIMIT = 256;

    public SimpleModifier(String compiledPattern, NumberFormat.Field field, boolean strong) {
        assert (compiledPattern != null);
        this.compiledPattern = compiledPattern;
        this.field = field;
        this.strong = strong;
        int argLimit = SimpleFormatterImpl.getArgumentLimit(compiledPattern);
        if (argLimit == 0) {
            this.prefixLength = compiledPattern.charAt(1) - 256;
            assert (2 + this.prefixLength == compiledPattern.length());
            this.suffixOffset = -1;
            this.suffixLength = 0;
        } else {
            assert (argLimit == 1);
            if (compiledPattern.charAt(1) != '\u0000') {
                this.prefixLength = compiledPattern.charAt(1) - 256;
                this.suffixOffset = 3 + this.prefixLength;
            } else {
                this.prefixLength = 0;
                this.suffixOffset = 2;
            }
            this.suffixLength = 3 + this.prefixLength < compiledPattern.length() ? compiledPattern.charAt(this.suffixOffset) - 256 : 0;
        }
    }

    @Override
    public int apply(NumberStringBuilder output, int leftIndex, int rightIndex) {
        return this.formatAsPrefixSuffix(output, leftIndex, rightIndex, this.field);
    }

    @Override
    public int getPrefixLength() {
        return this.prefixLength;
    }

    @Override
    public int getCodePointCount() {
        int count = 0;
        if (this.prefixLength > 0) {
            count += Character.codePointCount(this.compiledPattern, 2, 2 + this.prefixLength);
        }
        if (this.suffixLength > 0) {
            count += Character.codePointCount(this.compiledPattern, 1 + this.suffixOffset, 1 + this.suffixOffset + this.suffixLength);
        }
        return count;
    }

    @Override
    public boolean isStrong() {
        return this.strong;
    }

    public int formatAsPrefixSuffix(NumberStringBuilder result, int startIndex, int endIndex, NumberFormat.Field field) {
        if (this.suffixOffset == -1) {
            return result.splice(startIndex, endIndex, this.compiledPattern, 2, 2 + this.prefixLength, field);
        }
        if (this.prefixLength > 0) {
            result.insert(startIndex, this.compiledPattern, 2, 2 + this.prefixLength, field);
        }
        if (this.suffixLength > 0) {
            result.insert(endIndex + this.prefixLength, this.compiledPattern, 1 + this.suffixOffset, 1 + this.suffixOffset + this.suffixLength, field);
        }
        return this.prefixLength + this.suffixLength;
    }
}

