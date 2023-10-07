/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.AnyElements;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ContinuousArrayData;
import jdk.nashorn.internal.runtime.arrays.DeletedRangeArrayFilter;
import jdk.nashorn.internal.runtime.arrays.NumericElements;
import jdk.nashorn.internal.runtime.arrays.SparseArrayData;

final class ObjectArrayData
extends ContinuousArrayData
implements AnyElements {
    private Object[] array;
    private static final MethodHandle HAS_GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), ObjectArrayData.class, "getElem", Object.class, Integer.TYPE).methodHandle();
    private static final MethodHandle SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), ObjectArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Object.class).methodHandle();

    ObjectArrayData(Object[] array, int length) {
        super(length);
        assert (array.length >= length);
        this.array = array;
    }

    @Override
    public final Class<?> getElementType() {
        return Object.class;
    }

    @Override
    public final Class<?> getBoxedElementType() {
        return this.getElementType();
    }

    @Override
    public final int getElementWeight() {
        return 4;
    }

    @Override
    public final ContinuousArrayData widest(ContinuousArrayData otherData) {
        return otherData instanceof NumericElements ? this : otherData;
    }

    @Override
    public ObjectArrayData copy() {
        return new ObjectArrayData((Object[])this.array.clone(), (int)this.length());
    }

    @Override
    public Object[] asObjectArray() {
        return (long)this.array.length == this.length() ? (Object[])this.array.clone() : this.asObjectArrayCopy();
    }

    private Object[] asObjectArrayCopy() {
        long len = this.length();
        assert (len <= Integer.MAX_VALUE);
        Object[] copy = new Object[(int)len];
        System.arraycopy(this.array, 0, copy, 0, (int)len);
        return copy;
    }

    @Override
    public ObjectArrayData convert(Class<?> type) {
        return this;
    }

    @Override
    public ArrayData shiftLeft(int by) {
        if ((long)by >= this.length()) {
            this.shrink(0L);
        } else {
            System.arraycopy(this.array, by, this.array, 0, this.array.length - by);
        }
        this.setLength(Math.max(0L, this.length() - (long)by));
        return this;
    }

    @Override
    public ArrayData shiftRight(int by) {
        ArrayData newData = this.ensure((long)by + this.length() - 1L);
        if (newData != this) {
            newData.shiftRight(by);
            return newData;
        }
        System.arraycopy(this.array, 0, this.array, by, this.array.length - by);
        return this;
    }

    @Override
    public ArrayData ensure(long safeIndex) {
        if (safeIndex >= 131072L) {
            return new SparseArrayData(this, safeIndex + 1L);
        }
        int alen = this.array.length;
        if (safeIndex >= (long)alen) {
            int newLength = ArrayData.nextSize((int)safeIndex);
            this.array = Arrays.copyOf(this.array, newLength);
        }
        if (safeIndex >= this.length()) {
            this.setLength(safeIndex + 1L);
        }
        return this;
    }

    @Override
    public ArrayData shrink(long newLength) {
        Arrays.fill(this.array, (int)newLength, this.array.length, ScriptRuntime.UNDEFINED);
        return this;
    }

    @Override
    public ArrayData set(int index, Object value, boolean strict) {
        this.array[index] = value;
        this.setLength(Math.max((long)(index + 1), this.length()));
        return this;
    }

    @Override
    public ArrayData set(int index, int value, boolean strict) {
        this.array[index] = value;
        this.setLength(Math.max((long)(index + 1), this.length()));
        return this;
    }

    @Override
    public ArrayData set(int index, double value, boolean strict) {
        this.array[index] = value;
        this.setLength(Math.max((long)(index + 1), this.length()));
        return this;
    }

    @Override
    public ArrayData setEmpty(int index) {
        this.array[index] = ScriptRuntime.EMPTY;
        return this;
    }

    @Override
    public ArrayData setEmpty(long lo, long hi) {
        Arrays.fill(this.array, (int)Math.max(lo, 0L), (int)Math.min(hi + 1L, Integer.MAX_VALUE), ScriptRuntime.EMPTY);
        return this;
    }

    private Object getElem(int index) {
        if (this.has(index)) {
            return this.array[index];
        }
        throw new ClassCastException();
    }

    private void setElem(int index, Object elem) {
        if (this.hasRoomFor(index)) {
            this.array[index] = elem;
            return;
        }
        throw new ClassCastException();
    }

    @Override
    public MethodHandle getElementGetter(Class<?> returnType, int programPoint) {
        if (returnType.isPrimitive()) {
            return null;
        }
        return this.getContinuousElementGetter(HAS_GET_ELEM, returnType, programPoint);
    }

    @Override
    public MethodHandle getElementSetter(Class<?> elementType) {
        return this.getContinuousElementSetter(SET_ELEM, Object.class);
    }

    @Override
    public int getInt(int index) {
        return JSType.toInt32(this.array[index]);
    }

    @Override
    public double getDouble(int index) {
        return JSType.toNumber(this.array[index]);
    }

    @Override
    public Object getObject(int index) {
        return this.array[index];
    }

    @Override
    public boolean has(int index) {
        return 0 <= index && (long)index < this.length();
    }

    @Override
    public ArrayData delete(int index) {
        this.setEmpty(index);
        return new DeletedRangeArrayFilter(this, index, index);
    }

    @Override
    public ArrayData delete(long fromIndex, long toIndex) {
        this.setEmpty(fromIndex, toIndex);
        return new DeletedRangeArrayFilter(this, fromIndex, toIndex);
    }

    @Override
    public double fastPush(int arg) {
        return this.fastPush((Object)arg);
    }

    @Override
    public double fastPush(long arg) {
        return this.fastPush((Object)arg);
    }

    @Override
    public double fastPush(double arg) {
        return this.fastPush((Object)arg);
    }

    @Override
    public double fastPush(Object arg) {
        int len = (int)this.length();
        if (len == this.array.length) {
            this.array = Arrays.copyOf(this.array, ObjectArrayData.nextSize(len));
        }
        this.array[len] = arg;
        return this.increaseLength();
    }

    @Override
    public Object fastPopObject() {
        if (this.length() == 0L) {
            return ScriptRuntime.UNDEFINED;
        }
        int newLength = (int)this.decreaseLength();
        Object elem = this.array[newLength];
        this.array[newLength] = ScriptRuntime.EMPTY;
        return elem;
    }

    @Override
    public Object pop() {
        if (this.length() == 0L) {
            return ScriptRuntime.UNDEFINED;
        }
        int newLength = (int)this.length() - 1;
        Object elem = this.array[newLength];
        this.setEmpty(newLength);
        this.setLength(newLength);
        return elem;
    }

    @Override
    public ArrayData slice(long from, long to) {
        long start = from < 0L ? from + this.length() : from;
        long newLength = to - start;
        return new ObjectArrayData(Arrays.copyOfRange(this.array, (int)from, (int)to), (int)newLength);
    }

    @Override
    public ArrayData push(boolean strict, Object item) {
        long len = this.length();
        ArrayData newData = this.ensure(len);
        if (newData == this) {
            this.array[(int)len] = item;
            return this;
        }
        return newData.set((int)len, item, strict);
    }

    @Override
    public ArrayData fastSplice(int start, int removed2, int added) throws UnsupportedOperationException {
        ArrayData returnValue;
        long oldLength = this.length();
        long newLength = oldLength - (long)removed2 + (long)added;
        if (newLength > 131072L && newLength > (long)this.array.length) {
            throw new UnsupportedOperationException();
        }
        ArrayData arrayData = returnValue = removed2 == 0 ? EMPTY_ARRAY : new ObjectArrayData(Arrays.copyOfRange(this.array, start, start + removed2), removed2);
        if (newLength != oldLength) {
            Object[] newArray;
            if (newLength > (long)this.array.length) {
                newArray = new Object[ArrayData.nextSize((int)newLength)];
                System.arraycopy(this.array, 0, newArray, 0, start);
            } else {
                newArray = this.array;
            }
            System.arraycopy(this.array, start + removed2, newArray, start + added, (int)(oldLength - (long)start - (long)removed2));
            this.array = newArray;
            this.setLength(newLength);
        }
        return returnValue;
    }

    @Override
    public ContinuousArrayData fastConcat(ContinuousArrayData otherData) {
        int otherLength = (int)otherData.length();
        int thisLength = (int)this.length();
        assert (otherLength > 0 && thisLength > 0);
        Object[] otherArray = ((ObjectArrayData)otherData).array;
        int newLength = otherLength + thisLength;
        Object[] newArray = new Object[ArrayData.alignUp(newLength)];
        System.arraycopy(this.array, 0, newArray, 0, thisLength);
        System.arraycopy(otherArray, 0, newArray, thisLength, otherLength);
        return new ObjectArrayData(newArray, newLength);
    }

    public String toString() {
        assert (this.length() <= (long)this.array.length) : this.length() + " > " + this.array.length;
        return this.getClass().getSimpleName() + ':' + Arrays.toString(Arrays.copyOf(this.array, (int)this.length()));
    }
}

