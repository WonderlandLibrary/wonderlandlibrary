// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

public final class ThreadQuickExitException extends RuntimeException
{
    public static final ThreadQuickExitException field_179886_a;
    private static final String __OBFID = "CL_00002274";
    
    static {
        field_179886_a = new ThreadQuickExitException();
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
