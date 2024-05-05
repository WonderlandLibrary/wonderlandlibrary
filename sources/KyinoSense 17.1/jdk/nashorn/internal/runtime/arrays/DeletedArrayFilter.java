/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.BitVector;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ArrayFilter;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.runtime.arrays.SparseArrayData;

final class DeletedArrayFilter
extends ArrayFilter {
    private final BitVector deleted;

    DeletedArrayFilter(ArrayData underlying) {
        super(underlying);
        this.deleted = new BitVector(underlying.length());
    }

    @Override
    public ArrayData copy() {
        DeletedArrayFilter copy = new DeletedArrayFilter(this.underlying.copy());
        copy.getDeleted().copy(this.deleted);
        return copy;
    }

    @Override
    public Object[] asObjectArray() {
        Object[] value = super.asObjectArray();
        for (int i = 0; i < value.length; ++i) {
            if (!this.deleted.isSet(i)) continue;
            value[i] = ScriptRuntime.UNDEFINED;
        }
        return value;
    }

    @Override
    public Object asArrayOfType(Class<?> componentType) {
        Object value = super.asArrayOfType(componentType);
        Object undefValue = DeletedArrayFilter.convertUndefinedValue(componentType);
        int l = Array.getLength(value);
        for (int i = 0; i < l; ++i) {
            if (!this.deleted.isSet(i)) continue;
            Array.set(value, i, undefValue);
        }
        return value;
    }

    @Override
    public ArrayData shiftLeft(int by) {
        super.shiftLeft(by);
        this.deleted.shiftLeft(by, this.length());
        return this;
    }

    @Override
    public ArrayData shiftRight(int by) {
        super.shiftRight(by);
        this.deleted.shiftRight(by, this.length());
        return this;
    }

    @Override
    public ArrayData ensure(long safeIndex) {
        if (safeIndex >= 131072L && safeIndex >= this.length()) {
            return new SparseArrayData(this, safeIndex + 1L);
        }
        super.ensure(safeIndex);
        this.deleted.resize(this.length());
        return this;
    }

    @Override
    public ArrayData shrink(long newLength) {
        super.shrink(newLength);
        this.deleted.resize(this.length());
        return this;
    }

    @Override
    public ArrayData set(int index, Object value, boolean strict) {
        this.deleted.clear(ArrayIndex.toLongIndex(index));
        return super.set(index, value, strict);
    }

    @Override
    public ArrayData set(int index, int value, boolean strict) {
        this.deleted.clear(ArrayIndex.toLongIndex(index));
        return super.set(index, value, strict);
    }

    @Override
    public ArrayData set(int index, double value, boolean strict) {
        this.deleted.clear(ArrayIndex.toLongIndex(index));
        return super.set(index, value, strict);
    }

    @Override
    public boolean has(int index) {
        return super.has(index) && this.deleted.isClear(ArrayIndex.toLongIndex(index));
    }

    @Override
    public ArrayData delete(int index) {
        long longIndex = ArrayIndex.toLongIndex(index);
        assert (longIndex >= 0L && longIndex < this.length());
        this.deleted.set(longIndex);
        this.underlying.setEmpty(index);
        return this;
    }

    @Override
    public ArrayData delete(long fromIndex, long toIndex) {
        assert (fromIndex >= 0L && fromIndex <= toIndex && toIndex < this.length());
        this.deleted.setRange(fromIndex, toIndex + 1L);
        this.underlying.setEmpty(fromIndex, toIndex);
        return this;
    }

    @Override
    public Object pop() {
        long index = this.length() - 1L;
        if (super.has((int)index)) {
            boolean isDeleted = this.deleted.isSet(index);
            Object value = super.pop();
            return isDeleted ? ScriptRuntime.UNDEFINED : value;
        }
        return super.pop();
    }

    @Override
    public ArrayData slice(long from, long to) {
        ArrayData newArray = this.underlying.slice(from, to);
        DeletedArrayFilter newFilter = new DeletedArrayFilter(newArray);
        newFilter.getDeleted().copy(this.deleted);
        newFilter.getDeleted().shiftLeft(from, newFilter.length());
        return newFilter;
    }

    private BitVector getDeleted() {
        return this.deleted;
    }
}

