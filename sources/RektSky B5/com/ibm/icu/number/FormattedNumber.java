/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.number;

import com.ibm.icu.impl.number.DecimalQuantity;
import com.ibm.icu.impl.number.NumberStringBuilder;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.ICUUncheckedIOException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.util.Arrays;

public class FormattedNumber {
    final NumberStringBuilder nsb;
    final DecimalQuantity fq;

    FormattedNumber(NumberStringBuilder nsb, DecimalQuantity fq) {
        this.nsb = nsb;
        this.fq = fq;
    }

    public String toString() {
        return this.nsb.toString();
    }

    public <A extends Appendable> A appendTo(A appendable) {
        try {
            appendable.append(this.nsb);
        }
        catch (IOException e2) {
            throw new ICUUncheckedIOException(e2);
        }
        return appendable;
    }

    @Deprecated
    public void populateFieldPosition(FieldPosition fieldPosition) {
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        this.nextFieldPosition(fieldPosition);
    }

    public boolean nextFieldPosition(FieldPosition fieldPosition) {
        this.fq.populateUFieldPosition(fieldPosition);
        return this.nsb.nextFieldPosition(fieldPosition);
    }

    @Deprecated
    public AttributedCharacterIterator getFieldIterator() {
        return this.nsb.toCharacterIterator();
    }

    public AttributedCharacterIterator toCharacterIterator() {
        return this.nsb.toCharacterIterator();
    }

    public BigDecimal toBigDecimal() {
        return this.fq.toBigDecimal();
    }

    @Deprecated
    public PluralRules.IFixedDecimal getFixedDecimal() {
        return this.fq;
    }

    public int hashCode() {
        return Arrays.hashCode(this.nsb.toCharArray()) ^ Arrays.hashCode(this.nsb.toFieldArray()) ^ this.fq.toBigDecimal().hashCode();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof FormattedNumber)) {
            return false;
        }
        FormattedNumber _other = (FormattedNumber)other;
        return Arrays.equals(this.nsb.toCharArray(), _other.nsb.toCharArray()) && Arrays.equals(this.nsb.toFieldArray(), _other.nsb.toFieldArray()) && this.fq.toBigDecimal().equals(_other.fq.toBigDecimal());
    }
}

