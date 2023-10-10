// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LoggerFqcnPatternConverter", category = "Converter")
@ConverterKeys({ "fqcn" })
@PerformanceSensitive({ "allocation" })
public final class LoggerFqcnPatternConverter extends LogEventPatternConverter
{
    private static final LoggerFqcnPatternConverter INSTANCE;
    
    private LoggerFqcnPatternConverter() {
        super("LoggerFqcn", "loggerFqcn");
    }
    
    public static LoggerFqcnPatternConverter newInstance(final String[] options) {
        return LoggerFqcnPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(event.getLoggerFqcn());
    }
    
    static {
        INSTANCE = new LoggerFqcnPatternConverter();
    }
}
