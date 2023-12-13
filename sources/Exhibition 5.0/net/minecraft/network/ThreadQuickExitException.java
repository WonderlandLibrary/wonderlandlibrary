// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network;

public final class ThreadQuickExitException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    public static final ThreadQuickExitException field_179886_a;
    private static final String __OBFID = "CL_00002274";
    
    private ThreadQuickExitException() {
        this.setStackTrace(new StackTraceElement[0]);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        this.setStackTrace(new StackTraceElement[0]);
        return this;
    }
    
    static {
        field_179886_a = new ThreadQuickExitException();
    }
}
