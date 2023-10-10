// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

public interface ChildJob extends Job
{
    void parentCancelled(final ParentJob p0);
}
