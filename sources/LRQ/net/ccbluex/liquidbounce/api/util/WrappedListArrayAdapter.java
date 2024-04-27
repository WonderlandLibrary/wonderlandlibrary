/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.Iterator;
import java.util.List;
import kotlin.jvm.functions.Function1;
import net.ccbluex.liquidbounce.api.util.IWrappedArray;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;

public final class WrappedListArrayAdapter<O, T>
implements IWrappedArray<T> {
    private final List<O> wrapped;
    private final Function1<T, O> unwrapper;
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
    public Iterator<T> iterator() {
        return new WrappedCollection.WrappedCollectionIterator<O, T>(this.wrapped.iterator(), this.wrapper);
    }

    public final List<O> getWrapped() {
        return this.wrapped;
    }

    public final Function1<T, O> getUnwrapper() {
        return this.unwrapper;
    }

    public final Function1<O, T> getWrapper() {
        return this.wrapper;
    }

    public WrappedListArrayAdapter(List<O> wrapped, Function1<? super T, ? extends O> unwrapper, Function1<? super O, ? extends T> wrapper) {
        this.wrapped = wrapped;
        this.unwrapper = unwrapper;
        this.wrapper = wrapper;
    }
}

