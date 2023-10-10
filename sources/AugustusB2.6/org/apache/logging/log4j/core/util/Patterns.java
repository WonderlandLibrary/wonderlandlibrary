// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

public final class Patterns
{
    public static final String COMMA_SEPARATOR;
    public static final String WHITESPACE = "\\s*";
    
    private Patterns() {
    }
    
    public static String toWhitespaceSeparator(final String separator) {
        return "\\s*" + separator + "\\s*";
    }
    
    static {
        COMMA_SEPARATOR = toWhitespaceSeparator(",");
    }
}
