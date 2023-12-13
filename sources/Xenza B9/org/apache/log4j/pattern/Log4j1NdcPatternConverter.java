// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.pattern;

import java.util.List;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(name = "Log4j1NdcPatternConverter", category = "Converter")
@ConverterKeys({ "ndc" })
public final class Log4j1NdcPatternConverter extends LogEventPatternConverter
{
    private static final Log4j1NdcPatternConverter INSTANCE;
    
    private Log4j1NdcPatternConverter() {
        super("Log4j1NDC", "ndc");
    }
    
    public static Log4j1NdcPatternConverter newInstance(final String[] options) {
        return Log4j1NdcPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final List<String> ndc = event.getContextStack().asList();
        toAppendTo.append(Strings.join(ndc, ' '));
    }
    
    static {
        INSTANCE = new Log4j1NdcPatternConverter();
    }
}
