/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util.datetime;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;

public abstract class Format {
    public final String format(Object obj) {
        return this.format(obj, new StringBuilder(), new FieldPosition(0)).toString();
    }

    public abstract StringBuilder format(Object var1, StringBuilder var2, FieldPosition var3);

    public abstract Object parseObject(String var1, ParsePosition var2);

    public Object parseObject(String source) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Object result = this.parseObject(source, pos);
        if (pos.getIndex() == 0) {
            throw new ParseException("Format.parseObject(String) failed", pos.getErrorIndex());
        }
        return result;
    }
}

