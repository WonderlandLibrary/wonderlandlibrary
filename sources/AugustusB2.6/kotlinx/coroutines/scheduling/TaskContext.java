// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

public interface TaskContext
{
    TaskMode getTaskMode();
    
    void afterTask();
}
