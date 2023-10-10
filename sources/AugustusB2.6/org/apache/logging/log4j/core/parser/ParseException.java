// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.parser;

public class ParseException extends Exception
{
    private static final long serialVersionUID = -2739649998196663857L;
    
    public ParseException(final String message) {
        super(message);
    }
    
    public ParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ParseException(final Throwable cause) {
        super(cause);
    }
}
