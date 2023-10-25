/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(name="SequenceNumberPatternConverter", category="Converter")
@ConverterKeys(value={"sn", "sequenceNumber"})
public final class SequenceNumberPatternConverter
extends LogEventPatternConverter {
    private static final AtomicLong SEQUENCE = new AtomicLong();
    private static final SequenceNumberPatternConverter INSTANCE = new SequenceNumberPatternConverter();

    private SequenceNumberPatternConverter() {
        super("Sequence Number", "sn");
    }

    public static SequenceNumberPatternConverter newInstance(String[] options) {
        return INSTANCE;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        toAppendTo.append(Long.toString(SEQUENCE.incrementAndGet()));
    }
}

