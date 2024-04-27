/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.util.IWrappedArray;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\b\u0012\u0004\u0012\u0002H\u00020\u0003B;\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00000\u0007\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\u000f\u001a\u00028\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0096\u0002\u00a2\u0006\u0002\u0010\u0012J\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00028\u00010\u0014H\u0096\u0002J\u001e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00028\u0001H\u0096\u0002\u00a2\u0006\u0002\u0010\u0018R\u001d\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00000\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001d\u0010\b\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000b\u00a8\u0006\u0019"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedListArrayAdapter;", "O", "T", "Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "wrapped", "", "unwrapper", "Lkotlin/Function1;", "wrapper", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getUnwrapper", "()Lkotlin/jvm/functions/Function1;", "getWrapped", "()Ljava/util/List;", "getWrapper", "get", "index", "", "(I)Ljava/lang/Object;", "iterator", "", "set", "", "value", "(ILjava/lang/Object;)V", "LiKingSense"})
public final class WrappedListArrayAdapter<O, T>
implements IWrappedArray<T> {
    @NotNull
    private final List<O> wrapped;
    @NotNull
    private final Function1<T, O> unwrapper;
    @NotNull
    private final Function1<O, T> wrapper;

    @Override
    public T get(int index) {
        return (T)this.wrapper.invoke(this.wrapped.get(index));
    }

    @Override
    public void set(int index, T value) {
        this.wrapped.set(index, this.unwrapper.invoke(value));
    }

    @Override
    @NotNull
    public Iterator<T> iterator() {
        return new WrappedCollection.WrappedCollectionIterator<O, T>(this.wrapped.iterator(), this.wrapper);
    }

    @NotNull
    public final List<O> getWrapped() {
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

    public WrappedListArrayAdapter(@NotNull List<O> wrapped, @NotNull Function1<? super T, ? extends O> unwrapper, @NotNull Function1<? super O, ? extends T> wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        Intrinsics.checkParameterIsNotNull(unwrapper, (String)"unwrapper");
        Intrinsics.checkParameterIsNotNull(wrapper, (String)"wrapper");
        this.wrapped = wrapped;
        this.unwrapper = unwrapper;
        this.wrapper = wrapper;
    }
}

