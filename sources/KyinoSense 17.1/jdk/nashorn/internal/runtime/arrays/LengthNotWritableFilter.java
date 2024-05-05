/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ArrayFilter;

final class LengthNotWritableFilter
extends ArrayFilter {
    private final SortedMap<Long, Object> extraElements;

    LengthNotWritableFilter(ArrayData underlying) {
        this(underlying, new TreeMap<Long, Object>());
    }

    private LengthNotWritableFilter(ArrayData underlying, SortedMap<Long, Object> extraElements) {
        super(underlying);
        this.extraElements = extraElements;
    }

    @Override
    public ArrayData copy() {
        return new LengthNotWritableFilter(this.underlying.copy(), new TreeMap<Long, Object>(this.extraElements));
    }

    @Override
    public boolean has(int index) {
        return super.has(index) || this.extraElements.containsKey(index);
    }

    @Override
    public void setLength(long length) {
    }

    @Override
    public ArrayData ensure(long index) {
        return this;
    }

    @Override
    public ArrayData slice(long from, long to) {
        return new LengthNotWritableFilter(this.underlying.slice(from, to), this.extraElements.subMap(from, to));
    }

    private boolean checkAdd(long index, Object value) {
        if (index >= this.length()) {
            this.extraElements.put(index, value);
            return true;
        }
        return false;
    }

    private Object get(long index) {
        Object obj = this.extraElements.get(index);
        if (obj == null) {
            return ScriptRuntime.UNDEFINED;
        }
        return obj;
    }

    @Override
    public int getInt(int index) {
        if ((long)index >= this.length()) {
            return JSType.toInt32(this.get(index));
        }
        return this.underlying.getInt(index);
    }

    @Override
    public int getIntOptimistic(int index, int programPoint) {
        if ((long)index >= this.length()) {
            return JSType.toInt32Optimistic(this.get(index), programPoint);
        }
        return this.underlying.getIntOptimistic(index, programPoint);
    }

    @Override
    public double getDouble(int index) {
        if ((long)index >= this.length()) {
            return JSType.toNumber(this.get(index));
        }
        return this.underlying.getDouble(index);
    }

    @Override
    public double getDoubleOptimistic(int index, int programPoint) {
        if ((long)index >= this.length()) {
            return JSType.toNumberOptimistic(this.get(index), programPoint);
        }
        return this.underlying.getDoubleOptimistic(index, programPoint);
    }

    @Override
    public Object getObject(int index) {
        if ((long)index >= this.length()) {
            return this.get(index);
        }
        return this.underlying.getObject(index);
    }

    @Override
    public ArrayData set(int index, Object value, boolean strict) {
        if (this.checkAdd(index, value)) {
            return this;
        }
        this.underlying = this.underlying.set(index, value, strict);
        return this;
    }

    @Override
    public ArrayData set(int index, int value, boolean strict) {
        if (this.checkAdd(index, value)) {
            return this;
        }
        this.underlying = this.underlying.set(index, value, strict);
        return this;
    }

    @Override
    public ArrayData set(int index, double value, boolean strict) {
        if (this.checkAdd(index, value)) {
            return this;
        }
        this.underlying = this.underlying.set(index, value, strict);
        return this;
    }

    @Override
    public ArrayData delete(int index) {
        this.extraElements.remove(index);
        this.underlying = this.underlying.delete(index);
        return this;
    }

    @Override
    public ArrayData delete(long fromIndex, long toIndex) {
        Iterator<Long> iter = this.extraElements.keySet().iterator();
        while (iter.hasNext()) {
            long next = iter.next();
            if (next >= fromIndex && next <= toIndex) {
                iter.remove();
            }
            if (next <= toIndex) continue;
            break;
        }
        this.underlying = this.underlying.delete(fromIndex, toIndex);
        return this;
    }

    @Override
    public Iterator<Long> indexIterator() {
        List<Long> keys2 = this.computeIteratorKeys();
        keys2.addAll(this.extraElements.keySet());
        return keys2.iterator();
    }
}

