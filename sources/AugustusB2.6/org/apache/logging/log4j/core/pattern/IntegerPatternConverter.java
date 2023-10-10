// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import java.util.Date;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "IntegerPatternConverter", category = "FileConverter")
@ConverterKeys({ "i", "index" })
@PerformanceSensitive({ "allocation" })
public final class IntegerPatternConverter extends AbstractPatternConverter implements ArrayPatternConverter
{
    private static final IntegerPatternConverter INSTANCE;
    
    private IntegerPatternConverter() {
        super("Integer", "integer");
    }
    
    public static IntegerPatternConverter newInstance(final String[] options) {
        return IntegerPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final StringBuilder toAppendTo, final Object... objects) {
        for (int i = 0; i < objects.length; ++i) {
            if (objects[i] instanceof Integer) {
                this.format(objects[i], toAppendTo);
                break;
            }
            if (objects[i] instanceof NotANumber) {
                toAppendTo.append("\u0000");
                break;
            }
        }
    }
    
    @Override
    public void format(final Object obj, final StringBuilder toAppendTo) {
        if (obj instanceof Integer) {
            toAppendTo.append((int)obj);
        }
        else if (obj instanceof Date) {
            toAppendTo.append(((Date)obj).getTime());
        }
    }
    
    static {
        INSTANCE = new IntegerPatternConverter();
    }
}
