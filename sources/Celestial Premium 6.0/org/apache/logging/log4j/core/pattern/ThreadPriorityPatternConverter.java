/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name="ThreadPriorityPatternConverter", category="Converter")
@ConverterKeys(value={"tp", "threadPriority"})
@PerformanceSensitive(value={"allocation"})
public final class ThreadPriorityPatternConverter
extends LogEventPatternConverter {
    private static final ThreadPriorityPatternConverter INSTANCE = new ThreadPriorityPatternConverter();

    private ThreadPriorityPatternConverter() {
        super("ThreadPriority", "threadPriority");
    }

    public static ThreadPriorityPatternConverter newInstance(String[] options) {
        return INSTANCE;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        toAppendTo.append(event.getThreadPriority());
    }
}

