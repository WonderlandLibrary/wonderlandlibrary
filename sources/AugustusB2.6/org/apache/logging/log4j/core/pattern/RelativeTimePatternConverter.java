// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "RelativeTimePatternConverter", category = "Converter")
@ConverterKeys({ "r", "relative" })
@PerformanceSensitive({ "allocation" })
public class RelativeTimePatternConverter extends LogEventPatternConverter
{
    private final long startTime;
    
    public RelativeTimePatternConverter() {
        super("Time", "time");
        this.startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
    }
    
    public static RelativeTimePatternConverter newInstance(final String[] options) {
        return new RelativeTimePatternConverter();
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final long timestamp = event.getTimeMillis();
        toAppendTo.append(timestamp - this.startTime);
    }
}
