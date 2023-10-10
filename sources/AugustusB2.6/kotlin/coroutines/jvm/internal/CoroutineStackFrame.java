// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

public interface CoroutineStackFrame
{
    CoroutineStackFrame getCallerFrame();
    
    StackTraceElement getStackTraceElement();
}
