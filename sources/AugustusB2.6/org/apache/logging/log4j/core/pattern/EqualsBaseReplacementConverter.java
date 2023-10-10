// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import java.util.List;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public abstract class EqualsBaseReplacementConverter extends LogEventPatternConverter
{
    private final List<PatternFormatter> formatters;
    private final List<PatternFormatter> substitutionFormatters;
    private final String substitution;
    private final String testString;
    
    protected EqualsBaseReplacementConverter(final String name, final String style, final List<PatternFormatter> formatters, final String testString, final String substitution, final PatternParser parser) {
        super(name, style);
        this.testString = testString;
        this.substitution = substitution;
        this.formatters = formatters;
        this.substitutionFormatters = (substitution.contains("%") ? parser.parse(substitution) : null);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final int initialSize = toAppendTo.length();
        for (int i = 0; i < this.formatters.size(); ++i) {
            final PatternFormatter formatter = this.formatters.get(i);
            formatter.format(event, toAppendTo);
        }
        if (this.equals(this.testString, toAppendTo, initialSize, toAppendTo.length() - initialSize)) {
            toAppendTo.setLength(initialSize);
            this.parseSubstitution(event, toAppendTo);
        }
    }
    
    protected abstract boolean equals(final String str, final StringBuilder buff, final int from, final int len);
    
    void parseSubstitution(final LogEvent event, final StringBuilder substitutionBuffer) {
        if (this.substitutionFormatters != null) {
            for (int i = 0; i < this.substitutionFormatters.size(); ++i) {
                final PatternFormatter formatter = this.substitutionFormatters.get(i);
                formatter.format(event, substitutionBuffer);
            }
        }
        else {
            substitutionBuffer.append(this.substitution);
        }
    }
}
