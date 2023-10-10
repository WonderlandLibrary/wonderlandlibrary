// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MdcPatternConverter", category = "Converter")
@ConverterKeys({ "X", "mdc", "MDC" })
@PerformanceSensitive({ "allocation" })
public final class MdcPatternConverter extends LogEventPatternConverter
{
    private final String key;
    private final String[] keys;
    private final boolean full;
    private static final TriConsumer<String, Object, StringBuilder> WRITE_KEY_VALUES_INTO;
    
    private MdcPatternConverter(final String[] options) {
        super((options != null && options.length > 0) ? ("MDC{" + options[0] + '}') : "MDC", "mdc");
        if (options != null && options.length > 0) {
            this.full = false;
            if (options[0].indexOf(44) > 0) {
                this.keys = options[0].split(",");
                for (int i = 0; i < this.keys.length; ++i) {
                    this.keys[i] = this.keys[i].trim();
                }
                this.key = null;
            }
            else {
                this.keys = null;
                this.key = options[0];
            }
        }
        else {
            this.full = true;
            this.key = null;
            this.keys = null;
        }
    }
    
    public static MdcPatternConverter newInstance(final String[] options) {
        return new MdcPatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final ReadOnlyStringMap contextData = event.getContextData();
        if (this.full) {
            if (contextData == null || contextData.isEmpty()) {
                toAppendTo.append("{}");
                return;
            }
            appendFully(contextData, toAppendTo);
        }
        else if (this.keys != null) {
            if (contextData == null || contextData.isEmpty()) {
                toAppendTo.append("{}");
                return;
            }
            appendSelectedKeys(this.keys, contextData, toAppendTo);
        }
        else if (contextData != null) {
            final Object value = contextData.getValue(this.key);
            if (value != null) {
                StringBuilders.appendValue(toAppendTo, value);
            }
        }
    }
    
    private static void appendFully(final ReadOnlyStringMap contextData, final StringBuilder toAppendTo) {
        toAppendTo.append("{");
        final int start = toAppendTo.length();
        contextData.forEach(MdcPatternConverter.WRITE_KEY_VALUES_INTO, toAppendTo);
        final int end = toAppendTo.length();
        if (end > start) {
            toAppendTo.setCharAt(end - 2, '}');
            toAppendTo.deleteCharAt(end - 1);
        }
        else {
            toAppendTo.append('}');
        }
    }
    
    private static void appendSelectedKeys(final String[] keys, final ReadOnlyStringMap contextData, final StringBuilder sb) {
        final int start = sb.length();
        sb.append('{');
        for (int i = 0; i < keys.length; ++i) {
            final String theKey = keys[i];
            final Object value = contextData.getValue(theKey);
            if (value != null) {
                if (sb.length() - start > 1) {
                    sb.append(", ");
                }
                sb.append(theKey).append('=');
                StringBuilders.appendValue(sb, value);
            }
        }
        sb.append('}');
    }
    
    static {
        WRITE_KEY_VALUES_INTO = ((key, value, sb) -> {
            sb.append(key).append('=');
            StringBuilders.appendValue(sb, value);
            sb.append(", ");
        });
    }
}
