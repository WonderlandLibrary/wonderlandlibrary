// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.util.OptionConverter;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public final class LiteralPatternConverter extends LogEventPatternConverter implements ArrayPatternConverter
{
    private final String literal;
    private final Configuration config;
    private final boolean substitute;
    
    public LiteralPatternConverter(final Configuration config, final String literal, final boolean convertBackslashes) {
        super("Literal", "literal");
        this.literal = (convertBackslashes ? OptionConverter.convertSpecialChars(literal) : literal);
        this.config = config;
        this.substitute = (config != null && containsSubstitutionSequence(literal));
    }
    
    static boolean containsSubstitutionSequence(final String literal) {
        return literal != null && literal.contains("${");
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(this.substitute ? this.config.getStrSubstitutor().replace(event, this.literal) : this.literal);
    }
    
    @Override
    public void format(final Object obj, final StringBuilder output) {
        output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
    }
    
    @Override
    public void format(final StringBuilder output, final Object... objects) {
        output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
    }
    
    public String getLiteral() {
        return this.literal;
    }
    
    @Override
    public boolean isVariable() {
        return false;
    }
    
    @Override
    public String toString() {
        return "LiteralPatternConverter[literal=" + this.literal + ", config=" + this.config + ", substitute=" + this.substitute + "]";
    }
}
