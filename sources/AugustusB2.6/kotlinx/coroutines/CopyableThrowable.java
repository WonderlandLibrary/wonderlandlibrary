// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

public interface CopyableThrowable<T extends Throwable>
{
    T createCopy();
}
