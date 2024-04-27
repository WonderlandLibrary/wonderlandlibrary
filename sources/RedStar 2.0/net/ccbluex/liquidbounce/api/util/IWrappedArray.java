package net.ccbluex.liquidbounce.api.util;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\n\b\n\b\n\b\n\n\b\bf\u0000 *\b\u00002\bH0:J8\u000020H¦¢J0\b202\t8\u0000H¦¢\n¨\f"}, d2={"Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "T", "", "get", "index", "", "(I)Ljava/lang/Object;", "set", "", "value", "(ILjava/lang/Object;)V", "Companion", "Pride"})
public interface IWrappedArray<T>
extends Iterable<T>,
KMappedMarker {
    public static final Companion Companion = net.ccbluex.liquidbounce.api.util.IWrappedArray$Companion.$$INSTANCE;

    public T get(int var1);

    public void set(int var1, T var2);

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\u0000\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\b\u000020B\b¢JD0\"\b*\n\bH02'#0\t¢\f\b\n\b\b\b\b(\fH00\bH\b¨\r"}, d2={"Lnet/ccbluex/liquidbounce/api/util/IWrappedArray$Companion;", "", "()V", "forEachIndexed", "", "T", "Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "action", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "index", "Pride"})
    public static final class Companion {
        static final Companion $$INSTANCE;

        public final <T> void forEachIndexed(@NotNull IWrappedArray<? extends T> $this$forEachIndexed, @NotNull Function2<? super Integer, ? super T, Unit> action) {
            int $i$f$forEachIndexed = 0;
            Intrinsics.checkParameterIsNotNull($this$forEachIndexed, "$this$forEachIndexed");
            Intrinsics.checkParameterIsNotNull(action, "action");
            int index = 0;
            for (Object item : $this$forEachIndexed) {
                Integer n = index;
                ++index;
                action.invoke(n, item);
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
