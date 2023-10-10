// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.List;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "maxLength", category = "Converter")
@ConverterKeys({ "maxLength", "maxLen" })
@PerformanceSensitive({ "allocation" })
public final class MaxLengthConverter extends LogEventPatternConverter
{
    private final List<PatternFormatter> formatters;
    private final int maxLength;
    
    public static MaxLengthConverter newInstance(final Configuration config, final String[] options) {
        if (options.length != 2) {
            MaxLengthConverter.LOGGER.error("Incorrect number of options on maxLength: expected 2 received {}: {}", (Object)options.length, options);
            return null;
        }
        if (options[0] == null) {
            MaxLengthConverter.LOGGER.error("No pattern supplied on maxLength");
            return null;
        }
        if (options[1] == null) {
            MaxLengthConverter.LOGGER.error("No length supplied on maxLength");
            return null;
        }
        final PatternParser parser = PatternLayout.createPatternParser(config);
        final List<PatternFormatter> formatters = parser.parse(options[0]);
        return new MaxLengthConverter(formatters, AbstractAppender.parseInt(options[1], 100));
    }
    
    private MaxLengthConverter(final List<PatternFormatter> formatters, final int maxLength) {
        super("MaxLength", "maxLength");
        this.maxLength = maxLength;
        this.formatters = formatters;
        MaxLengthConverter.LOGGER.trace("new MaxLengthConverter with {}", (Object)maxLength);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final int initialLength = toAppendTo.length();
        for (int i = 0; i < this.formatters.size(); ++i) {
            final PatternFormatter formatter = this.formatters.get(i);
            formatter.format(event, toAppendTo);
            if (toAppendTo.length() > initialLength + this.maxLength) {
                break;
            }
        }
        if (toAppendTo.length() > initialLength + this.maxLength) {
            toAppendTo.setLength(initialLength + this.maxLength);
            if (this.maxLength > 20) {
                toAppendTo.append("...");
            }
        }
    }
}
