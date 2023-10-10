// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.LocationAware;

@Plugin(name = "MethodLocationPatternConverter", category = "Converter")
@ConverterKeys({ "M", "method" })
public final class MethodLocationPatternConverter extends LogEventPatternConverter implements LocationAware
{
    private static final MethodLocationPatternConverter INSTANCE;
    
    private MethodLocationPatternConverter() {
        super("Method", "method");
    }
    
    public static MethodLocationPatternConverter newInstance(final String[] options) {
        return MethodLocationPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final StackTraceElement element = event.getSource();
        if (element != null) {
            toAppendTo.append(element.getMethodName());
        }
    }
    
    @Override
    public boolean requiresLocation() {
        return true;
    }
    
    static {
        INSTANCE = new MethodLocationPatternConverter();
    }
}
