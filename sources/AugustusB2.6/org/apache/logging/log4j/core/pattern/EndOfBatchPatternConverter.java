// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "EndOfBatchPatternConverter", category = "Converter")
@ConverterKeys({ "endOfBatch" })
@PerformanceSensitive({ "allocation" })
public final class EndOfBatchPatternConverter extends LogEventPatternConverter
{
    private static final EndOfBatchPatternConverter INSTANCE;
    
    private EndOfBatchPatternConverter() {
        super("LoggerFqcn", "loggerFqcn");
    }
    
    public static EndOfBatchPatternConverter newInstance(final String[] options) {
        return EndOfBatchPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(event.isEndOfBatch());
    }
    
    static {
        INSTANCE = new EndOfBatchPatternConverter();
    }
}
