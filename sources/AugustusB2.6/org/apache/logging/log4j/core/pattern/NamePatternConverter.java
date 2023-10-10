// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public abstract class NamePatternConverter extends LogEventPatternConverter
{
    private final NameAbbreviator abbreviator;
    
    protected NamePatternConverter(final String name, final String style, final String[] options) {
        super(name, style);
        if (options != null && options.length > 0) {
            this.abbreviator = NameAbbreviator.getAbbreviator(options[0]);
        }
        else {
            this.abbreviator = NameAbbreviator.getDefaultAbbreviator();
        }
    }
    
    protected final void abbreviate(final String original, final StringBuilder destination) {
        this.abbreviator.abbreviate(original, destination);
    }
}
