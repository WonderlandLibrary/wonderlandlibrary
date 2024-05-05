/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ArrayFilter;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;

class SealedArrayFilter
extends ArrayFilter {
    SealedArrayFilter(ArrayData underlying) {
        super(underlying);
    }

    @Override
    public ArrayData copy() {
        return new SealedArrayFilter(this.underlying.copy());
    }

    @Override
    public ArrayData slice(long from, long to) {
        return this.getUnderlying().slice(from, to);
    }

    @Override
    public boolean canDelete(int index, boolean strict) {
        return this.canDelete(ArrayIndex.toLongIndex(index), strict);
    }

    @Override
    public boolean canDelete(long longIndex, boolean strict) {
        if (strict) {
            throw ECMAErrors.typeError("cant.delete.property", Long.toString(longIndex), "sealed array");
        }
        return false;
    }

    @Override
    public PropertyDescriptor getDescriptor(Global global, int index) {
        return global.newDataDescriptor(this.getObject(index), false, true, true);
    }
}

