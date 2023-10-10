// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ThreadIdPatternConverter", category = "Converter")
@ConverterKeys({ "T", "tid", "threadId" })
@PerformanceSensitive({ "allocation" })
public final class ThreadIdPatternConverter extends LogEventPatternConverter
{
    private static final ThreadIdPatternConverter INSTANCE;
    
    private ThreadIdPatternConverter() {
        super("ThreadId", "threadId");
    }
    
    public static ThreadIdPatternConverter newInstance(final String[] options) {
        return ThreadIdPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(event.getThreadId());
    }
    
    static {
        INSTANCE = new ThreadIdPatternConverter();
    }
}
