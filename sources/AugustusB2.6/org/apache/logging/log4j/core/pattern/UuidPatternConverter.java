// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.util.UuidUtil;
import java.util.UUID;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "UuidPatternConverter", category = "Converter")
@ConverterKeys({ "u", "uuid" })
public final class UuidPatternConverter extends LogEventPatternConverter
{
    private final boolean isRandom;
    
    private UuidPatternConverter(final boolean isRandom) {
        super("u", "uuid");
        this.isRandom = isRandom;
    }
    
    public static UuidPatternConverter newInstance(final String[] options) {
        if (options.length == 0) {
            return new UuidPatternConverter(false);
        }
        if (options.length > 1 || (!options[0].equalsIgnoreCase("RANDOM") && !options[0].equalsIgnoreCase("Time"))) {
            UuidPatternConverter.LOGGER.error("UUID Pattern Converter only accepts a single option with the value \"RANDOM\" or \"TIME\"");
        }
        return new UuidPatternConverter(options[0].equalsIgnoreCase("RANDOM"));
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final UUID uuid = this.isRandom ? UUID.randomUUID() : UuidUtil.getTimeBasedUuid();
        toAppendTo.append(uuid.toString());
    }
}
