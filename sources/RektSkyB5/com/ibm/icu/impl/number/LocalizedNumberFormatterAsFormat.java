/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.number.FormattedNumber;
import com.ibm.icu.number.LocalizedNumberFormatter;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.util.ULocale;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class LocalizedNumberFormatterAsFormat
extends Format {
    private static final long serialVersionUID = 1L;
    private final transient LocalizedNumberFormatter formatter;
    private final transient ULocale locale;

    public LocalizedNumberFormatterAsFormat(LocalizedNumberFormatter formatter, ULocale locale) {
        this.formatter = formatter;
        this.locale = locale;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (!(obj instanceof Number)) {
            throw new IllegalArgumentException();
        }
        FormattedNumber result = this.formatter.format((Number)obj);
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        boolean found = result.nextFieldPosition(pos);
        if (found && toAppendTo.length() != 0) {
            pos.setBeginIndex(pos.getBeginIndex() + toAppendTo.length());
            pos.setEndIndex(pos.getEndIndex() + toAppendTo.length());
        }
        result.appendTo(toAppendTo);
        return toAppendTo;
    }

    @Override
    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        if (!(obj instanceof Number)) {
            throw new IllegalArgumentException();
        }
        return this.formatter.format((Number)obj).toCharacterIterator();
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

    public LocalizedNumberFormatter getNumberFormatter() {
        return this.formatter;
    }

    public int hashCode() {
        return this.formatter.hashCode();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof LocalizedNumberFormatterAsFormat)) {
            return false;
        }
        return this.formatter.equals(((LocalizedNumberFormatterAsFormat)other).getNumberFormatter());
    }

    private Object writeReplace() throws ObjectStreamException {
        Proxy proxy = new Proxy();
        proxy.languageTag = this.locale.toLanguageTag();
        proxy.skeleton = this.formatter.toSkeleton();
        return proxy;
    }

    static class Proxy
    implements Externalizable {
        private static final long serialVersionUID = 1L;
        String languageTag;
        String skeleton;

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(0);
            out.writeUTF(this.languageTag);
            out.writeUTF(this.skeleton);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            in.readByte();
            this.languageTag = in.readUTF();
            this.skeleton = in.readUTF();
        }

        private Object readResolve() throws ObjectStreamException {
            return NumberFormatter.forSkeleton(this.skeleton).locale(ULocale.forLanguageTag(this.languageTag)).toFormat();
        }
    }
}

