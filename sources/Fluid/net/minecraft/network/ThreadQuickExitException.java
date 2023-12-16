// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network;

public final class ThreadQuickExitException extends RuntimeException
{
    public static final ThreadQuickExitException INSTANCE;
    
    static {
        INSTANCE = new ThreadQuickExitException();
    }
    
    private ThreadQuickExitException() {
        this.setStackTrace(new StackTraceElement[0]);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        this.setStackTrace(new StackTraceElement[0]);
        return this;
    }
}
