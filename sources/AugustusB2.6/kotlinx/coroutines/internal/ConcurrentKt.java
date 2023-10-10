// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import kotlin.jvm.internal.Intrinsics;
import java.util.Map;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class ConcurrentKt
{
    public static final <E> Set<E> identitySet(final int expectedSize) {
        final Set<E> setFromMap = Collections.newSetFromMap((Map<E, Boolean>)new IdentityHashMap<Object, Boolean>(expectedSize));
        Intrinsics.checkExpressionValueIsNotNull(setFromMap, "Collections.newSetFromMa\u2026ityHashMap(expectedSize))");
        return setFromMap;
    }
    
    static {
        try {
            ScheduledThreadPoolExecutor.class.getMethod("setRemoveOnCancelPolicy", Boolean.TYPE);
        }
        catch (Throwable t) {}
    }
}
