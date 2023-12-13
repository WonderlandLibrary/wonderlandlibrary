/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.Modifier;
import com.ibm.icu.impl.number.NumberStringBuilder;
import com.ibm.icu.text.NumberFormat;

public class ConstantAffixModifier
implements Modifier {
    public static final ConstantAffixModifier EMPTY = new ConstantAffixModifier();
    private final String prefix;
    private final String suffix;
    private final NumberFormat.Field field;
    private final boolean strong;

    public ConstantAffixModifier(String prefix, String suffix, NumberFormat.Field field, boolean strong) {
        this.prefix = prefix == null ? "" : prefix;
        this.suffix = suffix == null ? "" : suffix;
        this.field = field;
        this.strong = strong;
    }

    public ConstantAffixModifier() {
        this.prefix = "";
        this.suffix = "";
        this.field = null;
        this.strong = false;
    }

    @Override
    public int apply(NumberStringBuilder output, int leftIndex, int rightIndex) {
        int length = output.insert(rightIndex, this.suffix, this.field);
        return length += output.insert(leftIndex, this.prefix, this.field);
    }

    @Override
    public int getPrefixLength() {
        return this.prefix.length();
    }

    @Override
    public int getCodePointCount() {
        return this.prefix.codePointCount(0, this.prefix.length()) + this.suffix.codePointCount(0, this.suffix.length());
    }

    @Override
    public boolean isStrong() {
        return this.strong;
    }

    public String toString() {
        return String.format("<ConstantAffixModifier prefix:'%s' suffix:'%s'>", this.prefix, this.suffix);
    }
}

