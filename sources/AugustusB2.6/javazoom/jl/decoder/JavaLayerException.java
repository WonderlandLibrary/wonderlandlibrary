// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

import java.io.PrintStream;

public class JavaLayerException extends Exception
{
    private Throwable exception;
    
    public JavaLayerException() {
    }
    
    public JavaLayerException(final String msg) {
        super(msg);
    }
    
    public JavaLayerException(final String msg, final Throwable t) {
        super(msg);
        this.exception = t;
    }
    
    public Throwable getException() {
        return this.exception;
    }
    
    @Override
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }
    
    @Override
    public void printStackTrace(final PrintStream ps) {
        if (this.exception == null) {
            super.printStackTrace(ps);
        }
        else {
            this.exception.printStackTrace();
        }
    }
}
