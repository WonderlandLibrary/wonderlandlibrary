// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util.datetime;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.FieldPosition;

public abstract class Format
{
    public final String format(final Object obj) {
        return this.format(obj, new StringBuilder(), new FieldPosition(0)).toString();
    }
    
    public abstract StringBuilder format(final Object obj, final StringBuilder toAppendTo, final FieldPosition pos);
    
    public abstract Object parseObject(final String source, final ParsePosition pos);
    
    public Object parseObject(final String source) throws ParseException {
        final ParsePosition pos = new ParsePosition(0);
        final Object result = this.parseObject(source, pos);
        if (pos.getIndex() == 0) {
            throw new ParseException("Format.parseObject(String) failed", pos.getErrorIndex());
        }
        return result;
    }
}
