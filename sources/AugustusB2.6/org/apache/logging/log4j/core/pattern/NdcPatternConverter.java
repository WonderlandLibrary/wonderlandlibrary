// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "NdcPatternConverter", category = "Converter")
@ConverterKeys({ "x", "NDC" })
public final class NdcPatternConverter extends LogEventPatternConverter
{
    private static final NdcPatternConverter INSTANCE;
    
    private NdcPatternConverter() {
        super("NDC", "ndc");
    }
    
    public static NdcPatternConverter newInstance(final String[] options) {
        return NdcPatternConverter.INSTANCE;
    }
    
    @PerformanceSensitive({ "allocation" })
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(event.getContextStack());
    }
    
    static {
        INSTANCE = new NdcPatternConverter();
    }
}
