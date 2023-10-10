// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

public class ParameterException extends RuntimeException
{
    private JCommander jc;
    
    public ParameterException(final Throwable cause) {
        super(cause);
    }
    
    public ParameterException(final String message) {
        super(message);
    }
    
    public ParameterException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public void setJCommander(final JCommander jc) {
        this.jc = jc;
    }
    
    public JCommander getJCommander() {
        return this.jc;
    }
    
    public void usage() {
        if (this.jc != null) {
            this.jc.usage();
        }
    }
}
