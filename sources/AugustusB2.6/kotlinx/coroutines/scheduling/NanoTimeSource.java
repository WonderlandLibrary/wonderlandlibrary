// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

public final class NanoTimeSource extends TimeSource
{
    public static final NanoTimeSource INSTANCE;
    
    @Override
    public final long nanoTime() {
        return System.nanoTime();
    }
    
    private NanoTimeSource() {
    }
    
    static {
        INSTANCE = new NanoTimeSource();
    }
}
