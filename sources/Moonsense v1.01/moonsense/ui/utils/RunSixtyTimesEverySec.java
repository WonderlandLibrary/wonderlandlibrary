// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.utils;

public interface RunSixtyTimesEverySec extends Runnable
{
    default boolean isRegistered() {
        return RunSixtyTimesEverySecImpl.TICKS_LIST.contains(this);
    }
    
    default void registerTick() {
        RunSixtyTimesEverySecImpl.TICKS_LIST.removeIf(runSixtyTimesEverySec -> runSixtyTimesEverySec == this);
        RunSixtyTimesEverySecImpl.TICKS_LIST.add(this);
    }
    
    default void unregisterTick() {
        RunSixtyTimesEverySecImpl.TICKS_LIST.remove(this);
    }
}
