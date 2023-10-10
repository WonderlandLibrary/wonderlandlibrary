// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LineSeparatorPatternConverter", category = "Converter")
@ConverterKeys({ "n" })
@PerformanceSensitive({ "allocation" })
public final class LineSeparatorPatternConverter extends LogEventPatternConverter
{
    private static final LineSeparatorPatternConverter INSTANCE;
    
    private LineSeparatorPatternConverter() {
        super("Line Sep", "lineSep");
    }
    
    public static LineSeparatorPatternConverter newInstance(final String[] options) {
        return LineSeparatorPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent ignored, final StringBuilder toAppendTo) {
        toAppendTo.append(Strings.LINE_SEPARATOR);
    }
    
    @Override
    public void format(final Object ignored, final StringBuilder output) {
        output.append(Strings.LINE_SEPARATOR);
    }
    
    @Override
    public boolean isVariable() {
        return false;
    }
    
    static {
        INSTANCE = new LineSeparatorPatternConverter();
    }
}
