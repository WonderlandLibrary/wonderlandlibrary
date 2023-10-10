// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import java.util.LinkedHashMap;
import kotlin.jvm.internal.Intrinsics;
import java.util.Map;
import kotlin.Pair;

class MapsKt__MapsKt extends ArraysKt__ArraysJVMKt
{
    public static final <K, V> Map<K, V> mapOf(Pair<? extends K, ? extends V>... pairs) {
        Intrinsics.checkParameterIsNotNull(pairs, "pairs");
        final int n;
        int initialCapacity;
        if ((n = 7) < 3) {
            initialCapacity = n + 1;
        }
        else if (n < 1073741824) {
            final int n2 = n;
            initialCapacity = n2 + n2 / 3;
        }
        else {
            initialCapacity = Integer.MAX_VALUE;
        }
        final LinkedHashMap<Object, Object> value = new LinkedHashMap<Object, Object>(initialCapacity);
        pairs = (Pair<? extends K, ? extends V>[])pairs;
        Intrinsics.checkParameterIsNotNull(pairs, "$this$toMap");
        Intrinsics.checkParameterIsNotNull(value, "destination");
        final Map<K, V> map;
        putAll((Map<? super Object, ? super Object>)(map = (Map<K, V>)value), (Pair<?, ?>[])pairs);
        return map;
    }
    
    public static final <K, V> void putAll(final Map<? super K, ? super V> $this$putAll, final Pair<? extends K, ? extends V>[] pairs) {
        Intrinsics.checkParameterIsNotNull($this$putAll, "$this$putAll");
        Intrinsics.checkParameterIsNotNull(pairs, "pairs");
        for (int length = pairs.length, i = 0; i < length; ++i) {
            final Pair<? extends K, ? extends V> pair;
            final Object key = (pair = pairs[i]).component1();
            final Object value = pair.component2();
            $this$putAll.put((Object)key, (Object)value);
        }
    }
}
