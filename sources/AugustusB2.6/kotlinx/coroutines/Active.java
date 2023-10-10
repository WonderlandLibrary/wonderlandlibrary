// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

final class Active implements NotCompleted
{
    public static final Active INSTANCE;
    
    @Override
    public final String toString() {
        return "Active";
    }
    
    private Active() {
    }
    
    static {
        INSTANCE = new Active();
    }
}
