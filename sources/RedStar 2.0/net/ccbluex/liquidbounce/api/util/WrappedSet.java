package net.ccbluex.liquidbounce.api.util;

import java.util.Collection;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\b\n\n\n\"\n\b\n\n\b\u0000*\b\u0000*\b2HH\n\bH002\bH0B;\f\b8\u0000088\u00000\b\t8\u000080\b¢\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedSet;", "O", "T", "Lnet/ccbluex/liquidbounce/api/util/WrappedCollection;", "", "", "wrapped", "unwrapper", "Lkotlin/Function1;", "wrapper", "(Ljava/util/Set;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "Pride"})
public final class WrappedSet<O, T>
extends WrappedCollection<O, T, Collection<? extends O>>
implements Set<T>,
KMappedMarker {
    public WrappedSet(@NotNull Set<? extends O> wrapped, @NotNull Function1<? super T, ? extends O> unwrapper, @NotNull Function1<? super O, ? extends T> wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
        Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
        Intrinsics.checkParameterIsNotNull(wrapper, "wrapper");
        super((Collection)wrapped, unwrapper, wrapper);
    }
}
