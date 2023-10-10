// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

public interface TimeSource
{
    long nanoTime();
    
    Runnable wrapTask$1daae6bf();
}
