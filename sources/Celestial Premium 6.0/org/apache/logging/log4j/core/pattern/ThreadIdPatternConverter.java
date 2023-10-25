/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name="ThreadIdPatternConverter", category="Converter")
@ConverterKeys(value={"T", "tid", "threadId"})
@PerformanceSensitive(value={"allocation"})
public final class ThreadIdPatternConverter
extends LogEventPatternConverter {
    private static final ThreadIdPatternConverter INSTANCE = new ThreadIdPatternConverter();

    private ThreadIdPatternConverter() {
        super("ThreadId", "threadId");
    }

    public static ThreadIdPatternConverter newInstance(String[] options) {
        return INSTANCE;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        toAppendTo.append(event.getThreadId());
    }
}

