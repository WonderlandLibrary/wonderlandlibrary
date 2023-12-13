/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.Modifier;
import com.ibm.icu.impl.number.NumberStringBuilder;
import com.ibm.icu.text.NumberFormat;

public class ConstantMultiFieldModifier
implements Modifier {
    protected final char[] prefixChars;
    protected final char[] suffixChars;
    protected final NumberFormat.Field[] prefixFields;
    protected final NumberFormat.Field[] suffixFields;
    private final boolean overwrite;
    private final boolean strong;

    public ConstantMultiFieldModifier(NumberStringBuilder prefix, NumberStringBuilder suffix, boolean overwrite, boolean strong) {
        this.prefixChars = prefix.toCharArray();
        this.suffixChars = suffix.toCharArray();
        this.prefixFields = prefix.toFieldArray();
        this.suffixFields = suffix.toFieldArray();
        this.overwrite = overwrite;
        this.strong = strong;
    }

    @Override
    public int apply(NumberStringBuilder output, int leftIndex, int rightIndex) {
        int length = output.insert(leftIndex, this.prefixChars, this.prefixFields);
        if (this.overwrite) {
            length += output.splice(leftIndex + length, rightIndex + length, "", 0, 0, null);
        }
        length += output.insert(rightIndex + length, this.suffixChars, this.suffixFields);
        return length;
    }

    @Override
    public int getPrefixLength() {
        return this.prefixChars.length;
    }

    @Override
    public int getCodePointCount() {
        return Character.codePointCount(this.prefixChars, 0, this.prefixChars.length) + Character.codePointCount(this.suffixChars, 0, this.suffixChars.length);
    }

    @Override
    public boolean isStrong() {
        return this.strong;
    }

    public String toString() {
        NumberStringBuilder temp = new NumberStringBuilder();
        this.apply(temp, 0, 0);
        int prefixLength = this.getPrefixLength();
        return String.format("<ConstantMultiFieldModifier prefix:'%s' suffix:'%s'>", temp.subSequence(0, prefixLength), temp.subSequence(prefixLength, temp.length()));
    }
}

