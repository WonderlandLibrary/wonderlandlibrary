// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "NanoTimePatternConverter", category = "Converter")
@ConverterKeys({ "N", "nano" })
@PerformanceSensitive({ "allocation" })
public final class NanoTimePatternConverter extends LogEventPatternConverter
{
    private NanoTimePatternConverter(final String[] options) {
        super("Nanotime", "nanotime");
    }
    
    public static NanoTimePatternConverter newInstance(final String[] options) {
        return new NanoTimePatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
        output.append(event.getNanoTime());
    }
}
