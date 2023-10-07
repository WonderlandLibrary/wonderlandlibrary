/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.BitVector;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ArrayFilter;
import jdk.nashorn.internal.runtime.arrays.SparseArrayData;

final class UndefinedArrayFilter
extends ArrayFilter {
    private final BitVector undefined;

    UndefinedArrayFilter(ArrayData underlying) {
        super(underlying);
        this.undefined = new BitVector(underlying.length());
    }

    @Override
    public ArrayData copy() {
        UndefinedArrayFilter copy = new UndefinedArrayFilter(this.underlying.copy());
        copy.getUndefined().copy(this.undefined);
        return copy;
    }

    @Override
    public Object[] asObjectArray() {
        Object[] value = super.asObjectArray();
        for (int i = 0; i < value.length; ++i) {
            if (!this.undefined.isSet(i)) continue;
            value[i] = ScriptRuntime.UNDEFINED;
        }
        return value;
    }

    @Override
    public Object asArrayOfType(Class<?> componentType) {
        Object value = super.asArrayOfType(componentType);
        Object undefValue = UndefinedArrayFilter.convertUndefinedValue(componentType);
        int l = Array.getLength(value);
        for (int i = 0; i < l; ++i) {
            if (!this.undefined.isSet(i)) continue;
            Array.set(value, i, undefValue);
        }
        return value;
    }

    @Override
    public ArrayData shiftLeft(int by) {
        super.shiftLeft(by);
        this.undefined.shiftLeft(by, this.length());
        return this;
    }

    @Override
    public ArrayData shiftRight(int by) {
        super.shiftRight(by);
        this.undefined.shiftRight(by, this.length());
        return this;
    }

    @Override
    public ArrayData ensure(long safeIndex) {
        if (safeIndex >= 131072L && safeIndex >= this.length()) {
            return new SparseArrayData(this, safeIndex + 1L);
        }
        super.ensure(safeIndex);
        this.undefined.resize(this.length());
        return this;
    }

    @Override
    public ArrayData shrink(long newLength) {
        super.shrink(newLength);
        this.undefined.resize(this.length());
        return this;
    }

    @Override
    public ArrayData set(int index, Object value, boolean strict) {
        this.undefined.clear(index);
        if (value == ScriptRuntime.UNDEFINED) {
            this.undefined.set(index);
            return this;
        }
        return super.set(index, value, strict);
    }

    @Override
    public ArrayData set(int index, int value, boolean strict) {
        this.undefined.clear(index);
        return super.set(index, value, strict);
    }

    @Override
    public ArrayData set(int index, double value, boolean strict) {
        this.undefined.clear(index);
        return super.set(index, value, strict);
    }

    @Override
    public int getInt(int index) {
        if (this.undefined.isSet(index)) {
            return 0;
        }
        return super.getInt(index);
    }

    @Override
    public int getIntOptimistic(int index, int programPoint) {
        if (this.undefined.isSet(index)) {
            throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
        }
        return super.getIntOptimistic(index, programPoint);
    }

    @Override
    public double getDouble(int index) {
        if (this.undefined.isSet(index)) {
            return Double.NaN;
        }
        return super.getDouble(index);
    }

    @Override
    public double getDoubleOptimistic(int index, int programPoint) {
        if (this.undefined.isSet(index)) {
            throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
        }
        return super.getDoubleOptimistic(index, programPoint);
    }

    @Override
    public Object getObject(int index) {
        if (this.undefined.isSet(index)) {
            return ScriptRuntime.UNDEFINED;
        }
        return super.getObject(index);
    }

    @Override
    public ArrayData delete(int index) {
        this.undefined.clear(index);
        return super.delete(index);
    }

    @Override
    public Object pop() {
        long index = this.length() - 1L;
        if (super.has((int)index)) {
            boolean isUndefined = this.undefined.isSet(index);
            Object value = super.pop();
            return isUndefined ? ScriptRuntime.UNDEFINED : value;
        }
        return super.pop();
    }

    @Override
    public ArrayData slice(long from, long to) {
        ArrayData newArray = this.underlying.slice(from, to);
        UndefinedArrayFilter newFilter = new UndefinedArrayFilter(newArray);
        newFilter.getUndefined().copy(this.undefined);
        newFilter.getUndefined().shiftLeft(from, newFilter.length());
        return newFilter;
    }

    private BitVector getUndefined() {
        return this.undefined;
    }
}

