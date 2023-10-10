// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

public final class NonBlockingContext implements TaskContext
{
    private static final TaskMode taskMode;
    public static final NonBlockingContext INSTANCE;
    
    @Override
    public final TaskMode getTaskMode() {
        return NonBlockingContext.taskMode;
    }
    
    @Override
    public final void afterTask() {
    }
    
    private NonBlockingContext() {
    }
    
    static {
        INSTANCE = new NonBlockingContext();
        taskMode = TaskMode.NON_BLOCKING;
    }
}
