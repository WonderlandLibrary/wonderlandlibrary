// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ThreadPatternConverter", category = "Converter")
@ConverterKeys({ "t", "tn", "thread", "threadName" })
@PerformanceSensitive({ "allocation" })
public final class ThreadNamePatternConverter extends LogEventPatternConverter
{
    private static final ThreadNamePatternConverter INSTANCE;
    
    private ThreadNamePatternConverter() {
        super("Thread", "thread");
    }
    
    public static ThreadNamePatternConverter newInstance(final String[] options) {
        return ThreadNamePatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(event.getThreadName());
    }
    
    static {
        INSTANCE = new ThreadNamePatternConverter();
    }
}
