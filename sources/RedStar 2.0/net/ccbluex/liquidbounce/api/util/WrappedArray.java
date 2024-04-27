package net.ccbluex.liquidbounce.api.util;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.util.IWrappedArray;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\b\n(\n\u0000\n\n\b\u0000*\b\u0000*\b2\bH0B;\f\b8\u0000088\u00000\b8\u000080¢\tJ820H¢J\b80HJ02028H¢R88\u00000¢\b\n\u0000\b\nR\b8\u00000¢\n\n\b\f\rR\b8\u000080¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedArray;", "O", "T", "Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "wrapped", "", "unwrapper", "Lkotlin/Function1;", "wrapper", "([Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getUnwrapper", "()Lkotlin/jvm/functions/Function1;", "getWrapped", "()[Ljava/lang/Object;", "[Ljava/lang/Object;", "getWrapper", "get", "index", "", "(I)Ljava/lang/Object;", "iterator", "", "set", "", "value", "(ILjava/lang/Object;)V", "Pride"})
public final class WrappedArray<O, T>
implements IWrappedArray<T> {
    @NotNull
    private final O[] wrapped;
    @NotNull
    private final Function1<T, O> unwrapper;
    @NotNull
    private final Function1<O, T> wrapper;

    @Override
    public T get(int index) {
        return this.wrapper.invoke(this.wrapped[index]);
    }

    @Override
    public void set(int index, T value) {
        this.wrapped[index] = this.unwrapper.invoke(value);
    }

    @Override
    @NotNull
    public Iterator<T> iterator() {
        return new WrappedCollection.WrappedCollectionIterator<O, T>(ArrayIteratorKt.iterator(this.wrapped), this.wrapper);
    }

    @NotNull
    public final O[] getWrapped() {
        return this.wrapped;
    }

    @NotNull
    public final Function1<T, O> getUnwrapper() {
        return this.unwrapper;
    }

    @NotNull
    public final Function1<O, T> getWrapper() {
        return this.wrapper;
    }

    public WrappedArray(@NotNull O[] wrapped, @NotNull Function1<? super T, ? extends O> unwrapper, @NotNull Function1<? super O, ? extends T> wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
        Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
        Intrinsics.checkParameterIsNotNull(wrapper, "wrapper");
        this.wrapped = wrapped;
        this.unwrapper = unwrapper;
        this.wrapper = wrapper;
    }
}
