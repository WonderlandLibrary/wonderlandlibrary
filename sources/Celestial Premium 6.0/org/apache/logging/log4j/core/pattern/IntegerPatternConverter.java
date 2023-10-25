/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import java.util.Date;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.AbstractPatternConverter;
import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
import org.apache.logging.log4j.core.pattern.ConverterKeys;

@Plugin(name="IntegerPatternConverter", category="FileConverter")
@ConverterKeys(value={"i", "index"})
public final class IntegerPatternConverter
extends AbstractPatternConverter
implements ArrayPatternConverter {
    private static final IntegerPatternConverter INSTANCE = new IntegerPatternConverter();

    private IntegerPatternConverter() {
        super("Integer", "integer");
    }

    public static IntegerPatternConverter newInstance(String[] options) {
        return INSTANCE;
    }

    @Override
    public void format(StringBuilder toAppendTo, Object ... objects) {
        for (Object obj : objects) {
            if (!(obj instanceof Integer)) continue;
            this.format(obj, toAppendTo);
            break;
        }
    }

    @Override
    public void format(Object obj, StringBuilder toAppendTo) {
        if (obj instanceof Integer) {
            toAppendTo.append(obj.toString());
        }
        if (obj instanceof Date) {
            toAppendTo.append(Long.toString(((Date)obj).getTime()));
        }
    }
}

