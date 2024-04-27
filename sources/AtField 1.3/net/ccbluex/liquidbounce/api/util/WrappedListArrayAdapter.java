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

public final class WrappedListArrayAdapter
implements IWrappedArray {
    private final Function1 unwrapper;
    private final List wrapped;
    private final Function1 wrapper;

    public final List getWrapped() {
        return this.wrapped;
    }

    public final Function1 getUnwrapper() {
        return this.unwrapper;
    }

    public final Function1 getWrapper() {
        return this.wrapper;
    }

    public WrappedListArrayAdapter(List list, Function1 function1, Function1 function12) {
        this.wrapped = list;
        this.unwrapper = function1;
        this.wrapper = function12;
    }

    @Override
    public void set(int n, Object object) {
        this.wrapped.set(n, this.unwrapper.invoke(object));
    }

    @Override
    public Object get(int n) {
        return this.wrapper.invoke(this.wrapped.get(n));
    }

    public Iterator iterator() {
        return new WrappedCollection.WrappedCollectionIterator(this.wrapped.iterator(), this.wrapper);
    }
}

