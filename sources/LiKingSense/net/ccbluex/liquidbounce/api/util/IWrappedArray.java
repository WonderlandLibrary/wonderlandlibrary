/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function2
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.markers.KMappedMarker
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api.util;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\bf\u0018\u0000 \u000b*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001\u000bJ\u0016\u0010\u0003\u001a\u00028\u00002\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6\u0002\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00028\u0000H\u00a6\u0002\u00a2\u0006\u0002\u0010\n\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "T", "", "get", "index", "", "(I)Ljava/lang/Object;", "set", "", "value", "(ILjava/lang/Object;)V", "Companion", "LiKingSense"})
public interface IWrappedArray<T>
extends Iterable<T>,
KMappedMarker {
    public static final Companion Companion = net.ccbluex.liquidbounce.api.util.IWrappedArray$Companion.$$INSTANCE;

    public T get(int var1);

    public void set(int var1, T var2);

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002JD\u0010\u0003\u001a\u00020\u0004\"\u0004\b\u0001\u0010\u0005*\n\u0012\u0006\b\u0001\u0012\u0002H\u00050\u00062'\u0010\u0007\u001a#\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u0005\u0012\u0004\u0012\u00020\u00040\bH\u0086\b\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/api/util/IWrappedArray$Companion;", "", "()V", "forEachIndexed", "", "T", "Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "action", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "index", "LiKingSense"})
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE;

        public final <T> void forEachIndexed(@NotNull IWrappedArray<? extends T> $this$forEachIndexed, @NotNull Function2<? super Integer, ? super T, Unit> action) {
            int $i$f$forEachIndexed = 0;
            Intrinsics.checkParameterIsNotNull($this$forEachIndexed, (String)"$this$forEachIndexed");
            Intrinsics.checkParameterIsNotNull(action, (String)"action");
            int index = 0;
            for (Object item : $this$forEachIndexed) {
                Integer n = index;
                ++index;
                action.invoke((Object)n, item);
            }
        }

        private Companion() {
        }

        static {
            Companion companion;
            $$INSTANCE = companion = new Companion();
        }
    }
}

