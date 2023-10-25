/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.Constants;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;

@Plugin(name="RootThrowablePatternConverter", category="Converter")
@ConverterKeys(value={"rEx", "rThrowable", "rException"})
public final class RootThrowablePatternConverter
extends ThrowablePatternConverter {
    private RootThrowablePatternConverter(String[] options) {
        super("RootThrowable", "throwable", options);
    }

    public static RootThrowablePatternConverter newInstance(String[] options) {
        return new RootThrowablePatternConverter(options);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        Throwable throwable;
        ThrowableProxy proxy = null;
        if (event instanceof Log4jLogEvent) {
            proxy = ((Log4jLogEvent)event).getThrownProxy();
        }
        if ((throwable = event.getThrown()) != null && this.options.anyLines()) {
            if (proxy == null) {
                super.format(event, toAppendTo);
                return;
            }
            String trace = proxy.getRootCauseStackTrace(this.options.getPackages());
            int len = toAppendTo.length();
            if (len > 0 && !Character.isWhitespace(toAppendTo.charAt(len - 1))) {
                toAppendTo.append(" ");
            }
            if (!this.options.allLines() || !Constants.LINE_SEP.equals(this.options.getSeparator())) {
                StringBuilder sb = new StringBuilder();
                String[] array = trace.split(Constants.LINE_SEP);
                int limit = this.options.minLines(array.length) - 1;
                for (int i = 0; i <= limit; ++i) {
                    sb.append(array[i]);
                    if (i >= limit) continue;
                    sb.append(this.options.getSeparator());
                }
                toAppendTo.append(sb.toString());
            } else {
                toAppendTo.append(trace);
            }
        }
    }
}

