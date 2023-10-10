// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "RootThrowablePatternConverter", category = "Converter")
@ConverterKeys({ "rEx", "rThrowable", "rException" })
public final class RootThrowablePatternConverter extends ThrowablePatternConverter
{
    private RootThrowablePatternConverter(final Configuration config, final String[] options) {
        super("RootThrowable", "throwable", options, config);
    }
    
    public static RootThrowablePatternConverter newInstance(final Configuration config, final String[] options) {
        return new RootThrowablePatternConverter(config, options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final ThrowableProxy proxy = event.getThrownProxy();
        final Throwable throwable = event.getThrown();
        if (throwable != null && this.options.anyLines()) {
            if (proxy == null) {
                super.format(event, toAppendTo);
                return;
            }
            final String trace = proxy.getCauseStackTraceAsString(this.options.getIgnorePackages(), this.options.getTextRenderer(), this.getSuffix(event), this.options.getSeparator());
            final int len = toAppendTo.length();
            if (len > 0 && !Character.isWhitespace(toAppendTo.charAt(len - 1))) {
                toAppendTo.append(' ');
            }
            if (!this.options.allLines() || !Strings.LINE_SEPARATOR.equals(this.options.getSeparator())) {
                final StringBuilder sb = new StringBuilder();
                final String[] array = trace.split(Strings.LINE_SEPARATOR);
                for (int limit = this.options.minLines(array.length) - 1, i = 0; i <= limit; ++i) {
                    sb.append(array[i]);
                    if (i < limit) {
                        sb.append(this.options.getSeparator());
                    }
                }
                toAppendTo.append(sb.toString());
            }
            else {
                toAppendTo.append(trace);
            }
        }
    }
}
