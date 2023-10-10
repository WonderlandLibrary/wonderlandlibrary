// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

public interface ThreadSafeHeapNode
{
    ThreadSafeHeap<?> getHeap();
    
    void setHeap(final ThreadSafeHeap<?> p0);
    
    void setIndex(final int p0);
}
