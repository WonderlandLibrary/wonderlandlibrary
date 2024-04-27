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

public final class WrappedArray
implements IWrappedArray {
    private final Object[] wrapped;
    private final Function1 wrapper;
    private final Function1 unwrapper;

    public Iterator iterator() {
        return new WrappedCollection.WrappedCollectionIterator(ArrayIteratorKt.iterator((Object[])this.wrapped), this.wrapper);
    }

    public final Function1 getWrapper() {
        return this.wrapper;
    }

    public final Function1 getUnwrapper() {
        return this.unwrapper;
    }

    @Override
    public Object get(int n) {
        return this.wrapper.invoke(this.wrapped[n]);
    }

    @Override
    public void set(int n, Object object) {
        this.wrapped[n] = this.unwrapper.invoke(object);
    }

    public final Object[] getWrapped() {
        return this.wrapped;
    }

    public WrappedArray(Object[] objectArray, Function1 function1, Function1 function12) {
        this.wrapped = objectArray;
        this.unwrapper = function1;
        this.wrapper = function12;
    }
}

