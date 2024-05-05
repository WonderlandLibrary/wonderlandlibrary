/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.ArrayIteratorKt
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ArrayIteratorKt;
import net.ccbluex.liquidbounce.api.util.IWrappedArray;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;

public final class WrappedArray<O, T>
implements IWrappedArray<T> {
    private final O[] wrapped;
    private final Function1<T, O> unwrapper;
    private final Function1<O, T> wrapper;

    @Override
    public T get(int index) {
        return (T)this.wrapper.invoke(this.wrapped[index]);
    }

    @Override
    public void set(int index, T value) {
        this.wrapped[index] = this.unwrapper.invoke(value);
    }

    @Override
    public Iterator<T> iterator() {
        return new WrappedCollection.WrappedCollectionIterator<O, T>(ArrayIteratorKt.iterator((Object[])this.wrapped), this.wrapper);
    }

    public final O[] getWrapped() {
        return this.wrapped;
    }

    public final Function1<T, O> getUnwrapper() {
        return this.unwrapper;
    }

    public final Function1<O, T> getWrapper() {
        return this.wrapper;
    }

    public WrappedArray(O[] wrapped, Function1<? super T, ? extends O> unwrapper, Function1<? super O, ? extends T> wrapper) {
        this.wrapped = wrapped;
        this.unwrapper = unwrapper;
        this.wrapper = wrapper;
    }
}

