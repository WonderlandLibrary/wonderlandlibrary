/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name="NanoTimePatternConverter", category="Converter")
@ConverterKeys(value={"N", "nano"})
@PerformanceSensitive(value={"allocation"})
public final class NanoTimePatternConverter
extends LogEventPatternConverter {
    private NanoTimePatternConverter(String[] options) {
        super("Nanotime", "nanotime");
    }

    public static NanoTimePatternConverter newInstance(String[] options) {
        return new NanoTimePatternConverter(options);
    }

    @Override
    public void format(LogEvent event, StringBuilder output) {
        output.append(event.getNanoTime());
    }
}

