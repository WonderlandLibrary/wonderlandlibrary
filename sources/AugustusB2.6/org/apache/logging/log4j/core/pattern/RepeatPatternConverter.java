// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "repeat", category = "Converter")
@ConverterKeys({ ":|", "repeat" })
@PerformanceSensitive({ "allocation" })
public final class RepeatPatternConverter extends LogEventPatternConverter
{
    private final String result;
    
    public static RepeatPatternConverter newInstance(final Configuration config, final String[] options) {
        if (options.length != 2) {
            RepeatPatternConverter.LOGGER.error("Incorrect number of options on repeat. Expected 2 received " + options.length);
            return null;
        }
        if (options[0] == null) {
            RepeatPatternConverter.LOGGER.error("No string supplied on repeat");
            return null;
        }
        if (options[1] == null) {
            RepeatPatternConverter.LOGGER.error("No repeat count supplied on repeat");
            return null;
        }
        int count = 0;
        String result = options[0];
        try {
            count = Integer.parseInt(options[1].trim());
            result = Strings.repeat(options[0], count);
        }
        catch (Exception ex) {
            RepeatPatternConverter.LOGGER.error("The repeat count is not an integer: {}", options[1].trim());
        }
        return new RepeatPatternConverter(result);
    }
    
    private RepeatPatternConverter(final String result) {
        super("repeat", "repeat");
        this.result = result;
    }
    
    @Override
    public void format(final Object obj, final StringBuilder toAppendTo) {
        this.format(toAppendTo);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        this.format(toAppendTo);
    }
    
    private void format(final StringBuilder toAppendTo) {
        if (this.result != null) {
            toAppendTo.append(this.result);
        }
    }
}
