/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name="LoggerFqcnPatternConverter", category="Converter")
@ConverterKeys(value={"fqcn"})
@PerformanceSensitive(value={"allocation"})
public final class LoggerFqcnPatternConverter
extends LogEventPatternConverter {
    private static final LoggerFqcnPatternConverter INSTANCE = new LoggerFqcnPatternConverter();

    private LoggerFqcnPatternConverter() {
        super("LoggerFqcn", "loggerFqcn");
    }

    public static LoggerFqcnPatternConverter newInstance(String[] options) {
        return INSTANCE;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        toAppendTo.append(event.getLoggerFqcn());
    }
}

