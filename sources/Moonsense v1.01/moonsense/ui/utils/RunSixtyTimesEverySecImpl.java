// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.concurrent.Executors;
import com.google.common.collect.Lists;
import java.util.List;

public class RunSixtyTimesEverySecImpl
{
    public static final List<RunSixtyTimesEverySec> TICKS_LIST;
    
    static {
        TICKS_LIST = Lists.newCopyOnWriteArrayList();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            RunSixtyTimesEverySecImpl.TICKS_LIST.removeIf(Objects::isNull);
            RunSixtyTimesEverySecImpl.TICKS_LIST.iterator().forEachRemaining(Runnable::run);
        }, 0L, 16L, TimeUnit.MILLISECONDS);
    }
}
