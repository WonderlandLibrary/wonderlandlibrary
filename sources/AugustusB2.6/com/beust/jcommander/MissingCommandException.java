// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

public class MissingCommandException extends ParameterException
{
    private final String unknownCommand;
    
    public MissingCommandException(final String s) {
        this(s, (String)null);
    }
    
    public MissingCommandException(final String s, final String unknownCommand) {
        super(s);
        this.unknownCommand = unknownCommand;
    }
    
    public String getUnknownCommand() {
        return this.unknownCommand;
    }
}
