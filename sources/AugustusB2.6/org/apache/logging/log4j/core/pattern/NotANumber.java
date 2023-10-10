// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

public final class NotANumber
{
    public static final NotANumber NAN;
    public static final String VALUE = "\u0000";
    
    private NotANumber() {
    }
    
    @Override
    public String toString() {
        return "\u0000";
    }
    
    static {
        NAN = new NotANumber();
    }
}
