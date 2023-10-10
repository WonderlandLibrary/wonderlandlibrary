// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.util.StringBuilders;
import java.util.List;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "equals", category = "Converter")
@ConverterKeys({ "equals" })
@PerformanceSensitive({ "allocation" })
public final class EqualsReplacementConverter extends EqualsBaseReplacementConverter
{
    public static EqualsReplacementConverter newInstance(final Configuration config, final String[] options) {
        if (options.length != 3) {
            EqualsReplacementConverter.LOGGER.error("Incorrect number of options on equals. Expected 3 received " + options.length);
            return null;
        }
        if (options[0] == null) {
            EqualsReplacementConverter.LOGGER.error("No pattern supplied on equals");
            return null;
        }
        if (options[1] == null) {
            EqualsReplacementConverter.LOGGER.error("No test string supplied on equals");
            return null;
        }
        if (options[2] == null) {
            EqualsReplacementConverter.LOGGER.error("No substitution supplied on equals");
            return null;
        }
        final String p = options[1];
        final PatternParser parser = PatternLayout.createPatternParser(config);
        final List<PatternFormatter> formatters = parser.parse(options[0]);
        return new EqualsReplacementConverter(formatters, p, options[2], parser);
    }
    
    private EqualsReplacementConverter(final List<PatternFormatter> formatters, final String testString, final String substitution, final PatternParser parser) {
        super("equals", "equals", formatters, testString, substitution, parser);
    }
    
    @Override
    protected boolean equals(final String str, final StringBuilder buff, final int from, final int len) {
        return StringBuilders.equals(str, 0, str.length(), buff, from, len);
    }
}
