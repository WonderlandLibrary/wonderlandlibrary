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

@Plugin(name = "equalsIgnoreCase", category = "Converter")
@ConverterKeys({ "equalsIgnoreCase" })
@PerformanceSensitive({ "allocation" })
public final class EqualsIgnoreCaseReplacementConverter extends EqualsBaseReplacementConverter
{
    public static EqualsIgnoreCaseReplacementConverter newInstance(final Configuration config, final String[] options) {
        if (options.length != 3) {
            EqualsIgnoreCaseReplacementConverter.LOGGER.error("Incorrect number of options on equalsIgnoreCase. Expected 3 received " + options.length);
            return null;
        }
        if (options[0] == null) {
            EqualsIgnoreCaseReplacementConverter.LOGGER.error("No pattern supplied on equalsIgnoreCase");
            return null;
        }
        if (options[1] == null) {
            EqualsIgnoreCaseReplacementConverter.LOGGER.error("No test string supplied on equalsIgnoreCase");
            return null;
        }
        if (options[2] == null) {
            EqualsIgnoreCaseReplacementConverter.LOGGER.error("No substitution supplied on equalsIgnoreCase");
            return null;
        }
        final String p = options[1];
        final PatternParser parser = PatternLayout.createPatternParser(config);
        final List<PatternFormatter> formatters = parser.parse(options[0]);
        return new EqualsIgnoreCaseReplacementConverter(formatters, p, options[2], parser);
    }
    
    private EqualsIgnoreCaseReplacementConverter(final List<PatternFormatter> formatters, final String testString, final String substitution, final PatternParser parser) {
        super("equalsIgnoreCase", "equalsIgnoreCase", formatters, testString, substitution, parser);
    }
    
    @Override
    protected boolean equals(final String str, final StringBuilder buff, final int from, final int len) {
        return StringBuilders.equalsIgnoreCase(str, 0, str.length(), buff, from, len);
    }
}
