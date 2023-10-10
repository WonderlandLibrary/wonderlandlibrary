// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ThreadPriorityPatternConverter", category = "Converter")
@ConverterKeys({ "tp", "threadPriority" })
@PerformanceSensitive({ "allocation" })
public final class ThreadPriorityPatternConverter extends LogEventPatternConverter
{
    private static final ThreadPriorityPatternConverter INSTANCE;
    
    private ThreadPriorityPatternConverter() {
        super("ThreadPriority", "threadPriority");
    }
    
    public static ThreadPriorityPatternConverter newInstance(final String[] options) {
        return ThreadPriorityPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(event.getThreadPriority());
    }
    
    static {
        INSTANCE = new ThreadPriorityPatternConverter();
    }
}
