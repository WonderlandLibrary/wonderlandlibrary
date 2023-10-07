// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(name = "Log4j1MdcPatternConverter", category = "Converter")
@ConverterKeys({ "properties" })
public final class Log4j1MdcPatternConverter extends LogEventPatternConverter
{
    private final String key;
    private static TriConsumer<String, Object, StringBuilder> APPEND_EACH;
    
    private Log4j1MdcPatternConverter(final String[] options) {
        super((options != null && options.length > 0) ? ("Log4j1MDC{" + options[0] + '}') : "Log4j1MDC", "property");
        if (options != null && options.length > 0) {
            this.key = options[0];
        }
        else {
            this.key = null;
        }
    }
    
    public static Log4j1MdcPatternConverter newInstance(final String[] options) {
        return new Log4j1MdcPatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        if (this.key == null) {
            toAppendTo.append('{');
            event.getContextData().forEach(Log4j1MdcPatternConverter.APPEND_EACH, toAppendTo);
            toAppendTo.append('}');
        }
        else {
            final Object val = event.getContextData().getValue(this.key);
            if (val != null) {
                toAppendTo.append(val);
            }
        }
    }
    
    static {
        Log4j1MdcPatternConverter.APPEND_EACH = ((key, value, toAppendTo) -> toAppendTo.append('{').append(key).append(',').append(value).append('}'));
    }
}
